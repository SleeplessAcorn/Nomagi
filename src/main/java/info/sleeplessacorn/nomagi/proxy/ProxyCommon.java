package info.sleeplessacorn.nomagi.proxy;

import info.sleeplessacorn.nomagi.core.ModObjects;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ProxyCommon {

    public void onPreInit(FMLPreInitializationEvent event) {
        ModObjects.registerObjects();
    }

    public void onPostInit(FMLPostInitializationEvent event) {
    }

}
