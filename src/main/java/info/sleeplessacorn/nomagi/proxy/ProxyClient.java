package info.sleeplessacorn.nomagi.proxy;

import info.sleeplessacorn.nomagi.client.FontRendererSmall;
import info.sleeplessacorn.nomagi.core.ModObjects;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ProxyClient extends ProxyCommon {

    public static FontRendererSmall fontRenderer;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        ModelLoader.setCustomStateMapper(ModObjects.DOOR, new StateMap.Builder().ignore(BlockDoor.POWERED).build());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        Minecraft mc = Minecraft.getMinecraft();
        ResourceLocation asciiSprite = new ResourceLocation("minecraft:textures/font/ascii.png");
        fontRenderer = new FontRendererSmall(mc.gameSettings, asciiSprite, mc.renderEngine);
    }
}
