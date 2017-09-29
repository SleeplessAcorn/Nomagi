package info.sleeplessacorn.nomagi;

import com.google.common.collect.Range;
import info.sleeplessacorn.nomagi.client.gui.GuiPrivacyLectern;
import info.sleeplessacorn.nomagi.client.gui.GuiRoomWorktable;
import info.sleeplessacorn.nomagi.container.ContainerPrivacyLectern;
import info.sleeplessacorn.nomagi.container.ContainerRoomWorktable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@SuppressWarnings("NewExpressionSideOnly")
public enum ModGuis implements IGuiHandler {

    PRIVACY_LECTERN(new ResourceLocation(Nomagi.ID, "textures/gui/privacy_lectern.png")) {
        @Override
        public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            return new ContainerPrivacyLectern(player);
        }

        @Override
        public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            return new GuiPrivacyLectern(player, world);
        }
    },

    ROOM_WORKTABLE(new ResourceLocation(Nomagi.ID, "textures/gui/room_worktable.png")) {
        @Override
        public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            return new ContainerRoomWorktable(player);
        }

        @Override
        public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            return new GuiRoomWorktable(player);
        }
    };

    private final ResourceLocation texture;

    ModGuis(ResourceLocation texture) {
        this.texture = texture;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public int getID() {
        return ordinal();
    }

    private static final ModGuis[] VALUES = values();

    private static final Range<Integer> RANGE = Range.openClosed(0, VALUES.length);

    private static final IGuiHandler NO_OP = new IGuiHandler() {
        @Override
        public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            return null;
        }

        @Override
        public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            return null;
        }
    };

    private static IGuiHandler getHandler(int ID) {
        return RANGE.contains(ID) ? VALUES[ID] : NO_OP;
    }

    public static void register() {
        NetworkRegistry.INSTANCE.registerGuiHandler(Nomagi.ID, new IGuiHandler() {
            @Override
            public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                return getHandler(ID).getServerGuiElement(ID, player, world, x, y, z);
            }

            @Override
            public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                return getHandler(ID).getClientGuiElement(ID, player, world, x, y, z);
            }
        });
    }

}
