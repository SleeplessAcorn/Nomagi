package info.sleeplessacorn.nomagi.client.render;

import info.sleeplessacorn.nomagi.common.block.base.BlockCardinalBase;
import info.sleeplessacorn.nomagi.common.tile.TilePrivacyLectern;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderLecternBook extends TileEntitySpecialRenderer<TilePrivacyLectern> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("nomagi:textures/entity/lectern_book.png");

    private final ModelBook model = new ModelBook();

    public void render(
            TilePrivacyLectern tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        float tickInterpolation = MathHelper.sin((tile.tickCount + partialTicks) * 0.1F);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5F, y + 0.62F, z + 0.5F);
        GlStateManager.translate(0.0F, 0.1F + tickInterpolation * 0.01F, 0.0F);

        IBlockState state = tile.getWorld().getBlockState(tile.getPos());
        EnumFacing facing = state.getValue(BlockCardinalBase.getFacingProperty());
        float angleOffset = facing.getAxis() == EnumFacing.Axis.X ? 90F : -90F; // EnumFacing hates me.

        GlStateManager.rotate(facing.getHorizontalAngle() + angleOffset, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(67.5F, 0.0F, 0.0F, 1.0F);

        float flipInterpolation = (tile.pageFlip - tile.pageFlipPrev) * partialTicks;
        float flip0 = tile.pageFlipPrev + flipInterpolation + 0.25F;
        float flip1 = tile.pageFlipPrev + flipInterpolation + 0.75F;
        flip0 = MathHelper.clamp((flip0 - MathHelper.fastFloor(flip0)) * 1.6F - 0.3F, 0.0F, 1.0F);
        flip1 = MathHelper.clamp((flip1 - MathHelper.fastFloor(flip1)) * 1.6F - 0.3F, 0.0F, 1.0F);

        bindTexture(TEXTURE);
        GlStateManager.enableCull();
        model.render(null, tile.tickCount + partialTicks, flip0, flip1, 1.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
    }

}
