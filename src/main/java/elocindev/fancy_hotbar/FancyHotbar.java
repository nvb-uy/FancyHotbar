package elocindev.fancy_hotbar;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import elocindev.fancy_hotbar.config.FancyHotbarConfig;
import elocindev.necronomicon.api.config.v1.NecConfigAPI;

public class FancyHotbar implements ModInitializer {
	public static final String MODID = "fancy_hotbar";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		NecConfigAPI.registerConfig(FancyHotbarConfig.class);
	}
}