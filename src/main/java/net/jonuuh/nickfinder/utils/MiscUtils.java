package net.jonuuh.nickfinder.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MiscUtils
{
    public List<String> getOnlinePlayers()
    {
        final Collection<NetworkPlayerInfo> networkPlayerInfoCollection = Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap();
        List<String> onlinePlayers = new ArrayList<>();
        for (NetworkPlayerInfo networkPlayerInfo : networkPlayerInfoCollection)
        {
            onlinePlayers.add(networkPlayerInfo.getGameProfile().getName());
        }
        return onlinePlayers;
    }

    // player.getName()
    public List<EntityPlayer> getNearbyPlayers(double radius)
    {
        final EntityPlayerSP clientPlayer = Minecraft.getMinecraft().thePlayer;
        List<EntityPlayer> nearbyPlayers = clientPlayer.worldObj.getEntitiesWithinAABB(EntityPlayer.class, clientPlayer.getEntityBoundingBox().expand(radius, radius, radius));
        nearbyPlayers.remove(clientPlayer);
        return nearbyPlayers;
    }
}
