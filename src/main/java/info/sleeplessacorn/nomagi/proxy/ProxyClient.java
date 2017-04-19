package info.sleeplessacorn.nomagi.proxy;

import info.sleeplessacorn.nomagi.core.ModObjects;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraftforge.client.model.ModelLoader;

public class ProxyClient extends ProxyCommon {

    @Override
    public void preInit() {
        super.preInit();

        ModelLoader.setCustomStateMapper(ModObjects.DOOR, new StateMap.Builder().ignore(BlockDoor.POWERED).build());
    }
}
