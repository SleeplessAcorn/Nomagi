package info.sleeplessacorn.nomagi.core;

import com.google.common.collect.Maps;
import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.block.BlockDecorative;
import info.sleeplessacorn.nomagi.block.BlockNomagiDoor;
import info.sleeplessacorn.nomagi.core.data.Room;
import info.sleeplessacorn.nomagi.item.ItemNomagiDoor;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import tehnut.lib.mc.util.RegistryHelper;

import java.util.Map;

public class ModObjects {

    public static final Map<ResourceLocation, Room> ROOMS = Maps.newHashMap();
    public static void addRoom(Room room) { ROOMS.put(room.getSchematic(), room); }
    public static final Room STARTER = new Room(new ResourceLocation(Nomagi.MODID, "starter"));

    public static final Block DECOR = new BlockDecorative();
    public static final Block DOOR = new BlockNomagiDoor();

    public static void preInit() {
        RegistryHelper.register(DECOR, "decorative");
        RegistryHelper.register(DOOR, "door");
        RegistryHelper.register(new ItemNomagiDoor(DOOR), "door");

        addRoom(STARTER);
    }
}
