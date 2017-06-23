package info.sleeplessacorn.nomagi.client.gui.button;

import info.sleeplessacorn.nomagi.client.RenderHelper;
import info.sleeplessacorn.nomagi.util.SubTexture;
import org.apache.commons.lang3.tuple.Pair;

public class GuiButtonNomagiSmall extends GuiButtonNomagi {

    public static final SubTexture BUTTON_DEFAULT = new SubTexture(RenderHelper.GUI_PRIVACY, 206, 0, 10, 10);
    public static final SubTexture BUTTON_HOVER = new SubTexture(RenderHelper.GUI_PRIVACY, 176, 0, 10, 10);
    public static final SubTexture BUTTON_PRESSED = new SubTexture(RenderHelper.GUI_PRIVACY, 186, 0, 10, 10);
    public static final SubTexture BUTTON_DISABLED = new SubTexture(RenderHelper.GUI_PRIVACY, 196, 0, 10, 10);

    public GuiButtonNomagiSmall(int buttonId, Pair<Integer, Integer> position) {
        super(buttonId, position, "");

        width = getDefault().getWidth();
        height = getDefault().getHeight();
    }

    @Override
    public SubTexture getDefault() {
        return BUTTON_DEFAULT;
    }

    @Override
    public SubTexture getHover() {
        return BUTTON_HOVER;
    }

    @Override
    public SubTexture getPressed() {
        return BUTTON_PRESSED;
    }

    @Override
    public SubTexture getDisabled() {
        return BUTTON_DISABLED;
    }
}
