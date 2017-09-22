package info.sleeplessacorn.nomagi;

import net.minecraftforge.common.config.Config;

@Config(modid = Nomagi.ID, name = Nomagi.ID + "/" + Nomagi.ID)
@Config.LangKey("config.nomagi")
public class ConfigHandler {

    @Config.Name("Tent Dimension ID")
    @Config.Comment({"The ID that should be used for the tent dimension"})
    @Config.LangKey("config.nomagi.tentDimensionId")
    public static int tentDimensionId = 10;

    @Config.Name("Tent Spacing")
    @Config.Comment({"The number of empty chunks to generate between each generated tent"})
    @Config.RangeInt(min = 16, max = 64)
    @Config.LangKey("config.nomagi.tentSpacing")
    public static int tentSpacing = 20;

    @Config.Name("Tent Radius")
    @Config.Comment({"The maximum number of rooms a tent can create in any given direction from the core room",
                     "A value of 0 means only the core room itself will only be permitted",
                     "Ensure that the tent spacing is modified to account for the value set here"})
    @Config.RangeInt(min = 0, max = 16)
    @Config.LangKey("config.nomagi.tentRadius")
    public static int tentRadius = 2;

}
