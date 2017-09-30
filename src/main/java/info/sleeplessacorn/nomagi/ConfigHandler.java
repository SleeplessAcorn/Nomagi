package info.sleeplessacorn.nomagi;

import net.minecraftforge.common.config.Config;

@Config(modid = Nomagi.ID, name = Nomagi.ID)
@Config.LangKey("config.nomagi")
public final class ConfigHandler {

    @Config.Comment({ "The ID that should be used for the tent dimension [default: 18]" })
    @Config.LangKey("config.nomagi.tentDimensionId")
    public static int tentDimensionId = 18;

    @Config.Comment({ "The number of empty chunks to generate between each generated tent [default: 32]" })
    @Config.RangeInt(min = 16, max = 128)
    @Config.LangKey("config.nomagi.tentSpacing")
    public static int tentSpacing = 32;

    @Config.Comment({ "The maximum number of rooms a tent can create in any given direction from the core tent [default: 2]", "A value of 0 means no additional rooms will be permitted outside the core tent", "Ensure that the tent spacing is modified to account for the value set here" })
    @Config.RangeInt(min = 0, max = 16)
    @Config.LangKey("config.nomagi.tentRadius")
    public static int tentRadius = 2;
}
