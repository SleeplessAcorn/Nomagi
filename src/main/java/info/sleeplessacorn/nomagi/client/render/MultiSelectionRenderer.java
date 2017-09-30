package info.sleeplessacorn.nomagi.client.render;

import info.sleeplessacorn.nomagi.Nomagi;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.function.Function;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = Nomagi.ID, value = Side.CLIENT)
public class MultiSelectionRenderer {

    @SubscribeEvent
    protected static void onDrawBlockHighlight(DrawBlockHighlightEvent event) {
        BlockPos pos = event.getTarget().getBlockPos();
        World world = event.getPlayer().getEntityWorld();
        if (pos == null)
            return;

        IBlockState state = world.getBlockState(pos);
        IBlockState actualState = state.getActualState(world, pos);
        if (state.getBlock() instanceof IProvider) {
            ((IProvider) state.getBlock()).apply(actualState).stream()
                    .filter(aabb -> !aabb.equals(actualState.getSelectedBoundingBox(world, pos)))
                    .forEach(aabb -> renderSelectionBox(aabb, pos, event.getPlayer(), event.getPartialTicks()));
        }
    }

    private static void renderSelectionBox(AxisAlignedBB aabb, BlockPos pos, EntityPlayer plr, float pTicks) {
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
        );
        GlStateManager.glLineWidth(2.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        AxisAlignedBB target = getTarget(aabb, pos, plr, pTicks);
        RenderGlobal.drawSelectionBoundingBox(target, 0.0F, 0.0F, 0.0F, 0.4F);
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    private static AxisAlignedBB getTarget(AxisAlignedBB aabb, BlockPos pos, EntityPlayer plr, float pTicks) {
        double offsetX = plr.lastTickPosX + (plr.posX - plr.lastTickPosX) * pTicks;
        double offsetY = plr.lastTickPosY + (plr.posY - plr.lastTickPosY) * pTicks;
        double offsetZ = plr.lastTickPosZ + (plr.posZ - plr.lastTickPosZ) * pTicks;
        return aabb.grow(0.002D).offset(pos).offset(-offsetX, -offsetY, -offsetZ);
    }

    @FunctionalInterface
    public interface IProvider extends Function<IBlockState, List<AxisAlignedBB>> {

    }
}

