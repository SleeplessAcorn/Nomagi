package info.sleeplessacorn.nomagi.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class TilePrivacyLectern extends TileEntity implements ITickable {

    private static final Random FLIP_RAND = new Random();

    public int tickCount;
    public float flipT;
    public float flipA;
    public float pageFlip;
    public float pageFlipPrev;

    public TilePrivacyLectern() {
        // TODO Handle privacy data syncing
    }

    public void update() {
        pageFlipPrev = pageFlip;
        if (FLIP_RAND.nextInt(40) == 0) {
            float prevFlip = flipT;
            while (true) {
                flipT += (float) (FLIP_RAND.nextInt(4) - FLIP_RAND.nextInt(4));
                if (prevFlip != flipT) break;
            }
        }
        ++tickCount;
        float flipInterpolation = (flipT - pageFlip) * 0.4F;
        flipInterpolation = MathHelper.clamp(flipInterpolation, -0.2F, 0.2F);
        flipA += (flipInterpolation - flipA) * 0.9F;
        pageFlip += flipA;
    }

}
