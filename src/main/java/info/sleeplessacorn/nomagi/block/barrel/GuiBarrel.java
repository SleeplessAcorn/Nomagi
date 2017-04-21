package info.sleeplessacorn.nomagi.block.barrel;

import info.sleeplessacorn.nomagi.Nomagi;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class GuiBarrel extends GuiContainer {


    private static final ResourceLocation CABINET_TEXTURE = new ResourceLocation(Nomagi.MODID, "textures/gui/barrel.png");


    private TileEntity barrel;
    private EntityPlayer accessor;

    public GuiBarrel(TileEntity tile, EntityPlayer player) {
        super(new ContainerBarrel(tile, player));
        barrel = tile;
        accessor = player;
        IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        int inventoryRows = inventory.getSlots() / 9;
        xSize = 176;
        ySize = 114 + inventoryRows * 18;

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRendererObj.drawString(I18n.format("tile." + Nomagi.MODID + ".barrel"), 8, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 1, 4210752);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f);
        mc.getTextureManager().bindTexture(CABINET_TEXTURE);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

    }

}
