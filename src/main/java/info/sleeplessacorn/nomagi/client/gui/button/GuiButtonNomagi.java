package info.sleeplessacorn.nomagi.client.gui.button;

import info.sleeplessacorn.nomagi.client.RenderHelper;
import info.sleeplessacorn.nomagi.util.SubTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Mouse;

public class GuiButtonNomagi extends GuiButton {

    public static final SubTexture BUTTON_HOVER = new SubTexture(RenderHelper.GUI_CREATION, 176, 0, 48, 18);
    public static final SubTexture BUTTON_PRESSED = new SubTexture(RenderHelper.GUI_CREATION, 176, 18, 48, 18);
    public static final SubTexture BUTTON_DISABLED = new SubTexture(RenderHelper.GUI_CREATION, 176, 36, 48, 18);

    public GuiButtonNomagi(int buttonId, Pair<Integer, Integer> position, String buttonText) {
        super(buttonId, position.getLeft(), position.getRight(), BUTTON_HOVER.getWidth(), BUTTON_HOVER.getHeight(), buttonText);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (!visible)
            return;

        hovered = RenderHelper.isBetween(mouseX, mouseY, xPosition, yPosition, width, height);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if (hovered) {
            BUTTON_HOVER.draw(xPosition, yPosition, 0.0D);

            if (Mouse.isButtonDown(0))
                BUTTON_PRESSED.draw(xPosition, yPosition, 0.0D);
        }

        if (!enabled)
            BUTTON_DISABLED.draw(xPosition, yPosition, 0.0D);

        drawCenteredString(mc.fontRendererObj, TextFormatting.WHITE + I18n.format(displayString), xPosition + width / 2, yPosition + (height - 8) / 2, 0);
    }

    public GuiButtonNomagi offset(int x, int y) {
        xPosition += x;
        yPosition += y;
        return this;
    }
}
