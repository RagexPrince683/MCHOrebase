package com.norwood.mcheli.weapon;

import com.norwood.mcheli.__helper.MCH_Utils;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_WeaponDummy extends MCH_WeaponBase {
   static final MCH_WeaponInfo dummy = new MCH_WeaponInfo(MCH_Utils.buildinAddon("none"), "none");

   public int getUseInterval() {
      return 0;
   }

   public MCH_WeaponDummy(World w, Vec3d v, float yaw, float pitch, String nm, MCH_WeaponInfo wi) {
      super(w, v, yaw, pitch, !nm.isEmpty() ? nm : "none", wi != null ? wi : dummy);
   }

   @Override
   public boolean shot(MCH_WeaponParam prm) {
      return false;
   }
}
