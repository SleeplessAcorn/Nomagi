package info.sleeplessacorn.nomagi.command.tent;

import com.google.common.collect.Lists;
import info.sleeplessacorn.nomagi.core.ModObjects;
import info.sleeplessacorn.nomagi.core.data.Tent;
import info.sleeplessacorn.nomagi.core.data.TentWorldSavedData;
import info.sleeplessacorn.nomagi.util.GeneratorUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class CommandReset extends CommandBase {

    @Override
    public String getName() {
        return "reset";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "reset [playerId]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        WorldServer world = server.worldServerForDimension(ModObjects.TENT_DIMENSION.getId());
        UUID playerId = args.length == 0 ? getCommandSenderAsPlayer(sender).getGameProfile().getId() : UUID.fromString(args[0]);
        Tent tent = TentWorldSavedData.getData(world).getTent(playerId);
        if (tent == null)
            throw new CommandException("Specified UUID does not have a tent.");

        // Move all players to central room and notify them
        BlockPos moveTo = new ChunkPos(tent.getChunkX(), tent.getChunkZ()).getBlock(8, Tent.BASE_HEIGHT, 8);
        for (EntityPlayer player : tent.getPlayersInside()) {
            player.setPositionAndUpdate(moveTo.getX() + 0.5D, moveTo.getY() + 2, moveTo.getZ() + 0.5D);
            player.sendStatusMessage(new TextComponentTranslation("chat.nomagi.resetting"), true);
        }

        for (Chunk chunk : tent.getUsedChunks()) {
            if (chunk.isAtLocation(tent.getChunkX(), tent.getChunkZ()))
                continue;

            for (Entity entity : world.getLoadedEntityList()) {
                if (chunk.isAtLocation(entity.chunkCoordX, entity.chunkCoordZ)) {
                    entity.setDropItemsWhenDead(false);
                    entity.setDead();
                }
            }

            long chunkId = ChunkPos.asLong(chunk.xPosition, chunk.zPosition);
            Chunk newChunk = world.getChunkProvider().chunkGenerator.provideChunk(chunk.xPosition, chunk.zPosition);
            world.getChunkProvider().id2ChunkMap.put(chunkId, newChunk);
            newChunk.onChunkLoad();
            newChunk.populateChunk(world.getChunkProvider(), world.getChunkProvider().chunkGenerator);

            for (EntityPlayerMP player : server.getPlayerList().getPlayers())
                if (tent.isInside(player))
                    player.connection.sendPacket(new SPacketChunkData(newChunk, 65535));

            tent.reset();
        }

        GeneratorUtil.generateInitialRoom(world, tent.getChunkX(), tent.getChunkZ());
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> completions = Lists.newArrayList();
        switch (args.length) {
            case 1: {
                TentWorldSavedData tentData = TentWorldSavedData.getData(server.getEntityWorld());
                for (Tent tent : tentData.getTents())
                    completions.add(tent.getOwnerId().toString());
                break;
            }
        }
        return completions;
    }
}
