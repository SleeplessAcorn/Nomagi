package info.sleeplessacorn.nomagi.client.gui;

import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.client.GuiHandler;
import info.sleeplessacorn.nomagi.client.gui.GuiButtonNomagi.ButtonType;
import info.sleeplessacorn.nomagi.container.ContainerPrivacyLectern;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.TreeMap;
import java.util.UUID;

public class GuiPrivacyLectern extends GuiContainer {

    private final String unlocalizedName = "container." + Nomagi.ID + ".privacyLectern";
    private final TreeMap<UUID, String> playerCache = new TreeMap<>();

    private World world;

    public GuiPrivacyLectern(EntityPlayer player, World world) {
        super(new ContainerPrivacyLectern(player));
        this.world = world;
        xSize = 176;
        ySize = 200;
    }

    @Override
    public void initGui() {
        super.initGui();
        reloadPlayerMap();
        String loc = "button.nomagi.privacyLectern.addPlayer.tooltip";
        for (int i = 0; i < playerCache.values().size(); i++) {
            int padding = fontRenderer.FONT_HEIGHT * i + 4 * i;
            addButton(new GuiButtonNomagi(ButtonType.PLUS, guiLeft + 71, guiTop + 22 + padding).setTooltip(loc));
            // FIXME Use a fucking GuiScrollableList
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        super.renderHoveredToolTip(mouseX, mouseY);
        buttonList.stream().filter(GuiButtonNomagi.class::isInstance).forEach(btn -> ((GuiButtonNomagi) btn).renderHoveredTooltip(mc, mouseX, mouseY));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(I18n.format(unlocalizedName), 8, 6, 0x404040);
        fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 94, 0x404040);
        for (int i = 0; i < playerCache.values().size(); i++) {
            String name = playerCache.values().toArray(new String[0])[i];
            int padding = fontRenderer.FONT_HEIGHT * i + 4 * i;
            fontRenderer.drawStringWithShadow(name, 13, 22 + padding, -1);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f);
        mc.getTextureManager().bindTexture(GuiHandler.PRIVACY_LECTERN.getTexture());
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    private void reloadPlayerMap() {
        playerCache.clear(); // Invalidate existing cache
        if (world == null || world.playerEntities == null)
            Nomagi.LOGGER.warn("Failed to collect player names! Entity list doesn't exist!");
        else
            world.playerEntities.forEach(player -> playerCache.putIfAbsent(player.getUniqueID(), player.getDisplayNameString()));
    }
}
