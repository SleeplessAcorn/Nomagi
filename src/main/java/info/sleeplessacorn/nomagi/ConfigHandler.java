package info.sleeplessacorn.nomagi;

import net.minecraftforge.common.config.Config;

@Config(modid = Nomagi.MODID, name = Nomagi.MODID + "/" + Nomagi.MODID)
public class ConfigHandler {

    @Config.Comment({"ID number to use for the Tent dimension."})
    public static int tentDimensionId = 10;

    @Config.Comment({"Number of chunks to place between each newly generated tent."})
    @Config.RangeInt(min = 16, max = 64)
    public static int tentSpacing = 20;

    @Config.Comment({"Max number of rooms (chunks) a tent can extend in any given direction.", "A value of 0 means only the initial room is available.", "You should modify \"tentSpacing\" as needed."})
    @Config.RangeInt(min = 0, max = 16)
    public static int tentRadius = 2;
}
