package com.ragex.mcheli.helper.info;

import com.ragex.mcheli.helper.addon.AddonResourceLocation;

import java.util.List;

public interface IContentData {
    void parse(List<String> var1, String var2, boolean var3);

    boolean validate() throws Exception;

    void onPostReload();

    AddonResourceLocation getLoation();

    String getContentPath();
}
