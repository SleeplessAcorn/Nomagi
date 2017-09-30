package info.sleeplessacorn.nomagi.core.data;

import info.sleeplessacorn.nomagi.Nomagi;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.io.InputStream;

public final class RoomData {

    private final ResourceLocation schematic;
    private final Template template;
    private final String name;
    private final String description;
    private final String file;

    public RoomData(ResourceLocation schematic) {
        String domain = schematic.getResourceDomain();
        String path = schematic.getResourcePath();
        this.schematic = schematic;
        this.template = getTemplateFromSchematic(schematic);
        this.name = String.format("room.%s.%s.name", domain, path);
        this.description = String.format("room.%s.%s.desc", domain, path);
        this.file = String.format("/assets/%s/rooms/nbt/%s.nbt", domain, path);
    }

    public RoomData(String schematic) {
        this(new ResourceLocation(Nomagi.ID, schematic));
    }

    @SideOnly(Side.CLIENT)
    public String getName() {
        return I18n.format(name);
    }

    @SideOnly(Side.CLIENT)
    public String getDescription() {
        return I18n.format(description);
    }

    public ResourceLocation getSchematic() {
        return schematic;
    }

    public Template getTemplate() {
        return template;
    }

    private Template getTemplateFromSchematic(ResourceLocation schematic) {
        try {
            InputStream stream = Nomagi.class.getResourceAsStream(file);
            NBTTagCompound nbt = CompressedStreamTools.readCompressed(stream);
            Template template = new Template();
            template.read(nbt);
            return template;
        } catch (IOException ignored) {
            Nomagi.LOGGER.error("Failed to parse NBT file for room <{}>!", schematic.toString());
            return null;
        }
    }

}
