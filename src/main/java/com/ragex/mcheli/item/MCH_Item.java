package com.ragex.mcheli.item;

import com.ragex.mcheli.wrapper.W_Item;
//import com.ragex.mcheli.wrapper.W_MOD;


public class MCH_Item extends W_Item {

    //@Override
    //public Item setTexture(String par1Str) {
    //    this.setTextureName(W_MOD.DOMAIN + ":" + par1Str);
    //    return this;
    //}

    public MCH_Item(int par1) {
        super(par1);
        this.setMaxStackSize(1);
    }

}
