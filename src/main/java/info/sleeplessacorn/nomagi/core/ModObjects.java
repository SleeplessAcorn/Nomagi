package info.sleeplessacorn.nomagi.core;

import com.google.common.collect.Maps;
import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.block.*;
import info.sleeplessacorn.nomagi.block.barrel.BlockBarrel;
import info.sleeplessacorn.nomagi.block.barrel.TileBarrel;
import info.sleeplessacorn.nomagi.core.data.Room;
import info.sleeplessacorn.nomagi.item.ItemBlockTent;
import info.sleeplessacorn.nomagi.item.ItemNomagiDoor;
import info.sleeplessacorn.nomagi.util.SubTexture;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tehnut.lib.mc.util.RegistryHelper;

import java.util.Map;

public class ModObjects {

    private static final ResourceLocation ROOM_PREVIEWS = new ResourceLocation(Nomagi.MODID, "textures/gui/room_previews.png");

    public static final Map<ResourceLocation, Room> ROOMS = Maps.newHashMap();

    public static final Room EMPTY_ROOM = new Room(new ResourceLocation(Nomagi.MODID, "empty_room"), new SubTexture(ROOM_PREVIEWS, 0, 0, 52, 52));
    public static final Room NATURE_ROOM = new Room(new ResourceLocation(Nomagi.MODID, "nature_room"), new SubTexture(ROOM_PREVIEWS, 52, 0, 52, 52));
    public static final Room CELLAR_ROOM = new Room(new ResourceLocation(Nomagi.MODID, "cellar_room"), new SubTexture(ROOM_PREVIEWS, 104, 0, 52, 52));

    public static final Block TENT = new BlockTent();
    public static final ItemBlock TENT_ITEM = new ItemBlockTent();

    public static final Block DECOR = new BlockDecorative();
    public static final Block NATURE = new BlockNature();
    public static final Block BARREL = new BlockBarrel();
    public static final Block DOOR = new BlockNomagiDoor();
    public static final Block DOOR_CONTROLLER = new BlockController();

    public static void preInit() {
        RegistryHelper.register(TENT, "tent");
        RegistryHelper.register(TENT_ITEM, "tent");
        RegistryHelper.register(DECOR, "decorative");
        RegistryHelper.register(NATURE, "nature");
        RegistryHelper.register(BARREL, "barrel");
        GameRegistry.registerTileEntity(TileBarrel.class, BARREL.getRegistryName().toString());
        RegistryHelper.register(DOOR, "door");
        RegistryHelper.register(new ItemNomagiDoor(DOOR), "door");
        RegistryHelper.register(DOOR_CONTROLLER, "controller");

        addRoom(EMPTY_ROOM);
        addRoom(NATURE_ROOM);
        addRoom(CELLAR_ROOM);
    }

    public static void addRoom(Room room) {
        ROOMS.put(room.getSchematic(), room);
    }
}
