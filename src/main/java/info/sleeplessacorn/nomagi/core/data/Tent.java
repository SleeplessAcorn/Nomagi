package info.sleeplessacorn.nomagi.core.data;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import info.sleeplessacorn.nomagi.ConfigHandler;
import info.sleeplessacorn.nomagi.core.ModObjects;
import info.sleeplessacorn.nomagi.util.GeneratorUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Tent implements INBTSerializable<NBTTagCompound> {

    public static final int BASE_HEIGHT = 50;
    public static final Pair<Integer, Integer> ORIGIN = Pair.of(0, 0);

    private final Map<Pair<Integer, Integer>, Room> rooms = Maps.newHashMap();
    private UUID ownerId;
    private int chunkX;
    private int chunkZ;
    private Privacy privacy;

    public Tent(UUID ownerId, int chunkX, int chunkZ, Privacy privacy) {
        this.ownerId = ownerId;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.privacy = privacy;

        rooms.put(ORIGIN, ModObjects.EMPTY_ROOM);
    }

    public void initialize(EntityPlayer player) {
        GeneratorUtil.generateInitialRoom(player.getEntityWorld(), chunkX, chunkZ);
        TentWorldSavedData.getData(player.getEntityWorld()).setTent(player, this);
        getWorld().getChunkFromChunkCoords(chunkX, chunkZ).checkLight();
    }

    public boolean isInside(Entity entity) {
        return getUsedChunks().contains(entity.getEntityWorld().getChunkFromBlockCoords(entity.getPosition()));
    }

    @Nonnull
    public Set<Chunk> getUsedChunks() {
        Set<Chunk> tentChunks = Sets.newHashSet();
        for (int x = -ConfigHandler.tentRadius; x <= ConfigHandler.tentRadius; x++) {
            for (int z = -ConfigHandler.tentRadius; z <= ConfigHandler.tentRadius; z++) {
                if (rooms.containsKey(Pair.of(x, z))) {
                    tentChunks.add(getWorld().getChunkFromChunkCoords(chunkX + x, chunkZ + z));
                }
            }
        }
        return tentChunks;
    }

    @Nonnull
    public Set<EntityPlayer> getPlayersInside() {
        return Sets.newHashSet(getWorld().getPlayers(EntityPlayer.class, this::isInside));
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public Room getRoom(int x, int z) {
        return getRoom(Pair.of(x, z));
    }

    public Room getRoom(Pair<Integer, Integer> location) {
        return rooms.get(location);
    }

    @Nullable
    public Room getRoom(EntityPlayer player) {
        if (isInside(player)) {
            int xPos = player.chunkCoordX - chunkX;
            int zPos = player.chunkCoordZ - chunkZ;
            return getRoom(xPos, zPos);
        }
        return null;
    }

    public Tent setRoom(@Nonnull Room room, int x, int z) {
        return setRoom(room, Pair.of(x, z));
    }

    public Tent setRoom(@Nonnull Room room, Pair<Integer, Integer> location) {
        rooms.put(location, room);
        return this;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public Tent reset() {
        rooms.clear();
        rooms.put(ORIGIN, ModObjects.EMPTY_ROOM);
        markDirty();
        return this;
    }

    public boolean canExtendTo(int x, int z) {
        return  x <= ConfigHandler.tentRadius
                && x >= -ConfigHandler.tentRadius
                && z <= ConfigHandler.tentRadius
                && z >= -ConfigHandler.tentRadius;
    }

    // TODO - Remove when moving to own dimension
    private World getWorld() {
        return DimensionManager.getWorld(ModObjects.TENT_DIMENSION.getId());
    }

    public void markDirty() {
        TentWorldSavedData.getData(getWorld()).markDirty();
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound data = new NBTTagCompound();

        data.setTag("uuid", NBTUtil.createUUIDTag(ownerId));
        data.setInteger("chunkX", chunkX);
        data.setInteger("chunkZ", chunkZ);
        data.setTag("privacy", privacy.serializeNBT());

        NBTTagCompound roomArray = new NBTTagCompound();
        rooms.forEach((key, value) ->
                roomArray.setString(key.getLeft() + "," + key.getRight(),
                value == null ? "null" : value.getSchematic().toString()));
        data.setTag("roomArray", roomArray);
        return data;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        ownerId = NBTUtil.getUUIDFromTag(nbt.getCompoundTag("uuid"));
        chunkX = nbt.getInteger("chunkX");
        chunkZ = nbt.getInteger("chunkZ");
        privacy = nbt.hasKey("privacy")
                  ? Privacy.fromNBT(nbt.getCompoundTag("privacy"), ownerId)
                  : new Privacy(ownerId);

        NBTTagCompound roomArray = nbt.getCompoundTag("roomArray");
        for (String key : roomArray.getKeySet()) {
            Pair<Integer, Integer> location = Pair.of(
                    Integer.parseInt(key.split(",")[0]),
                    Integer.parseInt(key.split(",")[1]));
            ResourceLocation roomId = new ResourceLocation(roomArray.getString(key));
            Room room = null;
            if (!roomId.toString().equalsIgnoreCase("null")) {
                room = ModObjects.ROOMS.get(roomId);
            }
            if (room != null) {
                setRoom(room, location);
            }
        }
    }
}
