package info.sleeplessacorn.nomagi.client.gui;

import info.sleeplessacorn.nomagi.Nomagi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.input.Mouse;

import java.util.Collections;
import java.util.List;

public class GuiButtonNomagi extends GuiButton {

    private final ResourceLocation texture = new ResourceLocation(Nomagi.ID, "textures/gui/buttons_all.png");
    private final ButtonType buttonType;

    private String tooltip;

    public GuiButtonNomagi(ButtonType buttonType, int posX, int posY, String label) {
        super(-1, posX, posY, 0, 0, label);
        this.buttonType = buttonType;
        this.width = buttonType.uvW;
        this.height = buttonType.uvH;
        this.tooltip = label + ".tooltip";
    }

    public GuiButtonNomagi(ButtonType buttonType, int posX, int posY) {
        super(-1, posX, posY, 0, 0, "");
        this.buttonType = buttonType;
        this.width = buttonType.uvW;
        this.height = buttonType.uvH;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (!visible)
            return;

        mc.getTextureManager().bindTexture(texture);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
        hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
        int newUvY = buttonType.uvY + (hovered ? (Mouse.isButtonDown(0) ? height * 2 : height) : 0);
        mc.ingameGUI.drawTexturedModalRect(x, y, buttonType.uvX, newUvY, width, height);

        if (displayString == null)
            return;

        String label = I18n.format(displayString);
        int labelX = x + (width / 2) - (mc.fontRenderer.getStringWidth(label) / 2);
        int labelY = y + (height / 2) - (mc.fontRenderer.FONT_HEIGHT / 2);
        int labelColor = hovered ? 0xffffa0 : 0xe0e0e0;
        mc.fontRenderer.drawStringWithShadow(label, labelX, labelY, labelColor);
    }

    protected void renderHoveredTooltip(Minecraft mc, int mouseX, int mouseY) {
        if (isMouseOver() && tooltip != null && I18n.hasKey(tooltip)) {
            ScaledResolution res = new ScaledResolution(mc);
            List<String> ls = Collections.singletonList(I18n.format(tooltip));
            GuiUtils.drawHoveringText(ItemStack.EMPTY, ls, mouseX, mouseY, res.getScaledWidth(), res.getScaledHeight(), -1, mc.fontRenderer);
        }
    }

    public GuiButtonNomagi setTooltip(String tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public enum ButtonType {
        SQUARE(0, 0, 16, 16),
        LONG(16, 0, 64, 16),
        PLUS(80, 0, 9, 9),
        MINUS(89, 0, 9, 9),
        IDEK(98, 0, 9, 9);

        private final int uvX, uvY, uvW, uvH;

        ButtonType(int uvX, int uvY, int uvW, int uvH) {
            this.uvX = uvX;
            this.uvY = uvY;
            this.uvW = uvW;
            this.uvH = uvH;
        }
    }
}
