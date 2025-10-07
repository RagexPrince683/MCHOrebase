package com.ragex.mcheli.helper.info;

import com.google.common.collect.Maps;
import com.ragex.mcheli.helper.MCH_Utils;
import com.ragex.mcheli.helper.addon.AddonResourceLocation;
import com.ragex.mcheli.helicopter.MCH_HeliInfo;
import com.ragex.mcheli.hud.MCH_Hud;
import com.ragex.mcheli.plane.MCP_PlaneInfo;
import com.ragex.mcheli.tank.MCH_TankInfo;
import com.ragex.mcheli.ship.MCH_ShipInfo;
import com.ragex.mcheli.throwable.MCH_ThrowableInfo;
import com.ragex.mcheli.item.MCH_ItemInfo;
import com.ragex.mcheli.vehicle.MCH_VehicleInfo;
import com.ragex.mcheli.weapon.MCH_WeaponInfo;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.BiFunction;

public class ContentFactories {
    private static final Map<String, IContentFactory> TABLE = Maps.newHashMap();

    static {
        TABLE.put("helicopters", createFactory(ContentType.HELICOPTER, MCH_HeliInfo::new));
        TABLE.put("planes", createFactory(ContentType.PLANE, MCP_PlaneInfo::new));
        TABLE.put("ships", createFactory(ContentType.SHIP, MCH_ShipInfo::new));
        TABLE.put("tanks", createFactory(ContentType.TANK, MCH_TankInfo::new));
        TABLE.put("vehicles", createFactory(ContentType.VEHICLE, MCH_VehicleInfo::new));
        TABLE.put("throwable", createFactory(ContentType.THROWABLE, MCH_ThrowableInfo::new));
        TABLE.put("item", createFactory(ContentType.ITEM, MCH_ItemInfo::new));
        TABLE.put("weapons", createFactory(ContentType.WEAPON, MCH_WeaponInfo::new));
        if (MCH_Utils.isClient()) {
            TABLE.put("hud", createFactory(ContentType.HUD, MCH_Hud::new));
        }
    }

    @Nullable
    public static IContentFactory getFactory(@Nullable String dirName) {
        return dirName == null ? null : TABLE.get(dirName);
    }

    private static IContentFactory createFactory(final ContentType type, final BiFunction<AddonResourceLocation, String, IContentData> function) {
        return new IContentFactory() {
            @Override
            public IContentData create(AddonResourceLocation location, String filepath) {
                return function.apply(location, filepath);
            }

            @Override
            public ContentType getType() {
                return type;
            }
        };
    }
}
