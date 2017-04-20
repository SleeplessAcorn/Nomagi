package info.sleeplessacorn.nomagi.proxy;

import info.sleeplessacorn.nomagi.client.FontRendererSmall;
import info.sleeplessacorn.nomagi.client.gui.GuiRoomCreation;
import info.sleeplessacorn.nomagi.core.ModObjects;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ProxyClient extends ProxyCommon {

    public static FontRendererSmall fontRenderer;

    @Override
    public void preInit() {
        super.preInit();

        ModelLoader.setCustomStateMapper(ModObjects.DOOR, new StateMap.Builder().ignore(BlockDoor.POWERED).build());
    }

    @Override
    public void postInit() {
        super.postInit();

        fontRenderer = new FontRendererSmall(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().renderEngine);
    }

    @Override
    public void displayRoomControllerGui() {
        Minecraft.getMinecraft().displayGuiScreen(new GuiRoomCreation());
    }
}
