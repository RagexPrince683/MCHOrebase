package com.ragex.mcheli.item;

import com.ragex.mcheli.MCH_BaseInfo;
import com.ragex.mcheli.helper.addon.AddonResourceLocation;
import com.ragex.mcheli.helper.info.IContentData;
import com.ragex.mcheli.helper.info.IItemContent;
import com.ragex.mcheli.wrapper.W_Item;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MCH_ItemInfo extends MCH_BaseInfo implements IItemContent { //implements
    public final String name;
    public String displayName;
    public final HashMap<String, String> displayNameLang;
    public int itemID;
    public W_Item item;
    public List<String> recipeString;
    public final List<IRecipe> recipe;
    public boolean isShapedRecipe;
    public int stackSize;

    //1.7 method:
    //public MCH_ItemInfo(String name) {
    //    this.name = name;
    //    this.displayName = name;
    //    this.displayNameLang = new HashMap();
    //    this.itemID = 0;
    //    this.item = null;
    //    this.recipeString = new ArrayList();
    //    this.recipe = new ArrayList();
    //    this.isShapedRecipe = true;
    //    this.stackSize = 1;
    //}

    public MCH_ItemInfo(AddonResourceLocation location, String filePath) {
        super(location, filePath); // required in 1.12.2 or something
        this.name = location.getPath();
        this.displayName = location.getPath();
        this.displayNameLang = new HashMap<>();
        this.itemID = 0;
        this.item = null;
        this.recipeString = new ArrayList<>();
        this.recipe = new ArrayList<>();
        this.isShapedRecipe = true;
        this.stackSize = 1;
    }

    @Override
    public Item getItem() {
        return this.item;
    }

    /**
     * public MCH_ThrowableInfo(AddonResourceLocation location, String path) {
     *         super(location, path);
     *         this.name = location.getPath();
     *         this.displayName = location.getPath();
     *         this.displayNameLang = new HashMap<>();
     *         this.itemID = 0;
     *         this.item = null;
     *         this.recipeString = new ArrayList<>();
     *         this.recipe = new ArrayList<>();
     *         this.isShapedRecipe = true;
     *         this.power = 0;
     *         this.acceleration = 1.0F;
     *         this.accelerationInWater = 1.0F;
     *         this.dispenseAcceleration = 1.0F;
     *         this.explosion = 0;
     *         this.delayFuse = 0;
     *         this.bound = 0.2F;
     *         this.timeFuse = 0;
     *         this.flaming = false;
     *         this.stackSize = 1;
     *         this.soundVolume = 1.0F;
     *         this.soundPitch = 1.0F;
     *         this.proximityFuseDist = 0.0F;
     *         this.accuracy = 0.0F;
     *         this.aliveTime = 10;
     *         this.bomblet = 0;
     *         this.bombletDiff = 0.3F;
     *         this.model = null;
     *         this.smokeSize = 10.0F;
     *         this.smokeNum = 0;
     *         this.smokeVelocityVertical = 1.0F;
     *         this.smokeVelocityHorizontal = 1.0F;
     *         this.gravity = 0.0F;
     *         this.gravityInWater = -0.04F;
     *         this.particleName = "explode";
     *         this.disableSmoke = true;
     *         this.smokeColor = new MCH_Color();
     *     }

     */

    public void loadItemData(String item, String data) {
        if(item.compareTo("displayname") == 0) {
            this.displayName = data;
        } else {
            String[] s;
            if(item.compareTo("adddisplayname") == 0) {
                s = data.split("\\s*,\\s*");
                if(s != null && s.length == 2) {
                    this.displayNameLang.put(s[0].trim(), s[1].trim());
                }
            } else if(item.compareTo("itemid") == 0) {
                this.itemID = this.toInt(data, 0, '\uffff');
            } else if(item.compareTo("addrecipe") != 0 && item.compareTo("addshapelessrecipe") != 0) {
                if(item.equalsIgnoreCase("StackSize")) {
                    this.stackSize = this.toInt(data, 1, 64);
                }
            } else {
                this.isShapedRecipe = item.compareTo("addrecipe") == 0;
                this.recipeString.add(data.toUpperCase());
            }
        }

    }


    @Override
    public void onPostReload() {

    }
}
