/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.utils.player;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PlayerEjectEvent;
import io.github.retrooper.packetevents.nettyhandler.NettyPacketHandler;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.versionlookup.VersionLookupUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public final class PlayerUtils {
    public final HashMap<UUID, Short> playerPingMap = new HashMap<>();
    public final HashMap<UUID, Short> playerSmoothedPingMap = new HashMap<>();

    /**
     * This map stores each player's client version.
     */
    public final HashMap<UUID, ClientVersion> clientVersionsMap = new HashMap<>();

    /**
     * Use reflection to read the ping value NMS calculates for the player.
     * NMS smooths the player ping.
     * @param player
     * @return Get NMS smoothed Player Ping
     */
    public int getNMSPing(final Player player) {
        return NMSUtils.getPlayerPing(player);
    }

    /**
     * Use the ping PacketEvents calculates for the player. (Updates every incoming Keep Alive packet)
     * @param player Player
     * @return Get Ping
     */
    public short getPing(final Player player) {
        return playerPingMap.getOrDefault(player.getUniqueId(), (short)0);
    }

    public short getSmoothedPing(final Player player) {
        return playerSmoothedPingMap.getOrDefault(player.getUniqueId(), (short)0);
    }

    public short getPing(final UUID uuid) {
        return playerPingMap.getOrDefault(uuid, (short)0);
    }

    public short getSmoothedPing(final UUID uuid) {
        return playerSmoothedPingMap.getOrDefault(uuid, (short)0);
    }

    /**
     * Get the client version by a UUID.
     * @param uuid
     * @return Get Client Version
     */
    public ClientVersion getClientVersion(final UUID uuid) {
        return clientVersionsMap.get(uuid);
    }

    /**
     * Get the client version by a player object.
     * @param player
     * @return Get Client Version
     */
    public ClientVersion getClientVersion(final Player player) {
        return clientVersionsMap.get(player.getUniqueId());
    }

    /**
     * Get the protocol version.
     * @param player
     * @return Get Protocol Version
     */
    public int getProtocolVersion(final Player player) {
        return VersionLookupUtils.getProtocolVersion(player);
    }

    /**
     * Inject a player.
     *
     * (This also calls the {@link io.github.retrooper.packetevents.event.impl.PlayerInjectEvent})
     * @param player
     */
    public void injectPlayer(final Player player) {
        if (PacketEvents.getSettings().shouldInjectAsync()) {
            NettyPacketHandler.injectPlayerAsync(player);
        } else {
            NettyPacketHandler.injectPlayer(player);
        }
    }

    /**
     * Eject a player.
     * (This also calls the {@link PlayerEjectEvent})
     * @param player
     */
    public void ejectPlayer(final Player player) {
        if (PacketEvents.getSettings().shouldEjectAsync()) {
            NettyPacketHandler.ejectPlayerAsync(player);
        } else {
            NettyPacketHandler.ejectPlayer(player);
        }
    }

    /**
     * Send a {@link SendableWrapper} wrapper to a player.
     * @param player
     * @param sendableWrapper
     */
    public void sendPacket(final Player player, final SendableWrapper sendableWrapper) {
        NMSUtils.sendSendableWrapper(player, sendableWrapper);
    }

    /**
     * Send a raw NMS packet to a player.
     * @param player
     * @param nmsPacket
     */
    public void sendNMSPacket(final Player player, final Object nmsPacket) {
        NMSUtils.sendNMSPacket(player, nmsPacket);
    }
}
