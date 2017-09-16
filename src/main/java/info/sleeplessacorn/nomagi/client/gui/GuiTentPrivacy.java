package info.sleeplessacorn.nomagi.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import info.sleeplessacorn.nomagi.client.RenderHelper;
import info.sleeplessacorn.nomagi.client.gui.button.GuiButtonDirection;
import info.sleeplessacorn.nomagi.client.gui.button.GuiButtonListing;
import info.sleeplessacorn.nomagi.client.gui.button.GuiButtonNomagiSmall;
import info.sleeplessacorn.nomagi.core.data.Privacy;
import info.sleeplessacorn.nomagi.util.SubTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Mouse;

import javax.annotation.Nullable;
import java.awt.Color;
import java.io.IOException;
import java.util.*;

public class GuiTentPrivacy extends GuiScreen {

    public static final SubTexture BACKGROUND = new SubTexture(RenderHelper.GUI_PRIVACY, 0, 0, 176, 166);

    // TODO - Rename these
    // Left
    public static final Pair<Integer, Integer> BUTTON_LEFT_TOP_LOCATION = Pair.of(11, 21);
    public static final Pair<Integer, Integer> BUTTON_LEFT_MID_LOCATION = Pair.of(11, 35);
    public static final Pair<Integer, Integer> BUTTON_LEFT_BOT_LOCATION = Pair.of(11, 49);
    public static final Pair<Integer, Integer> BUTTON_ONLINE_LEFT_LOCATION = Pair.of(7, 148);
    public static final Pair<Integer, Integer> BUTTON_ONLINE_RIGHT_LOCATION = Pair.of(76, 148);
    public static final Pair<Integer, Integer> BUTTON_WHITELIST_LOCATION = Pair.of(25, 148);
    public static final Pair<Integer, Integer> BUTTON_BLACKLIST_LOCATION = Pair.of(52, 148);
    // Right
    public static final Pair<Integer, Integer> BUTTON_RIGHT_TOP_LOCATION = Pair.of(92, 21);
    public static final Pair<Integer, Integer> BUTTON_RIGHT_MID_LOCATION = Pair.of(92, 35);
    public static final Pair<Integer, Integer> BUTTON_RIGHT_BOT_LOCATION = Pair.of(92, 49);
    public static final Pair<Integer, Integer> BUTTON_LISTED_LEFT_LOCATION = Pair.of(90, 148);
    public static final Pair<Integer, Integer> BUTTON_LISTED_RIGHT_LOCATION = Pair.of(159, 148);

    public static Map<UUID, String> usernameCache = Maps.newHashMap();

    private final Privacy privacy;

    private int left;
    private int top;
    private List<List<ListElement>> online = Lists.newArrayList();
    private List<List<ListElement>> listed = Lists.newArrayList();
    private int onlinePageIndex;
    private int listedPageIndex;
    private ListElement selected;

    public GuiTentPrivacy(Privacy privacy, List<UUID> onlinePlayers) {
        this.privacy = privacy;

        int index = 0;
        List<ListElement> elements = Lists.newArrayList();
        Set<UUID> added = Sets.newHashSet();

        // Populate listed
        for (UUID uuid : privacy.getList(Privacy.ListType.WHITELIST)) {
            elements.add(new ListElement(usernameCache.getOrDefault(uuid, uuid.toString()), index, true));
            added.add(uuid);
            index++;
        }
        for (UUID uuid : privacy.getList(Privacy.ListType.WHITELIST)) {
            elements.add(new ListElement(usernameCache.getOrDefault(uuid, uuid.toString()), index, false));
            added.add(uuid);
            index++;
        }

        if (elements.size() == 0)
            for (int i = 0; i < 27; i++)
                elements.add(new ListElement(UUID.randomUUID().toString(), i, new Random().nextBoolean()));

        listed = Lists.partition(elements, 5);

        // Populate online
        index = 0;
        elements = Lists.newArrayList();
        for (UUID uuid : onlinePlayers) {
            if (added.contains(uuid) || Minecraft.getMinecraft().player.getGameProfile().getId().equals(uuid)) // Ignore self and listed players
                continue;
            elements.add(new ListElement(usernameCache.getOrDefault(uuid, uuid.toString()), index, null));
            index++;
        }

        online = Lists.partition(elements, 5);
    }

    @Override
    public void initGui() {
        super.initGui();

        left = (width - BACKGROUND.getWidth()) / 2;
        top = (height - BACKGROUND.getHeight()) / 2;

        addButton(new GuiButtonNomagiSmall(0, BUTTON_LEFT_TOP_LOCATION).offset(left, top));
        addButton(new GuiButtonNomagiSmall(1, BUTTON_LEFT_MID_LOCATION).offset(left, top));
        addButton(new GuiButtonNomagiSmall(2, BUTTON_LEFT_BOT_LOCATION).offset(left, top));

        addButton(new GuiButtonNomagiSmall(3, BUTTON_RIGHT_TOP_LOCATION).offset(left, top));
        addButton(new GuiButtonNomagiSmall(4, BUTTON_RIGHT_MID_LOCATION).offset(left, top));
        addButton(new GuiButtonNomagiSmall(5, BUTTON_RIGHT_BOT_LOCATION).offset(left, top));

        addButton(new GuiButtonDirection(6, BUTTON_ONLINE_LEFT_LOCATION, false).offset(left, top));
        addButton(new GuiButtonDirection(7, BUTTON_ONLINE_RIGHT_LOCATION, true).offset(left, top));

        addButton(new GuiButtonDirection(8, BUTTON_LISTED_LEFT_LOCATION, false).offset(left, top));
        addButton(new GuiButtonDirection(9, BUTTON_LISTED_RIGHT_LOCATION, true).offset(left, top));

        addButton(new GuiButtonListing(10, BUTTON_WHITELIST_LOCATION, true).offset(left, top));
        addButton(new GuiButtonListing(11, BUTTON_BLACKLIST_LOCATION, false).offset(left, top));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        BACKGROUND.draw(left, top, 0.0D);

        // Draw listed entries
        int elementXPos = left + 8;
        int elementYPos = top + 84;

        if (online.size() != 0) {
            onlinePageIndex = MathHelper.clamp(onlinePageIndex, 0, online.size() - 1);
            for (ListElement listElement : online.get(onlinePageIndex)) {
                Color color = new Color(0xD7D7D7);
                if (listElement.getDisplayId() % 2 == 0)
                    color = new Color(0x828282);
                drawRect(elementXPos, elementYPos, elementXPos + listElement.getWidth(), elementYPos + listElement.getHeight(), color.getRGB());

                if (Mouse.isButtonDown(1) && isMouseOver(mouseX, mouseY, elementXPos, elementYPos, listElement.getWidth(), listElement.getHeight())) {
                    if (selected != null)
                        selected.setSelected(false);

                    listElement.setSelected(true);
                    selected = listElement;
                }

                String display = listElement.getDisplay();
                if (display.length() > 11)
                    display = display.substring(0, 11) + "...";

                fontRenderer.drawStringWithShadow((listElement.isSelected() ? TextFormatting.GOLD : TextFormatting.WHITE) + display, elementXPos + 1, elementYPos + 2, 0);

                elementYPos += listElement.getHeight();
            }
        }

        elementXPos = left + 91;
        elementYPos = top + 84;

        if (listed.size() != 0) {
            listedPageIndex = MathHelper.clamp(listedPageIndex, 0, listed.size() - 1);
            for (ListElement listElement : listed.get(listedPageIndex)) {
                Color color = new Color(0xD7D7D7);
                if (listElement.getDisplayId() % 2 == 0)
                    color = new Color(0x828282);
                drawRect(elementXPos, elementYPos, elementXPos + listElement.getWidth(), elementYPos + listElement.getHeight(), color.getRGB());

                if (Mouse.isButtonDown(0) && isMouseOver(mouseX, mouseY, elementXPos, elementYPos, listElement.getWidth(), listElement.getHeight())) {
                    if (selected != null)
                        selected.setSelected(false);

                    listElement.setSelected(true);
                    selected = listElement;
                }

                String display = listElement.getDisplay();
                if (display.length() > 8)
                    display = display.substring(0, 8) + "...";
                display += " (" + (listElement.isWhitelist() ? "W" : "B") + ")"; // TODO - Switch to icons
                if (listElement.isSelected())
                    display = TextFormatting.GOLD + display;
                else
                    display = TextFormatting.WHITE + display;

                fontRenderer.drawStringWithShadow(display, elementXPos + 1, elementYPos + 2, 0);

                elementYPos += listElement.getHeight();
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode))
            mc.displayGuiScreen(null);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 6: {
                onlinePageIndex--;
                break;
            }
            case 7: {
                onlinePageIndex++;
                break;
            }
            case 8: {
                listedPageIndex--;
                break;
            }
            case 9: {
                listedPageIndex++;
                break;
            }
            default: {
                break;
            }
        }
    }

    public static boolean isMouseOver(int mouseX, int mouseY, int xPos, int yPos, int width, int height) {
        return mouseX >= xPos && mouseY >= yPos && mouseX < xPos + width && mouseY < yPos + height;
    }

    public static class ListElement {

        private final String display;
        private final int displayId;
        @Nullable
        private Boolean whitelist;
        private boolean selected;
        private int height = 12;
        private int width = 77;

        public ListElement(String display, int displayId, @Nullable Boolean whitelist) {
            this.display = display;
            this.displayId = displayId;
            this.whitelist = whitelist;
        }

        public String getDisplay() {
            return display;
        }

        public int getDisplayId() {
            return displayId;
        }

        @Nullable
        public Boolean isWhitelist() {
            return whitelist;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }
    }

}
