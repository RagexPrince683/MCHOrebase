package com.ragex.mcheli.item;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import com.ragex.mcheli.MCH_InputFile;
import com.ragex.mcheli.MCH_Lib;
//import mcheli.throwable.MCH_ThrowableInfo;
//import mcheli.throwable.MCH_ThrowableInfoManager;
import com.ragex.mcheli.helper.addon.AddonResourceLocation;
import com.ragex.mcheli.helper.info.ContentRegistries;
import com.ragex.mcheli.throwable.MCH_ThrowableInfo;
import net.minecraft.item.Item;

public class MCH_ItemInfoManager {

    public static MCH_ItemInfo get(String name) {
        return ContentRegistries.item().get(name);
    }

    public static MCH_ItemInfo get(Item item) {
        return ContentRegistries.item().findFirst(info -> info.item == item);
    }
}
