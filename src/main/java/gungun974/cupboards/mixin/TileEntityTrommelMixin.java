package gungun974.cupboards.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import gungun974.cupboards.BlockLogicCupboard;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityTrommel;
import net.minecraft.core.player.inventory.container.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TileEntityTrommel.class, remap = false)
public class TileEntityTrommelMixin extends TileEntity {
	@Inject(
		method = "sieveItem",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Block;hasLogicClass(Lnet/minecraft/core/block/Block;Ljava/lang/Class;)Z")
	)
	void addSupportForCupboard(int slotIndex,
							   CallbackInfo ci,
							   @Local(name = "adjacentId") int adjacentId,
							   @Local(name = "xOffset") int xOffset,
							   @Local(name = "zOffset") int zOffset,
							   @Local(name = "chest") LocalRef<Container> chest
	) {
		if (Block.hasLogicClass(Blocks.blocksList[adjacentId], BlockLogicCupboard.class)) {
			assert this.worldObj != null;

			chest.set(BlockLogicCupboard.getInventory(this.worldObj, this.x + xOffset, this.y, this.z + zOffset));
		}

	}
}
