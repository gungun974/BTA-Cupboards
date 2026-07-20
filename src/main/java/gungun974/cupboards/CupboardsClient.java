package gungun974.cupboards;

import net.fabricmc.api.ClientModInitializer;
import turniplabs.halplibe.event.defs.ClientEvents;
import turniplabs.halplibe.util.dependency.Key;

import static gungun974.cupboards.Cupboards.MOD_ID;

public class CupboardsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		Cupboards.LOGGER.info("Binding to client events...");

		ClientEvents.BLOCK_MODEL_RELOAD.listen(Key.of(MOD_ID), (t) -> new CupboardsModels().initBlockModels(t));
		ClientEvents.ITEM_MODEL_RELOAD.listen(Key.of(MOD_ID), (t) -> new CupboardsModels().initItemModels(t));
		ClientEvents.TILE_ENTITY_RENDERER_RELOAD.listen(Key.of(MOD_ID), (t) -> new CupboardsModels().initTileEntityModels(t));
		ClientEvents.ENTITY_RENDERER_RELOAD.listen(Key.of(MOD_ID), (t) -> new CupboardsModels().initEntityModels(t));
	}
}
