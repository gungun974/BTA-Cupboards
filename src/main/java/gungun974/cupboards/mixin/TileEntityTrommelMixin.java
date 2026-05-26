package gungun974.cupboards.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import gungun974.cupboards.BlockLogicCupboard;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityTrommel;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.world.pos.TilePos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TileEntityTrommel.class, remap = false)
public abstract class TileEntityTrommelMixin extends TileEntity {

	@Inject(
		method = "sieveItem",
		cancellable = true,
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/block/Block;hasLogicClass(Lnet/minecraft/core/block/Block;Ljava/lang/Class;)Z"
		)
	)
	private void addSupportForCupboard(
		int slotIndex,
		CallbackInfo ci,
		@Local(name = "adjacentId") int adjacentId,
		@Local(name = "queryPos") TilePos queryPos,
		@Local(name = "itemResult") ItemStack itemResult
	) {
		if (this.worldObj == null) return;
		if (!Block.hasLogicClass(Blocks.blocksList[adjacentId], BlockLogicCupboard.class)) return;
		if (itemResult == null) return;

		Container chest = BlockLogicCupboard.getInventory(this.worldObj, queryPos);
		if (chest == null) return;

		for (int i = 0; i < chest.getContainerSize(); ++i) {
			ItemStack slot = chest.getItem(i);
			if (slot != null
				&& slot.itemID == itemResult.itemID
				&& slot.getMetadata() == itemResult.getMetadata()) {
				while (slot.stackSize + 1 <= slot.getMaxStackSize()) {
					++slot.stackSize;
					chest.setItem(i, slot);
					--itemResult.stackSize;
					if (itemResult.stackSize <= 0) {
						ci.cancel();
						return;
					}
				}
			}
		}

		if (itemResult.stackSize <= 0) {
			ci.cancel();
			return;
		}

		for (int i = 0; i < chest.getContainerSize(); ++i) {
			if (chest.getItem(i) == null) {
				chest.setItem(i, itemResult);
				ci.cancel();
				return;
			}
		}
	}
}
