package elocindev.fancy_hotbar.config;

import elocindev.necronomicon.api.config.v1.NecConfigAPI;
import elocindev.necronomicon.config.Comment;
import elocindev.necronomicon.config.NecConfig;

public class FancyHotbarConfig {
    @NecConfig
    public static FancyHotbarConfig INSTANCE;

    public static String getFile() {
        return NecConfigAPI.getFile("fancy_hotbar.json5");
    }

    public class HotbarConfig {
        public boolean enable = true;
        public int x_offset = 0;
        public int y_offset = 0;
    }

    public class ExperienceCfg {
        public boolean enable = true;
        public int xp_color = 0x00FF00;
        public int xp_outline_color = 0x000000;
    }

    @Comment("-----------------------------------------------------------")
    @Comment("              Fancy Hotbar by ElocinDev")
    @Comment("-----------------------------------------------------------")

    public HotbarConfig custom_hotbar               = new HotbarConfig();
    public ExperienceCfg custom_experience_number   = new ExperienceCfg();

    @Comment("-----------------------------------------------------------")
    @Comment("No touchy!")
    public int config_version = 1;
}
