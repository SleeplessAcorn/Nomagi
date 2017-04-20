package info.sleeplessacorn.nomagi.client;

import info.sleeplessacorn.nomagi.Nomagi;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class RenderHelper {

    public static final ResourceLocation GUI_CREATION = new ResourceLocation(Nomagi.MODID, "textures/gui/room_creation.png");
    public static final ResourceLocation GUI_PRIVACY = new ResourceLocation(Nomagi.MODID, "textures/gui/privacy.png");

    public static void bindTexture(ResourceLocation texture) {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
    }

    public static boolean isBetween(int mouseX, int mouseY, int xPos, int yPos, int width, int height) {
        return mouseX >= xPos && mouseX <= xPos + width - 1 && mouseY >= yPos && mouseY <= yPos + height - 1;
    }
}
