package info.sleeplessacorn.nomagi.block.barrel;

import info.sleeplessacorn.nomagi.Nomagi;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GuiBarrel extends GuiContainer {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Nomagi.ID, "textures/gui/barrel.png");

    public GuiBarrel(TileEntity tile, EntityPlayer player) {
        super(new ContainerBarrel(tile, player));
    }

    @Override
    public void initGui() {
        super.initGui();
        xSize = 176;
        ySize = 166;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(I18n.format("tile." + Nomagi.ID + ".barrel"), 8, 6, 0x404040);
        fontRenderer.drawString(I18n.format("container.inventory"), 8, 71, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f);
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect((width - xSize) >> 1, (height - ySize) >> 1, 0, 0, xSize, ySize);
    }

}
