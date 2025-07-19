package com.norwood.mcheli.__helper.addon;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.norwood.mcheli.__helper.MCH_Utils;

import java.io.File;

public class BuiltinAddonPack extends AddonPack {
    private static BuiltinAddonPack instance = null;

    private BuiltinAddonPack() {
        super("@builtin", "MCHeli-Builtin", "1.0", null, "EMB4-MCHeli", ImmutableList.of("EMB4", "Murachiki27"), "Builtin addon", "1", ImmutableMap.of());
    }

    public static BuiltinAddonPack instance() {
        if (instance == null) {
            instance = new BuiltinAddonPack();
        }

        return instance;
    }

    @Override
    public File getFile() {
        return MCH_Utils.getSource();
    }
}
