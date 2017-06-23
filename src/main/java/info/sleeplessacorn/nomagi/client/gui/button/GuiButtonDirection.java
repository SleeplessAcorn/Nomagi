package info.sleeplessacorn.nomagi.client.gui.button;

import info.sleeplessacorn.nomagi.client.RenderHelper;
import info.sleeplessacorn.nomagi.util.SubTexture;
import org.apache.commons.lang3.tuple.Pair;

public class GuiButtonDirection extends GuiButtonNomagiSmall {

    public static final SubTexture BUTTON_NEXT_DEFAULT = new SubTexture(RenderHelper.GUI_CREATION, 206, 54, 10, 10);
    public static final SubTexture BUTTON_NEXT_HOVER = new SubTexture(RenderHelper.GUI_CREATION, 176, 54, 10, 10);
    public static final SubTexture BUTTON_NEXT_PRESSED = new SubTexture(RenderHelper.GUI_CREATION, 186, 54, 10, 10);
    public static final SubTexture BUTTON_NEXT_DISABLED = new SubTexture(RenderHelper.GUI_CREATION, 196, 54, 10, 10);

    public static final SubTexture BUTTON_PREV_DEFAULT = new SubTexture(RenderHelper.GUI_CREATION, 206, 64, 10, 10);
    public static final SubTexture BUTTON_PREV_HOVER = new SubTexture(RenderHelper.GUI_CREATION, 176, 64, 10, 10);
    public static final SubTexture BUTTON_PREV_PRESSED = new SubTexture(RenderHelper.GUI_CREATION, 186, 64, 10, 10);
    public static final SubTexture BUTTON_PREV_DISABLED = new SubTexture(RenderHelper.GUI_CREATION, 196, 64, 10, 10);

    private boolean right;

    public GuiButtonDirection(int buttonId, Pair<Integer, Integer> position, boolean right) {
        super(buttonId, position);

        this.right = right;
    }

    @Override
    public SubTexture getDefault() {
        return right ? BUTTON_NEXT_DEFAULT : BUTTON_PREV_DEFAULT;
    }

    @Override
    public SubTexture getHover() {
        return right ? BUTTON_NEXT_HOVER : BUTTON_PREV_HOVER;
    }

    @Override
    public SubTexture getPressed() {
        return right ? BUTTON_NEXT_PRESSED : BUTTON_PREV_PRESSED;
    }

    @Override
    public SubTexture getDisabled() {
        return right ? BUTTON_NEXT_DISABLED : BUTTON_PREV_DISABLED;
    }
}
