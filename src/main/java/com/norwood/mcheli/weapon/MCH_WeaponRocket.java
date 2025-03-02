package com.norwood.mcheli.weapon;

import com.norwood.mcheli.MCH_Lib;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_WeaponRocket extends MCH_WeaponBase {
   public MCH_WeaponRocket(World w, Vec3d v, float yaw, float pitch, String nm, MCH_WeaponInfo wi) {
      super(w, v, yaw, pitch, nm, wi);
      this.acceleration = 4.0F;
      this.explosionPower = 3;
      this.power = 22;
      this.interval = 5;
      if (w.isRemote) {
         this.interval += 2;
      }

   }

   public String getName() {
      return super.getName() + (this.getCurrentMode() == 0 ? "" : " [HEIAP]");
   }

   public boolean shot(MCH_WeaponParam prm) {
      if (!this.worldObj.isRemote) {
         this.playSound(prm.entity);
         Vec3d v = MCH_Lib.RotVec3(0.0D, 0.0D, 1.0D, -prm.rotYaw, -prm.rotPitch, -prm.rotRoll);
         MCH_EntityRocket e = new MCH_EntityRocket(this.worldObj, prm.posX, prm.posY, prm.posZ, v.x, v.y, v.z, prm.rotYaw, prm.rotPitch, (double)this.acceleration);
         e.setName(this.name);
         e.setParameterFromWeapon(this, prm.entity, prm.user);
         if (prm.option1 == 0 && this.numMode > 1) {
            e.piercing = 0;
         }

         this.worldObj.func_72838_d(e);
      } else {
         this.optionParameter1 = this.getCurrentMode();
      }

      return true;
   }
}
