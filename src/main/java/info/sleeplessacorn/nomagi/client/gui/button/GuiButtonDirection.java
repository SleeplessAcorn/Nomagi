package info.sleeplessacorn.nomagi.client.gui.button;

import info.sleeplessacorn.nomagi.client.RenderHelper;
import info.sleeplessacorn.nomagi.util.SubTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Mouse;

public class GuiButtonDirection extends GuiButton {

    public static final SubTexture BUTTON_NEXT_HOVER = new SubTexture(RenderHelper.GUI_CREATION, 176, 54, 10, 10);
    public static final SubTexture BUTTON_NEXT_PRESSED = new SubTexture(RenderHelper.GUI_CREATION, 186, 54, 10, 10);
    public static final SubTexture BUTTON_NEXT_DISABLED = new SubTexture(RenderHelper.GUI_CREATION, 196, 54, 10, 10);

    public static final SubTexture BUTTON_PREV_HOVER = new SubTexture(RenderHelper.GUI_CREATION, 176, 64, 10, 10);
    public static final SubTexture BUTTON_PREV_PRESSED = new SubTexture(RenderHelper.GUI_CREATION, 186, 64, 10, 10);
    public static final SubTexture BUTTON_PREV_DISABLED = new SubTexture(RenderHelper.GUI_CREATION, 196, 64, 10, 10);
    
    private boolean right;

    public GuiButtonDirection(int buttonId, Pair<Integer, Integer> position, boolean right) {
        super(buttonId, position.getLeft(), position.getRight(), BUTTON_NEXT_HOVER.getWidth(), BUTTON_NEXT_HOVER.getHeight(), "");

        this.right = right;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (!visible)
            return;

        SubTexture hover = right ? BUTTON_NEXT_HOVER : BUTTON_PREV_HOVER;
        SubTexture pressed = right ? BUTTON_NEXT_PRESSED : BUTTON_PREV_PRESSED;
        SubTexture disabled = right ? BUTTON_NEXT_DISABLED : BUTTON_PREV_DISABLED;

        hovered = RenderHelper.isBetween(mouseX, mouseY, xPosition, yPosition, width, height);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if (right)
            GlStateManager.rotate(0.0F, 0.0F, 0.5F, 0.0F);
        if (hovered) {
            hover.draw(xPosition, yPosition, 0.0D);

            if (Mouse.isButtonDown(0))
                pressed.draw(xPosition, yPosition, 0.0D);
        }

        if (!enabled)
            disabled.draw(xPosition, yPosition, 0.0D);
    }

    public GuiButtonDirection offset(int x, int y) {
        xPosition += x;
        yPosition += y;
        return this;
    }
}
