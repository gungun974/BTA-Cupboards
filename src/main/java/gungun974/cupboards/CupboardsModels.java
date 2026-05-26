package gungun974.cupboards;

import net.minecraft.client.render.EntityRendererDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.ModelEntrypoint;

public class CupboardsModels implements ModelEntrypoint {

	@Override
	public void initBlockModels(BlockModelDispatcher dispatcher) {
		ModelHelper.setBlockModel(CupboardsBlocks.CUPBOARD, () -> new BlockModelCupboard<>(CupboardsBlocks.CUPBOARD )
		);

		ModelHelper.setBlockModel(CupboardsBlocks.CUPBOARD_PAINTED, () -> new BlockModelCupboardPainted<>(CupboardsBlocks.CUPBOARD_PAINTED));

		CupboardsMod.LOGGER.info("Block Models initialized.");
	}

	@Override
	public void initItemModels(ItemModelDispatcher dispatcher) {

	}

	@Override
	public void initEntityModels(EntityRendererDispatcher dispatcher) {

	}

	@Override
	public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {
		dispatcher.assignRenderer(TileEntityCupboard.class, new TileEntityRendererCupboard());
	}

	@Override
	public void initBlockColors(BlockColorDispatcher dispatcher) {
	}
}
