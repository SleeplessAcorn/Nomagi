package info.sleeplessacorn.nomagi.core.data;

import com.google.common.collect.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Privacy implements INBTSerializable<NBTTagCompound> {

    private transient final UUID ownerId;
    private boolean allowKnocking;
    private AccessMode accessMode;
    private Map<ListType, Boolean> checks;
    private Multimap<ListType, UUID> lists;

    public Privacy(
            UUID ownerId, boolean allowKnocking, AccessMode accessMode,
            Map<ListType, Boolean> checks, Multimap<ListType, UUID> lists) {
        this.ownerId = ownerId;
        this.allowKnocking = allowKnocking;
        this.accessMode = accessMode;
        this.checks = checks;
        this.lists = lists;
    }

    public Privacy(UUID ownerId) {
        this(ownerId, true, AccessMode.RESTRICTED, Maps.newHashMap(), ArrayListMultimap.create());
    }

    public boolean canEnter(EntityPlayer player) {
        UUID playerId = player.getGameProfile().getId();
        if (!playerId.equals(ownerId)) {
            switch (accessMode) {
                case OPEN: {
                    return  !shouldCheck(ListType.BLACKLIST)
                            || !checkPlayer(ListType.BLACKLIST, playerId);
                }
                case RESTRICTED: {
                    return  shouldCheck(ListType.WHITELIST)
                            && checkPlayer(ListType.WHITELIST, playerId);
                }
                default:
                    return false;
            }
        }
        return true;
    }

    public void listPlayer(ListType listType, UUID uuid) {
        if (!lists.get(listType).contains(uuid)) {
            lists.put(listType, uuid);
        }
    }

    public boolean checkPlayer(ListType listType, UUID uuid) {
        return lists.get(listType).contains(uuid);
    }

    public void unlistPlayer(ListType listType, UUID uuid) {
        lists.remove(listType, uuid);
    }

    public boolean canKnock() {
        return allowKnocking;
    }

    public void setKnockable(boolean knockable) {
        this.allowKnocking = knockable;
    }

    public AccessMode getAccessMode() {
        return accessMode;
    }

    public void setAccessMode(AccessMode accessMode) {
        this.accessMode = accessMode;
    }

    public Set<UUID> getList(ListType listType) {
        return ImmutableSet.copyOf(lists.get(listType));
    }

    public boolean shouldCheck(ListType listType) {
        return checks.get(listType);
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();

        // General info
        tag.setBoolean("allowKnocking", allowKnocking);
        tag.setInteger("accessMode", accessMode.ordinal());

        // List checks
        NBTTagList checkLists = new NBTTagList();
        for (Map.Entry<ListType, Boolean> entry : checks.entrySet()) {
            NBTTagCompound entryTag = new NBTTagCompound();
            entryTag.setInteger("key", entry.getKey().ordinal());
            entryTag.setBoolean("value", entry.getValue());
            checkLists.appendTag(entryTag);
        }
        tag.setTag("checkLists", checkLists);

        // Lists
        NBTTagList listLists = new NBTTagList();
        for (ListType listType : ListType.values()) {
            NBTTagCompound listTag = new NBTTagCompound();
            listTag.setInteger("type", listType.ordinal());
            NBTTagList values = new NBTTagList();
            for (UUID uuid : lists.get(listType))
                values.appendTag(NBTUtil.createUUIDTag(uuid));
            listTag.setTag("values", values);
            listLists.appendTag(listTag);
        }
        tag.setTag("listLists", listLists);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        // General info
        allowKnocking = tag.getBoolean("allowKnocking");
        accessMode = AccessMode.values()[tag.getInteger("accessMode")];

        // List checks
        NBTTagList checkLists = tag.getTagList("checkLists", 10);
        for (int i = 0; i < checkLists.tagCount(); i++) {
            NBTTagCompound entryTag = checkLists.getCompoundTagAt(i);
            ListType key = ListType.values()[entryTag.getInteger("key")];
            boolean value = entryTag.getBoolean("value");
            checks.put(key, value);
        }

        // Lists
        NBTTagList listLists = tag.getTagList("listLists", 10);
        for (int i = 0; i < listLists.tagCount(); i++) {
            NBTTagCompound listTag = listLists.getCompoundTagAt(i);
            ListType listType = ListType.values()[listTag.getInteger("type")];
            Set<UUID> uuids = Sets.newHashSet();
            NBTTagList values = listTag.getTagList("values", 10);
            for (int j = 0; j < values.tagCount(); j++)
                uuids.add(NBTUtil.getUUIDFromTag(values.getCompoundTagAt(i)));

            lists.putAll(listType, uuids);
        }
    }

    public static Privacy fromNBT(NBTTagCompound tagCompound, UUID ownerId) {
        Privacy privacy = new Privacy(ownerId);
        privacy.deserializeNBT(tagCompound);
        return privacy;
    }

    public enum ListType {
        WHITELIST, BLACKLIST
    }

    public enum AccessMode {

        OPEN,
        CLOSED,
        RESTRICTED;

        private final String desc;

        AccessMode() {
            this.desc = "privacy.nomagi." + name().toLowerCase(Locale.ROOT);
        }

        public String getDescription() {
            return desc;
        }

    }

}
