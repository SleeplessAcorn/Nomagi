package info.sleeplessacorn.nomagi.data;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import info.sleeplessacorn.nomagi.ModConfig;
import info.sleeplessacorn.nomagi.ModObjects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class TentData implements INBTSerializable<NBTTagCompound> {

    private UUID owner;
    private ChunkPos origin;
    private Map<ChunkPos, RoomData> rooms;

    public TentData(UUID owner, ChunkPos origin) {
        this.owner = owner;
        this.origin = origin;
        this.rooms = Maps.newHashMap();
    }

    public UUID getOwner() {
        return owner;
    }

    public ChunkPos getOrigin() {
        return origin;
    }

    public Map<ChunkPos, RoomData> getRooms() {
        return rooms;
    }

    public Set<Chunk> getUsedChunks() {
        Set<Chunk> usedChunks = Sets.newHashSet();
        int range = ModConfig.tentRadius; // FIXME: This will be inaccurate when multi-chunk rooms are implemented
        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                if (rooms.containsKey(new ChunkPos(x, z))) {
                    usedChunks.add(getWorld().getChunkFromChunkCoords(origin.x + x, origin.z + z));
                }
            }
        }
        return usedChunks;
    }

    public Set<EntityPlayer> getPlayersInside() {
        return Sets.newHashSet(getWorld().getPlayers(EntityPlayer.class, entity -> getUsedChunks()
                .contains(entity.getEntityWorld().getChunkFromBlockCoords(entity.getPosition()))));
    }

    public World getWorld() {
        return DimensionManager.getWorld(ModObjects.TENT_DIMENSION.getId());
    }

    public boolean canExtendTo(ChunkPos pos) {
        int range = ModConfig.tentRadius;
        return (pos.x <= range) && (pos.x >= -range) && (pos.z <= range) && (pos.z >= -range);
    }

    public boolean canExtendTo(int x, int y) {
        return canExtendTo(new ChunkPos(x, y));
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagCompound rooms = new NBTTagCompound();
        nbt.setUniqueId("owner", this.owner);
        nbt.setInteger("chunkX", this.origin.x);
        nbt.setInteger("chunkZ", this.origin.z);
        this.rooms.forEach((pos, room) -> {
            String name = room.getSchematic().toString();
            rooms.setIntArray(name, new int[] { pos.x, pos.z });
        });
        nbt.setTag("rooms", rooms);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.owner = nbt.getUniqueId("owner");
        int x = nbt.getInteger("chunkX");
        int z = nbt.getInteger("chunkZ");
        this.origin = new ChunkPos(x, z);
        this.rooms.clear();
        NBTTagCompound rooms = (NBTTagCompound) nbt.getTag("rooms");
        rooms.getKeySet().forEach(room -> {
            int[] xz = rooms.getIntArray(room);
            this.rooms.putIfAbsent(new ChunkPos(xz[0], xz[1]), null);
            // FIXME: This should be a RoomData not null, I just need to implement it properly later
        });
    }

}
