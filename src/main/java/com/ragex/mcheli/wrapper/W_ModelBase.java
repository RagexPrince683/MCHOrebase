package com.ragex.mcheli.wrapper;

import com.ragex.mcheli.Tags;
import com.ragex.mcheli.helper.client._IModelCustom;
import com.ragex.mcheli.helper.client._IModelCustomLoader;
import com.ragex.mcheli.helper.client._ModelFormatException;
import com.ragex.mcheli.helper.client.model.loader.TechneModelLoader;
import com.ragex.mcheli.wrapper.modelloader.W_MqoModelLoader;
import com.ragex.mcheli.wrapper.modelloader.W_ObjModelLoader;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;

public abstract class W_ModelBase extends ModelBase {
    private static final _IModelCustomLoader objLoader = new W_ObjModelLoader();
    private static final _IModelCustomLoader mqoLoader = new W_MqoModelLoader();
    private static final _IModelCustomLoader tcnLoader = new TechneModelLoader();

    public static _IModelCustom loadModel(String name) throws IllegalArgumentException, _ModelFormatException {
        ResourceLocation resource = new ResourceLocation(Tags.MODID, name);
        String path = resource.getPath();
        int i = path.lastIndexOf(46);
        if (i == -1) {
            throw new IllegalArgumentException("The resource name is not valid");
        } else if (path.substring(i).equalsIgnoreCase(".mqo")) {
            return mqoLoader.loadInstance(resource);
        } else {
            return path.substring(i).equalsIgnoreCase(".obj") ? objLoader.loadInstance(resource) : tcnLoader.loadInstance(resource);
        }
    }
}
