package info.sleeplessacorn.nomagi;

import net.minecraftforge.common.config.Config;

@Config(modid = Nomagi.MODID, name = Nomagi.MODID + "/" + Nomagi.MODID)
public class ConfigHandler {

    @Config.Comment({"ID number to use for the Tent dimension."})
    public static int tentDimensionId = 10;
}
