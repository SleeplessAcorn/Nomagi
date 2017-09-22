package info.sleeplessacorn.nomagi.world;

import info.sleeplessacorn.nomagi.Nomagi;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;

public class TeleporterTent extends Teleporter {

    private final WorldServer worldServer;
    private double x;
    private double y;
    private double z;

    public TeleporterTent(WorldServer world, double x, double y, double z) {
        super(world);
        this.worldServer = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void placeInPortal(@Nonnull Entity entity, float rotationYaw) {
        worldServer.getBlockState(new BlockPos((int) x, (int) y, (int) z));
        entity.setPosition(x, y, z);
        entity.motionX = 0.0f;
        entity.motionY = 0.0f;
        entity.motionZ = 0.0f;
    }

    public static boolean teleportToDimension(EntityPlayer player, int dimension, BlockPos pos) {
        return teleportToDimension(player, Pair.of(dimension, pos));
    }

    public static boolean teleportToDimension(EntityPlayer player, Pair<Integer, BlockPos> dimPos) {
        return dimPos != null && teleportToDimension(player, dimPos.getKey(), dimPos.getValue().getX() + 0.5, dimPos.getValue().getY(), dimPos.getValue().getZ() + 0.5);
    }

    public static boolean teleportToDimension(EntityPlayer player, int dimension, double x, double y, double z) {
        int oldDimension = player.getEntityWorld().provider.getDimension();
        MinecraftServer server = player.getEntityWorld().getMinecraftServer();

        if (server == null || server.getWorld(dimension).getMinecraftServer() == null) {
            Nomagi.LOGGER.error("Dimension {} doesn't exist!", dimension);
            return false;
        }

        WorldServer worldServer = server.getWorld(dimension);
        player.addExperienceLevel(0);

        //noinspection ConstantConditions - null checked above
        worldServer.getMinecraftServer().getPlayerList().transferPlayerToDimension((EntityPlayerMP) player, dimension, new TeleporterTent(worldServer, x, y, z));
        player.setPositionAndUpdate(x, y, z);

        if (oldDimension == 1) {
            // For some reason teleporting out of the end does weird things.
            player.setPositionAndUpdate(x, y, z);
            worldServer.spawnEntity(player);
            worldServer.updateEntityWithOptionalForce(player, true);
        }

        player.setSneaking(false);
        return true;
    }
}