package gungun974.cupboards.mixin;

import gungun974.cupboards.CupboardsBlocks;
import net.minecraft.core.block.Block;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.menu.MenuInventoryCreative;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = MenuInventoryCreative.class, remap = false)
public class MenuInventoryCreativeMixin {
	@Shadow
	public static List<ItemStack> creativeItems;

	@Shadow
	public static int creativeItemsCount;
	@Unique
	private static int extraCount = 0;

	@Redirect(
		method = "<clinit>",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Block;hasTag(Lnet/minecraft/core/data/tag/Tag;)Z")
	)
	private static boolean addBlocks(Block<?> block, Tag<Block<?>> tag) {
		if (block.id() == CupboardsBlocks.CUPBOARD_PAINTED.id()) {
			int before = creativeItems.size();

			creativeItems.add(new ItemStack(CupboardsBlocks.CUPBOARD_PAINTED));

			for (int i = 16; i < 256; i += 16) {
				creativeItems.add(new ItemStack(CupboardsBlocks.CUPBOARD_PAINTED, 1, i));
			}
			extraCount += creativeItems.size() - before;
			return true;
		}
		return block.hasTag(tag);
	}

	@Inject(
		method = "<clinit>",
		at = @At(value = "FIELD", target = "Lnet/minecraft/core/player/inventory/menu/MenuInventoryCreative;creativeItemsCount:I", shift = At.Shift.AFTER)
	)
	private static void addUpItemCount(CallbackInfo ci) {
		creativeItemsCount += extraCount;
	}
}

