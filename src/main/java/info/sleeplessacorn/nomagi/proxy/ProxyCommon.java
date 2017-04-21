package info.sleeplessacorn.nomagi.proxy;

import info.sleeplessacorn.nomagi.core.ModObjects;
import info.sleeplessacorn.nomagi.util.GuiHandler;

public class ProxyCommon {

    public void preInit() {
        ModObjects.preInit();
    }

    public void postInit() {
        GuiHandler.init();
    }

    public void displayRoomControllerGui() {

    }
}
