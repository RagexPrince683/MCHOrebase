package com.norwood.mcheli.__helper.client.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class MCH_WrapperItemLayerModel implements IModel {
   private ItemLayerModel model;
   private ModelBlock raw;

   public MCH_WrapperItemLayerModel(ModelBlock modelBlock) {
      this.raw = modelBlock;
      this.model = new ItemLayerModel(modelBlock);
   }

   public Collection<ResourceLocation> getTextures() {
      return this.model.getTextures();
   }

   public IModel retexture(ImmutableMap<String, String> textures) {
      return this.model.retexture(textures);
   }

   public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
      ItemCameraTransforms transforms = this.raw.func_181682_g();
      Map<TransformType, TRSRTransformation> tMap = Maps.newEnumMap(TransformType.class);
      tMap.putAll(PerspectiveMapWrapper.getTransforms(transforms));
      tMap.putAll(PerspectiveMapWrapper.getTransforms(state));
      IModelState perState = new SimpleModelState(ImmutableMap.copyOf(tMap), state.apply(Optional.empty()));
      return this.model.bake(perState, format, bakedTextureGetter);
   }
}
