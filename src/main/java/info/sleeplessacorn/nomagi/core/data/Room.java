package info.sleeplessacorn.nomagi.core.data;

import info.sleeplessacorn.nomagi.util.GeneratorUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.structure.template.Template;

public class Room {

    private transient final String name;
    private final ResourceLocation schematic;

    public Room(ResourceLocation schematic) {
        this.name = "tent." + schematic.toString() + ".name";
        this.schematic = schematic;
    }

    public String getName() {
        return name;
    }

    public ResourceLocation getSchematic() {
        return schematic;
    }

    public Template getTemplate() {
        return GeneratorUtil.readFromId(schematic);
    }
}
