package info.sleeplessacorn.nomagi.tile;

import info.sleeplessacorn.nomagi.block.BlockTent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import tehnut.lib.mc.tile.TileBase;
import tehnut.lib.mc.tile.sync.TileSyncBase;
import tehnut.lib.mc.tile.sync.adapters.NBT;
import tehnut.lib.mc.tile.sync.adapters.NBTAdapterUUID;

import java.util.UUID;

public class TileEntityTent extends TileSyncBase {

    static {
        // TODO - Figure out why LL didn't add this
        addAdapter(UUID.class, new NBTAdapterUUID());
    }

    @NBT
    private UUID ownerId = UUID.randomUUID();
    @NBT
    private EnumFacing facing = EnumFacing.NORTH;
    @NBT
    private BlockTent.TentType tentType = BlockTent.TentType.BASIC;

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
        markDirty();
    }

    public EnumFacing getFacing() {
        return facing;
    }

    public void setFacing(EnumFacing facing) {
        this.facing = facing;
        markDirty();
    }

    public BlockTent.TentType getTentType() {
        return tentType;
    }

    public void setTentType(BlockTent.TentType tentType) {
        this.tentType = tentType;
        markDirty();
    }
}
