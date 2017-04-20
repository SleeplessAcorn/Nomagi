package info.sleeplessacorn.nomagi.util;

import info.sleeplessacorn.nomagi.client.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SubTexture {

    private final ResourceLocation textureLocation;
    private final int xPos;
    private final int yPos;
    private final int width;
    private final int height;

    public SubTexture(ResourceLocation textureLocation, int xPos, int yPos, int width, int height) {
        this.textureLocation = textureLocation;
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
    }

    @SideOnly(Side.CLIENT)
    public void draw(int drawX, int drawY, double zLevel) {
        RenderHelper.bindTexture(textureLocation);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos((double)(drawX), (double)(drawY + height), zLevel).tex((double)((float)(xPos) * 0.00390625F), (double)((float)(yPos + height) * 0.00390625F)).endVertex();
        vertexbuffer.pos((double)(drawX + width), (double)(drawY + height), zLevel).tex((double)((float)(xPos + width) * 0.00390625F), (double)((float)(yPos + height) * 0.00390625F)).endVertex();
        vertexbuffer.pos((double)(drawX + width), (double)(drawY), zLevel).tex((double)((float)(xPos + width) * 0.00390625F), (double)((float)(yPos) * 0.00390625F)).endVertex();
        vertexbuffer.pos((double)(drawX), (double)(drawY), zLevel).tex((double)((float)(xPos) * 0.00390625F), (double)((float)(yPos) * 0.00390625F)).endVertex();
        tessellator.draw();
    }

    @SideOnly(Side.CLIENT)
    public void draw(int drawX, int drawY) {
        draw(drawX, drawY, 0.1D);
    }

    public ResourceLocation getTextureLocation() {
        return textureLocation;
    }

    public int getDrawX() {
        return xPos;
    }

    public int getDrawY() {
        return yPos;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
