package info.sleeplessacorn.nomagi.client.gui;

import info.sleeplessacorn.nomagi.ModGuis;
import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.client.gui.Button.ButtonType;
import info.sleeplessacorn.nomagi.container.ContainerRoomWorktable;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;

public class GuiRoomWorktable extends GuiContainer {

    private final String btnKey = "button.nomagi.roomWorktable.";

    public GuiRoomWorktable(EntityPlayer player) {
        super(new ContainerRoomWorktable(player));
        xSize = 256;
        ySize = 220;
    }

    @Override
    public void initGui() {
        super.initGui();
        addButton(new Button(ButtonType.LONG, guiLeft + 184, guiTop + 8, btnKey + "test"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String label = "container." + Nomagi.ID + ".roomWorktable";
        fontRenderer.drawString(I18n.format(label), 8, 6, 0x404040);
        fontRenderer.drawString(I18n.format("container.inventory"), 48, ySize - 94, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f);
        mc.getTextureManager().bindTexture(ModGuis.ROOM_WORKTABLE.getTexture());
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

}
