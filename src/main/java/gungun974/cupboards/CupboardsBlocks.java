package gungun974.cupboards;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.item.block.ItemBlockPainted;
import net.minecraft.core.sound.BlockSounds;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.helper.DyeColor;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.EntityHelper;

import java.util.ArrayList;
import java.util.List;

import static gungun974.cupboards.CupboardsMod.MOD_ID;

public class CupboardsBlocks {
	public static Block<BlockLogicCupboard> CUPBOARD;
	public static Block<BlockLogicCupboardPainted> CUPBOARD_PAINTED;

	private static int currentGeneratedId;

	private static int generateNexId() {
		return currentGeneratedId++;
	}

	public static void RegisterBlocks() {
		currentGeneratedId = CupboardsMod.startBlockID;

		EntityHelper.createTileEntity(TileEntityCupboard.class, NamespaceID.getPermanent(MOD_ID, "cupboard"));

		CUPBOARD = new BlockBuilder(MOD_ID)
			.setHardness(2.5f)
			.setResistance(5.0f)
			.setTags(BlockTags.FENCES_CONNECT, BlockTags.MINEABLE_BY_AXE)
			.setTileEntity(TileEntityCupboard::new)
			.setBlockSound(BlockSounds.WOOD)
			.build("cupboard", generateNexId(), b -> new BlockLogicCupboard(b, Material.wood));


		CUPBOARD_PAINTED = new BlockBuilder(MOD_ID)
			.setHardness(2.5f)
			.setResistance(5.0f)
			.setTags(BlockTags.FENCES_CONNECT, BlockTags.MINEABLE_BY_AXE)
			.setTileEntity(TileEntityCupboard::new)
			.setBlockSound(BlockSounds.WOOD)
			.setBlockItem((b) -> new ItemBlockPainted<>(b, true))
			.build("cupboard_painted", generateNexId(), b -> new BlockLogicCupboardPainted(b, Material.wood));
	}
}
