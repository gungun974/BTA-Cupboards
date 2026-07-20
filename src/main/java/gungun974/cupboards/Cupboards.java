package gungun974.cupboards;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.event.defs.CommonEvents;
import turniplabs.halplibe.util.dependency.Key;

public class Cupboards implements ModInitializer {
	public static final String MOD_ID = HalpLibe.registerMod("cupboards");
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static int startBlockID = 1900;

	@Override
	public void onInitialize() {
		LOGGER.info("Cupboards initialized.");
		CupboardsBlocks.RegisterBlocks();


		LOGGER.info("Binding to events...");
		CommonEvents.RECIPES_READY.listen(Key.of(MOD_ID), () -> new CupboardsRecipe().initNamespaces());
		CommonEvents.RECIPES_READY.listen(Key.of(MOD_ID), () -> new CupboardsRecipe().onRecipesReady());
	}
}
