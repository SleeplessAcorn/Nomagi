package info.sleeplessacorn.nomagi.util;

import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.core.ModObjects;
import info.sleeplessacorn.nomagi.core.data.Room;
import info.sleeplessacorn.nomagi.core.data.Tent;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

import javax.annotation.Nullable;
import java.io.InputStream;

public class GeneratorUtil {

    public static void generateInitialRoom(World world, int chunkX, int chunkZ) {
        BlockPos pos = new BlockPos(chunkX * 16, Tent.BASE_HEIGHT, chunkZ * 16);
        //noinspection ConstantConditions - STARTER always exists.
        ModObjects.EMPTY_ROOM.getTemplate().addBlocksToWorld(world, pos, new PlacementSettings());
    }

    /**
     * @param world    - Current world
     * @param tent     - Tent to add room to
     * @param roomPosX - Current room's internal grid x co-ordinates
     * @param roomPosZ - Current room's internal grid z co-ordinates
     * @param room     - Room to generate
     * @param facing   - Side of current room to generate on
     */
    public static void generateRoom(World world, Tent tent, int roomPosX, int roomPosZ, Room room, EnumFacing facing) {
        if (room.getTemplate() == null) {
            Nomagi.LOGGER.error("Error generating room {} as the schematic can't be found.", room.getSchematic());
            return;
        }

        int chunkX = tent.getChunkX() + roomPosX;
        int chunkZ = tent.getChunkZ() + roomPosZ;

        switch (facing) {
            case DOWN:
                return;
            case UP:
                return;
            case NORTH: {
                chunkZ -= 1;
                break;
            }
            case EAST: {
                chunkX += 1;
                break;
            }
            case SOUTH: {
                chunkZ += 1;
                break;
            }
            case WEST: {
                chunkX -= 1;
                break;
            }
        }

        int internalX = chunkX - tent.getChunkX();
        int internalZ = chunkZ - tent.getChunkZ();
        if (!tent.canExtendTo(internalX, internalZ))
            return;

        BlockPos pos = new ChunkPos(chunkX, chunkZ).getBlock(0, Tent.BASE_HEIGHT, 0);
        room.getTemplate().addBlocksToWorld(world, pos, new PlacementSettings());
        tent.setRoom(room, internalX, internalZ);
    }

    @Nullable
    public static Template readFromId(ResourceLocation id) {
        String domain = id.getResourceDomain();
        String path = id.getResourcePath();
        InputStream stream = Nomagi.class.getResourceAsStream("/assets/" + domain + "/rooms/" + path + ".nbt");
        return readFromStream(stream);
    }

    @Nullable
    public static Template readFromStream(InputStream stream) {
        try {
            NBTTagCompound templateData = CompressedStreamTools.readCompressed(stream);
            Template template = new Template();
            template.read(templateData);
            return template;
        } catch (Exception e) {
            return null;
        }
    }
}
