package com.ragex.mcheli.helper.client.renderer.item;

import com.ragex.mcheli.gltd.MCH_RenderGLTD;
import com.ragex.mcheli.wrapper.W_McClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BuiltInGLTDItemRenderer implements IItemModelRenderer {
    @Override
    public boolean shouldRenderer(ItemStack itemStack, TransformType transformType) {
        return IItemModelRenderer.isFirstPerson(transformType) || IItemModelRenderer.isThirdPerson(transformType) || transformType == TransformType.GROUND;
    }

    @Override
    public void renderItem(ItemStack itemStack, EntityLivingBase entityLivingBase, TransformType transformType, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.enableCull();
        W_McClient.MOD_bindTexture("textures/gltd.png");
         GlStateManager.enableRescaleNormal();;
        GlStateManager.enableColorMaterial();
        MCH_RenderGLTD.model.renderAll();
         GlStateManager.disableRescaleNormal();;
        GlStateManager.popMatrix();
    }
}
