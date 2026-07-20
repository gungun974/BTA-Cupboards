package gungun974.cupboards;

import net.minecraft.client.render.EntityRendererDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;

public class CupboardsModels {

	public void initBlockModels(BlockModelDispatcher dispatcher) {
		BlockModelDispatcher.getInstance().addDispatch(CupboardsBlocks.CUPBOARD, new BlockModelCupboard<>(CupboardsBlocks.CUPBOARD));

		BlockModelDispatcher.getInstance().addDispatch(CupboardsBlocks.CUPBOARD_PAINTED, new BlockModelCupboardPainted<>(CupboardsBlocks.CUPBOARD_PAINTED));

		Cupboards.LOGGER.info("Block Models initialized.");
	}

	public void initItemModels(ItemModelDispatcher dispatcher) {

	}

	public void initEntityModels(EntityRendererDispatcher dispatcher) {

	}

	public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {
		dispatcher.assignRenderer(TileEntityCupboard.class, new TileEntityRendererCupboard<>());
	}
}
