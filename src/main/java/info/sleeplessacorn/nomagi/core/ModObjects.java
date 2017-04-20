package info.sleeplessacorn.nomagi.core;

import com.google.common.collect.Maps;
import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.block.BlockDecorative;
import info.sleeplessacorn.nomagi.block.BlockNomagiDoor;
import info.sleeplessacorn.nomagi.core.data.Room;
import info.sleeplessacorn.nomagi.item.ItemNomagiDoor;
import info.sleeplessacorn.nomagi.util.SubTexture;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tehnut.lib.mc.util.RegistryHelper;

import java.util.Map;

public class ModObjects {

    private static final ResourceLocation ROOM_PREVIEWS = new ResourceLocation(Nomagi.MODID, "textures/gui/room_previews.png");

    public static final Map<ResourceLocation, Room> ROOMS = Maps.newHashMap();

    public static final Room STARTER = new Room(new ResourceLocation(Nomagi.MODID, "starter"), new SubTexture(ROOM_PREVIEWS, 0, 0, 52, 52));
    public static final Room EMPTY_ROOM = new Room(new ResourceLocation(Nomagi.MODID, "empty_room"), new SubTexture(ROOM_PREVIEWS, 52, 0, 52, 52));
    public static final Room EMPTY_ROOM_BRICK = new Room(new ResourceLocation(Nomagi.MODID, "empty_room_brick"), new SubTexture(ROOM_PREVIEWS, 104, 0, 52, 52));

    public static final Block DECOR = new BlockDecorative();
    public static final Block DOOR = new BlockNomagiDoor();
    public static final Block DOOR_CONTROLLER = new Block(Material.WOOD) {
        @Override
        public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
            Nomagi.PROXY.displayRoomControllerGui();
            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY);
        }
    };

    public static void preInit() {
        RegistryHelper.register(DECOR, "decorative");
        RegistryHelper.register(DOOR, "door");
        RegistryHelper.register(new ItemNomagiDoor(DOOR), "door");
        RegistryHelper.register(DOOR_CONTROLLER, "door_controller");

        addRoom(STARTER);
        addRoom(EMPTY_ROOM);
        addRoom(EMPTY_ROOM_BRICK);
    }

    public static void addRoom(Room room) {
        ROOMS.put(room.getSchematic(), room);
    }
}
