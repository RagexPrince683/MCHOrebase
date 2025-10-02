package com.ragex.mcheli.helper.info;

import com.ragex.mcheli.helper.addon.AddonResourceLocation;

import javax.annotation.Nullable;

public interface IContentFactory {
    @Nullable
    IContentData create(AddonResourceLocation var1, String var2);

    ContentType getType();
}
