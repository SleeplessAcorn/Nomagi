package info.sleeplessacorn.nomagi.client.render;

import info.sleeplessacorn.nomagi.tile.TileRoomWorktable;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class RenderWorktableSchematic extends TileEntitySpecialRenderer<TileRoomWorktable> {

    @Override
    public void render(TileRoomWorktable tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(tile, x, y, z, partialTicks, destroyStage, alpha);
        // FIXME bleh
    }

}
