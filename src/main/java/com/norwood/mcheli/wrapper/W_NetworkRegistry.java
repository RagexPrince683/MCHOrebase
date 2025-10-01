package com.norwood.mcheli.wrapper;

import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class W_NetworkRegistry {

    public static void registerGuiHandler(Object mod, IGuiHandler handler) {
        NetworkRegistry.INSTANCE.registerGuiHandler(mod, handler);
    }
}
