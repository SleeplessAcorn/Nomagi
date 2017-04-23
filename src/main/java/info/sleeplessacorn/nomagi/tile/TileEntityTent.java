package info.sleeplessacorn.nomagi.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import tehnut.lib.mc.tile.TileBase;

import java.util.UUID;

public class TileEntityTent extends TileBase {

    private UUID ownerId = UUID.randomUUID();

    @Override
    public void deserialize(NBTTagCompound tagCompound) {
        ownerId = NBTUtil.getUUIDFromTag(tagCompound.getCompoundTag("ownerId"));
    }

    @Override
    public NBTTagCompound serialize(NBTTagCompound tagCompound) {
        tagCompound.setTag("ownerId", NBTUtil.createUUIDTag(ownerId));
        return super.serialize(tagCompound);
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
        markDirty();
    }
}
