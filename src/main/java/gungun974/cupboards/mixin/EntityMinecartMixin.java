package gungun974.cupboards.mixin;

import gungun974.cupboards.CupboardsBlocks;
import gungun974.cupboards.TileEntityCupboard;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.motion.CarriedBlock;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.entity.vehicle.EntityMinecart;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.world.ICarriable;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityMinecart.class, remap = false)
public abstract class EntityMinecartMixin extends Entity {

	public EntityMinecartMixin(@Nullable World world) {
		super(world);
	}

	@Shadow
	public abstract byte getType();

	@Shadow
	public abstract void setType(byte type);

	@Shadow
	public abstract void setMeta(int meta);

	@Shadow
	public abstract int getContainerSize();

	@Shadow
	public abstract void setItem(int index, @Nullable ItemStack itemstack);

	@Shadow
	public abstract @Nullable ItemStack getItem(int index);

	@Shadow
	public abstract int getMeta();

	@Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/vehicle/EntityMinecart;getType()B"))
	public void dropCupboard(Entity entity, int baseDamage, DamageType type, CallbackInfoReturnable<Boolean> cir) {
		if (this.getType() == 42) {
			if ((this.getMeta() & 1) != 0) {
				this.dropItem(new ItemStack(CupboardsBlocks.CUPBOARD_PAINTED, 1, this.getMeta() & 240), 0.0F);
			} else {
				this.dropItem(CupboardsBlocks.CUPBOARD.id(), 1, 0.0F);
			}
		}
	}

	@Inject(method = "interact", at = @At("HEAD"), cancellable = true)
	public void interactWithCupboard(Player player, CallbackInfoReturnable<Boolean> cir) {
		switch (this.getType()) {
			case 0:
				if (!this.world.isClientSide && this.passenger == null && player.isSneaking() && player.getHeldObject() instanceof CarriedBlock) {
					CarriedBlock carriedBlock = (CarriedBlock)player.getHeldObject();
					if (carriedBlock.entity instanceof TileEntityCupboard) {
						TileEntityCupboard chest = (TileEntityCupboard)carriedBlock.entity;
						this.setType((byte)42);
						if (carriedBlock.blockId == CupboardsBlocks.CUPBOARD_PAINTED.id()) {
							this.setMeta(carriedBlock.metadata & 240 | 1);
						} else {
							this.setMeta(carriedBlock.metadata & 240);
						}

						for(int i = 0; i < this.getContainerSize(); ++i) {
							this.setItem(i, chest.getItem(i));
							chest.setItem(i, (ItemStack)null);
						}

						player.setHeldObject((ICarriable)null);
						cir.setReturnValue(true);
						cir.cancel();
						return;
					}
				}
				break;
			case 42:
				if (!this.world.isClientSide) {
					if (player.isSneaking() && player.inventory.getCurrentItem() == null && player.getHeldObject() == null) {
						TileEntityCupboard tileEntityChest = new TileEntityCupboard();

						for (int i = 0; i < this.getContainerSize(); ++i) {
							tileEntityChest.setItem(i, this.getItem(i));
							this.setItem(i, (ItemStack) null);
						}

						tileEntityChest.worldObj = null;
						Block<?> block;
						if ((this.getMeta() & 1) != 0) {
							block = CupboardsBlocks.CUPBOARD_PAINTED;
						} else {
							block = CupboardsBlocks.CUPBOARD;
						}

						tileEntityChest.carriedBlock = tileEntityChest.getCarriedEntry(this.world, player, block, this.getMeta() & 240);
						player.setHeldObject(tileEntityChest.carriedBlock);
						this.setType((byte) 0);
						this.setMeta(0);
					} else {
						player.displayContainerScreen((Container) this);
					}

					cir.setReturnValue(true);
					cir.cancel();
				}
		}
	}
}
