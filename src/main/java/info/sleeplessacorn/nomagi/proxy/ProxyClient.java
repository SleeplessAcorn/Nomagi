package info.sleeplessacorn.nomagi.proxy;

import info.sleeplessacorn.nomagi.client.FontRendererSmall;
import info.sleeplessacorn.nomagi.core.ModObjects;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ProxyClient extends ProxyCommon {

    public static FontRendererSmall fontRenderer;

    @Override
    public void preInit() {
        super.preInit();

        handleModel(Item.getItemFromBlock(ModObjects.DOOR), 0, "inventory");
        handleModel(Item.getItemFromBlock(ModObjects.DOOR_CONTROLLER), 0, "facing=east,type=tent");
        handleModel(Item.getItemFromBlock(ModObjects.DOOR_CONTROLLER), 1, "facing=east,type=brick");

        ModelLoader.setCustomStateMapper(ModObjects.DOOR, new StateMap.Builder().ignore(BlockDoor.POWERED).build());
    }

    @Override
    public void postInit() {
        super.postInit();

        fontRenderer = new FontRendererSmall(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().renderEngine);
    }

    private void handleModel(Item item, int meta, String variant) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), variant));
    }
}
