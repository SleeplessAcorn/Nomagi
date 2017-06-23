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

    public static final SubTexture BUTTON_DEFAULT = new SubTexture(RenderHelper.GUI_CREATION, 176, 54, 48, 18);
    public static final SubTexture BUTTON_HOVER = new SubTexture(RenderHelper.GUI_CREATION, 176, 0, 48, 18);
    public static final SubTexture BUTTON_PRESSED = new SubTexture(RenderHelper.GUI_CREATION, 176, 18, 48, 18);
    public static final SubTexture BUTTON_DISABLED = new SubTexture(RenderHelper.GUI_CREATION, 176, 36, 48, 18);

    public GuiButtonNomagi(int buttonId, Pair<Integer, Integer> position, String buttonText) {
        super(buttonId, position.getLeft(), position.getRight(), BUTTON_DEFAULT.getWidth(), BUTTON_DEFAULT.getHeight(), buttonText);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (!visible)
            return;

        hovered = RenderHelper.isBetween(mouseX, mouseY, x, y, width, height);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        getDefault().draw(x, y, 0.0D);
        if (hovered) {
            getHover().draw(x, y, 0.0D);

            if (Mouse.isButtonDown(0))
                getPressed().draw(x, y, 0.0D);
        }

        if (!enabled)
            getDisabled().draw(x, y, 0.0D);

        drawCenteredString(mc.fontRenderer, TextFormatting.WHITE + I18n.format(displayString), x + width / 2, y + (height - 8) / 2, 0);
    }

    public SubTexture getDefault() {
        return BUTTON_DEFAULT;
    }

    public SubTexture getHover() {
        return BUTTON_HOVER;
    }

    public SubTexture getPressed() {
        return BUTTON_PRESSED;
    }

    public SubTexture getDisabled() {
        return BUTTON_DISABLED;
    }

    public GuiButtonNomagi offset(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }
}
