package gungun974.cupboards;

import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.ModelEntrypoint;

public class CupboardsModels implements ModelEntrypoint {

	@Override
	public void initBlockModels(BlockModelDispatcher dispatcher) {
		ModelHelper.setBlockModel(CupboardsBlocks.CUPBOARD, () -> new BlockModelCupboard<>(CupboardsBlocks.CUPBOARD, "minecraft:block/chest/planks/")
			.setAllTextures(0, "minecraft:block/chest/planks/top")
		);

		ModelHelper.setBlockModel(CupboardsBlocks.CUPBOARD_PAINTED, () -> new BlockModelCupboardPainted<>(CupboardsBlocks.CUPBOARD_PAINTED)
			.setAllTextures(0, "minecraft:block/chest/planks/top")
		);

		CupboardsMod.LOGGER.info("Block Models initialized.");
	}

	@Override
	public void initItemModels(ItemModelDispatcher dispatcher) {

	}

	@Override
	public void initEntityModels(EntityRenderDispatcher dispatcher) {
	}

	@Override
	public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {
	}

	@Override
	public void initBlockColors(BlockColorDispatcher dispatcher) {
	}
}
