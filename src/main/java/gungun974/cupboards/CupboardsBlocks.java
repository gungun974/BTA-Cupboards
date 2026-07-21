package gungun974.cupboards;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntityDispatcher;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlockPainted;
import net.minecraft.core.sound.BlockSounds;
import net.minecraft.core.util.collection.NamespaceID;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryCategory;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;

import java.util.LinkedList;
import java.util.List;

import static gungun974.cupboards.Cupboards.MOD_ID;

public class CupboardsBlocks {
	public static Block<BlockLogicCupboard> CUPBOARD;
	public static Block<BlockLogicCupboardPainted> CUPBOARD_PAINTED;

	private static int currentGeneratedId;

	private static int generateNexId() {
		return currentGeneratedId++;
	}

	public static void RegisterBlocks() {
		currentGeneratedId = Cupboards.startBlockID;

		TileEntityDispatcher.addMapping(TileEntityCupboard.class, new NamespaceID(MOD_ID, "cupboard"));

		CUPBOARD = new BlockBuilder(MOD_ID)
			.setHardness(2.5f)
			.setResistance(5.0f)
			.setTags(BlockTags.FENCES_CONNECT, BlockTags.MINEABLE_BY_AXE)
			.setTileEntity(TileEntityCupboard::new)
			.setBlockSound(BlockSounds.WOOD)
			.setCreativeInventoryPlacement(new CreativeInventoryPlacement.Category(CreativeInventoryCategory.MISCELLANEOUS))
			.build("cupboard", generateNexId(), b -> new BlockLogicCupboard(b, Materials.WOOD));


		CUPBOARD_PAINTED = new BlockBuilder(MOD_ID)
			.setHardness(2.5f)
			.setResistance(5.0f)
			.setTags(BlockTags.FENCES_CONNECT, BlockTags.MINEABLE_BY_AXE, BlockTags.NOT_IN_CREATIVE_MENU)
			.setCreativeInventoryPlacement(new CreativeInventoryPlacement.Category(CreativeInventoryCategory.MISCELLANEOUS).setCustomSupplier(() -> {
				List<ItemStack> creativeItems = new LinkedList<>();

				creativeItems.add(new ItemStack(CupboardsBlocks.CUPBOARD_PAINTED));

				for (int i = 16; i < 256; i += 16) {
					creativeItems.add(new ItemStack(CupboardsBlocks.CUPBOARD_PAINTED, 1, i));
				}

				return creativeItems;
			}))
			.setTileEntity(TileEntityCupboard::new)
			.setBlockSound(BlockSounds.WOOD)
			.setBlockItem((b) -> new ItemBlockPainted<>(b, true))
			.build("cupboard_painted", generateNexId(), b -> new BlockLogicCupboardPainted(b, Materials.WOOD));
	}
}
