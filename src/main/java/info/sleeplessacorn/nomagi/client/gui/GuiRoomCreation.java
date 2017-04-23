package info.sleeplessacorn.nomagi.client.gui;

import com.google.common.collect.Lists;
import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.client.RenderHelper;
import info.sleeplessacorn.nomagi.network.MessageCreateRoom;
import info.sleeplessacorn.nomagi.proxy.ProxyClient;
import info.sleeplessacorn.nomagi.util.SubTexture;
import info.sleeplessacorn.nomagi.client.gui.button.GuiButtonDirection;
import info.sleeplessacorn.nomagi.client.gui.button.GuiButtonNomagi;
import info.sleeplessacorn.nomagi.core.ModObjects;
import info.sleeplessacorn.nomagi.core.data.Room;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;

public class GuiRoomCreation extends GuiScreen {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Nomagi.MODID, "textures/gui/room_creation.png");

    public static final SubTexture BACKGROUND = new SubTexture(TEXTURE, 0, 0, 176, 166);

    public static final Pair<Integer, Integer> PREVIEW_LOCATION = Pair.of(8, 22);
    public static final Pair<Integer, Integer> BUTTON_LEFT_LOCATION = Pair.of(65, 57);
    public static final Pair<Integer, Integer> BUTTON_RIGHT_LOCATION = Pair.of(121, 57);
    public static final Pair<Integer, Integer> BUTTON_PREV_LOCATION = Pair.of(10, 62);
    public static final Pair<Integer, Integer> BUTTON_NEXT_LOCATION = Pair.of(48, 62);

    public static final List<ResourceLocation> ROOMS = Lists.newArrayList();

    public static int roomIndex;

    private final int currentChunkX;
    private final int currentChunkZ;
    private final EnumFacing direction;

    private int left;
    private int top;

    public GuiRoomCreation(int currentChunkX, int currentChunkZ, EnumFacing direction) {
        this.currentChunkX = currentChunkX;
        this.currentChunkZ = currentChunkZ;
        this.direction = direction;
    }

    @Override
    public void initGui() {
        super.initGui();

        left = (width - BACKGROUND.getWidth()) / 2;
        top = (height - BACKGROUND.getHeight()) / 2;

        addButton(new GuiButtonNomagi(0, BUTTON_LEFT_LOCATION, "gui.nomagi.create").offset(left, top));
        addButton(new GuiButtonNomagi(1, BUTTON_RIGHT_LOCATION, "gui.nomagi.reset").offset(left, top));
        addButton(new GuiButtonDirection(2, BUTTON_PREV_LOCATION, false).offset(left, top));
        addButton(new GuiButtonDirection(3, BUTTON_NEXT_LOCATION, true).offset(left, top));

        ROOMS.clear();
        ROOMS.addAll(ModObjects.ROOMS.keySet());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        BACKGROUND.draw(left, top, 0.0D);

        drawString(fontRenderer, TextFormatting.WHITE + "?", left + 7, top + 7, 0);

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (ROOMS.isEmpty())
            return;

        Room room = ModObjects.ROOMS.get(ROOMS.get(roomIndex));
        if (room == null)
            return;

        room.getPreviewImage().draw(left + PREVIEW_LOCATION.getLeft(), top + PREVIEW_LOCATION.getRight());

        List<String> cutString = ProxyClient.fontRenderer.listFormattedStringToWidth(I18n.format(room.getDescription()), 100);
        int offsetY = 0;
        for (String string : cutString) {
            ProxyClient.fontRenderer.drawString(TextFormatting.WHITE + string, left + 68, top + 10 + offsetY, 0); // TODO - Figure out why this is always black
            offsetY += 9;
        }

        if (RenderHelper.isBetween(mouseX, mouseY, left + 7, top + 7, fontRenderer.getStringWidth("?"), fontRenderer.FONT_HEIGHT))
            GuiUtils.drawHoveringText(Lists.newArrayList(I18n.format(room.getName())), mouseX, mouseY, width, height, 300, fontRenderer);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                Nomagi.NETWORK_WRAPPER.sendToServer(new MessageCreateRoom(ModObjects.ROOMS.get(ROOMS.get(roomIndex)), currentChunkX, currentChunkZ, direction));
                break;
            }
            case 1: {
                Nomagi.NETWORK_WRAPPER.sendToServer(new MessageCreateRoom(currentChunkX, currentChunkZ, direction));
                break;
            }
            case 2: {
                prevRoom();
                break;
            }
            case 3: {
                nextRoom();
                break;
            }
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        int mouseWheel = Mouse.getEventDWheel();
        if (mouseWheel < 0)
            nextRoom();
        else if (mouseWheel > 0)
            prevRoom();

        super.handleMouseInput();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void nextRoom() {
        roomIndex++;
        if (roomIndex > ROOMS.size() - 1)
            roomIndex = 0;
    }

    public void prevRoom() {
        roomIndex--;
        if (roomIndex < 0)
            roomIndex = ROOMS.size() - 1;
    }
}
