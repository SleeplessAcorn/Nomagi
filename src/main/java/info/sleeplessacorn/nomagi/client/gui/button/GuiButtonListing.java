package info.sleeplessacorn.nomagi.client.gui.button;

import info.sleeplessacorn.nomagi.client.RenderHelper;
import info.sleeplessacorn.nomagi.util.SubTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.apache.commons.lang3.tuple.Pair;

public class GuiButtonListing extends GuiButtonNomagiSmall {

    public static final SubTexture WHITELIST = new SubTexture(RenderHelper.GUI_PRIVACY, 177, 35, 16, 11);
    public static final SubTexture BLACKLIST = new SubTexture(RenderHelper.GUI_PRIVACY, 177, 35, 16, 11);

    private final boolean whitelist;

    public GuiButtonListing(int buttonId, Pair<Integer, Integer> position, boolean whitelist) {
        super(buttonId, position);

        this.whitelist = whitelist;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (!visible)
            return;

        hovered = RenderHelper.isBetween(mouseX, mouseY, x, y, width, height);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        getDefault().draw(x, y, 0.0D);
        if (hovered)
            drawRect(x - 1, y - 1, x + width - 1, y + height - 1, 0xF0CA94);
    }

    @Override
    public SubTexture getDefault() {
        return whitelist ? WHITELIST : BLACKLIST;
    }

    @Override
    public SubTexture getHover() {
        return whitelist ? WHITELIST : BLACKLIST;
    }

    @Override
    public SubTexture getPressed() {
        return whitelist ? WHITELIST : BLACKLIST;
    }

    @Override
    public SubTexture getDisabled() {
        return whitelist ? WHITELIST : BLACKLIST;
    }
}
