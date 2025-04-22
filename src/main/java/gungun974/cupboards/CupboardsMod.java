package gungun974.cupboards;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.GameStartEntrypoint;

public class CupboardsMod implements ModInitializer, GameStartEntrypoint {
    public static final String MOD_ID = "cupboards";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static int startBlockID = 1900;

    @Override
    public void onInitialize() {
        LOGGER.info("Cupboards initialized.");
		CupboardsBlocks.RegisterBlocks();
    }

	@Override
	public void beforeGameStart() {}

	@Override
	public void afterGameStart() {

	}
}
