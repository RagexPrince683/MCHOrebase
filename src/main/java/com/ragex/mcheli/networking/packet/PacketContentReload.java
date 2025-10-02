package com.ragex.mcheli.networking.packet;

import com.ragex.mcheli.aircraft.MCH_EntityAircraft;
import com.ragex.mcheli.helper.MCH_Utils;
import com.ragex.mcheli.helper.info.ContentRegistries;
import hohserg.elegant.networking.api.ClientToServerPacket;
import hohserg.elegant.networking.api.ElegantPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;

import java.util.ArrayList;
import java.util.UUID;

@ElegantPacket
public class PacketContentReload extends PacketBase implements ClientToServerPacket {

    public final ReloadType type;

    public PacketContentReload(ReloadType type) {
        this.type = type;
    }

    @Override
    public void onReceive(EntityPlayerMP player) {
        if (player.world.isRemote) return;

        switch (type) {
            case VEHICLE -> reloadRiddenAircraft(player);
            case WEAPON -> reloadAllWeapons();
        }
    }


    private void reloadRiddenAircraft(EntityPlayerMP player) {
        MCH_EntityAircraft ridden = MCH_EntityAircraft.getAircraft_RiddenOrControl(player);
        if (ridden == null || ridden.getAcInfo() == null) return;

        String name = ridden.getAcInfo().name;

        for (WorldServer world : MCH_Utils.getServer().worlds) {
            for (Entity entity : new ArrayList<>(world.loadedEntityList)) {
                if (entity instanceof MCH_EntityAircraft ac && ac.getAcInfo() != null && ac.getAcInfo().name.equals(name)) {
                    ac.changeType(name);
                    ac.createSeats(UUID.randomUUID().toString());
                    ac.onAcInfoReloaded();
                }
            }
        }
    }

    private void reloadAllWeapons() {
        ContentRegistries.weapon().reloadAll();

        for (WorldServer world : MCH_Utils.getServer().worlds) {
            for (Entity entity : new ArrayList<>(world.loadedEntityList)) {
                if (entity instanceof MCH_EntityAircraft ac && ac.getAcInfo() != null) {
                    ac.changeType(ac.getAcInfo().name);
                    ac.createSeats(UUID.randomUUID().toString());
                }
            }
        }
    }


    public static enum ReloadType {
        VEHICLE, WEAPON

    }
}
