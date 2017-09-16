package info.sleeplessacorn.nomagi.util;

import info.sleeplessacorn.nomagi.client.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SubTexture {

    private final ResourceLocation texture;
    private final int xPos;
    private final int yPos;
    private final int width;
    private final int height;

    public SubTexture(ResourceLocation texture, int xPos, int yPos, int width, int height) {
        this.texture = texture;
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
    }

    @SideOnly(Side.CLIENT)
    public void draw(int drawX, int drawY, double zLevel) {
        RenderHelper.bindTexture(texture);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        float offset = 0.00390625F;
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(drawX, drawY + height, zLevel)
                .tex(xPos * offset, (yPos + height) * offset)
                .endVertex();
        vertexbuffer.pos(drawX + width, drawY + height, zLevel)
                .tex((xPos + width) * offset, (yPos + height) * offset)
                .endVertex();
        vertexbuffer.pos(drawX + width, drawY, zLevel)
                .tex((xPos + width) * offset, yPos * offset)
                .endVertex();
        vertexbuffer.pos(drawX, drawY, zLevel)
                .tex(xPos * offset, yPos * offset)
                .endVertex();
        tessellator.draw();
    }

    @SideOnly(Side.CLIENT)
    public void draw(int drawX, int drawY) {
        draw(drawX, drawY, 0.1D);
    }

    public ResourceLocation getTextureLocation() {
        return texture;
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
