package net.jonuuh.nickfinder.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Miscellaneous utilities.
 */
public class MiscUtils
{
//    /**
//     * Gets a list of online players. (in the player's world)
//     *
//     * @return the list of online players
//     */
//    public List<String> getOnlinePlayers()
//    {
//        final Collection<NetworkPlayerInfo> networkPlayerInfoCollection = Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap();
//        List<String> onlinePlayers = new ArrayList<>();
//        for (NetworkPlayerInfo networkPlayerInfo : networkPlayerInfoCollection)
//        {
//            onlinePlayers.add(getPlayerName(networkPlayerInfo));
//        }
//        return onlinePlayers;
//    }

    /**
     * Gets a list of players within a distance from the player.
     *
     * @param distance the distance from the player
     * @return the list of nearby players
     */
    public List<EntityPlayer> getNearbyPlayers(double distance)
    {
        final EntityPlayerSP clientPlayer = Minecraft.getMinecraft().thePlayer;
        List<EntityPlayer> nearbyPlayers = clientPlayer.worldObj.getEntitiesWithinAABB(EntityPlayer.class, clientPlayer.getEntityBoundingBox().expand(distance, distance, distance));
        nearbyPlayers.remove(clientPlayer);
        return nearbyPlayers;
    }

//
//    private String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn)
//    {
//        return networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
//    }
}
