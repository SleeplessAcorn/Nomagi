package info.sleeplessacorn.nomagi;

import net.minecraftforge.common.config.Config;

@Config(modid = Nomagi.ID, name = Nomagi.ID)
@Config.LangKey("config.nomagi")
public final class ModConfig {

    @Config.Name("Tent Dimension ID")
    @Config.Comment({ "The ID that should be used for the data dimension [default: 18]" })
    @Config.LangKey("config.nomagi.tentDimensionId")
    public static int tentDimensionId = 18;

    @Config.Name("Tent Spacing")
    @Config.Comment({ "The number of empty chunks to generate between each generated data [default: 32]" })
    @Config.RangeInt(min = 16, max = 128)
    @Config.LangKey("config.nomagi.tentSpacing")
    public static int tentSpacing = 32;

    @Config.Name("Tent Radius")
    @Config.Comment({ "The maximum number of rooms a data can create in any given direction from the core data [default: 2]",
                      "A value of 0 means no additional rooms will be permitted outside the core data",
                      "Ensure that the data spacing is modified to account for the value set here" })
    @Config.RangeInt(min = 0, max = 16)
    @Config.LangKey("config.nomagi.tentRadius")
    public static int tentRadius = 2;

}
