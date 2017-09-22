package info.sleeplessacorn.nomagi.proxy;

import info.sleeplessacorn.nomagi.core.ModObjects;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ProxyCommon {

    public void preInit(FMLPreInitializationEvent event) {
        ModObjects.registerObjects();
    }

    public void postInit(FMLPostInitializationEvent event) {
    }
}
