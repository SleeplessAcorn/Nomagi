package info.sleeplessacorn.nomagi.util;

import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.core.ModObjects;
import info.sleeplessacorn.nomagi.core.data.Room;
import info.sleeplessacorn.nomagi.core.data.Tent;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

import javax.annotation.Nullable;
import java.io.InputStream;

public class GeneratorUtil {

    public static void generateInitialRoom(World world, int chunkX, int chunkZ) {
        //noinspection ConstantConditions - STARTER always exists.
        ModObjects.STARTER.getTemplate().addBlocksToWorld(world, new BlockPos(chunkX * 16, 100, chunkZ * 16), new PlacementSettings());
    }

    public static void generateRoom(World world, Tent tent, int roomPosX, int roomPosZ, Room room) {
        if (room.getTemplate() == null) {
            Nomagi.LOGGER.error("Error generating room {} as the schematic can't be found.", room.getSchematic());
            return;
        }
        room.getTemplate().addBlocksToWorld(world, new BlockPos((tent.getChunkX() + roomPosX) * 16, 100, (tent.getChunkZ() + roomPosZ) * 16), new PlacementSettings());
    }

    @Nullable
    public static Template readFromId(ResourceLocation id) {
        InputStream stream = Nomagi.class.getResourceAsStream("/assets/" + id.getResourceDomain() + "/rooms/" + id.getResourcePath() + ".nbt");
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
