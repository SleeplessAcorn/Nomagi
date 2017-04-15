package info.sleeplessacorn.nomagi.core;

import com.google.common.collect.Maps;
import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.core.data.Room;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class ModObjects {

    public static final Map<ResourceLocation, Room> ROOMS = Maps.newHashMap();
    public static void addRoom(Room room) { ROOMS.put(room.getSchematic(), room); }
    public static final Room STARTER = new Room(new ResourceLocation(Nomagi.MODID, "starter"));

    public static void preInit() {
        addRoom(STARTER);
    }
}
