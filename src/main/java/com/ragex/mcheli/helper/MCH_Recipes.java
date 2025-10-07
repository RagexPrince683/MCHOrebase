package com.ragex.mcheli.helper;

import com.google.common.collect.Sets;
import com.ragex.mcheli.MCH_ItemRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;

@EventBusSubscriber(
        modid = "mcheli"
)
public class MCH_Recipes {
    private static final Set<IRecipe> registryWrapper = Sets.newLinkedHashSet();

    @SubscribeEvent
    static void onRecipeRegisterEvent(Register<IRecipe> event) {
        MCH_ItemRecipe.registerItemRecipe(event.getRegistry());

        for (IRecipe recipe : registryWrapper) {
            event.getRegistry().register(recipe);
        }
    }

    public static void register(String name, IRecipe recipe) {
        registryWrapper.add(recipe.setRegistryName(MCH_Utils.suffix(name)));
    }

    public static ShapedRecipes addShapedRecipe(String name, ItemStack output, Object... params) {
        ShapedPrimer primer = CraftingHelper.parseShaped(params);
        ShapedRecipes recipe = new ShapedRecipes("", primer.width, primer.height, primer.input, output);
        register(name, recipe);
        return recipe;
    }

    public static boolean canCraft(EntityPlayer player, IRecipe recipe) {
        if (recipe == null) return false;
        if (recipe.getIngredients().isEmpty()) return false;

        for (Ingredient ingredient : recipe.getIngredients()) {
            if (ingredient == null || ingredient == Ingredient.EMPTY) continue; // skip blanks

            boolean found = false;
            for (ItemStack stack : player.inventory.mainInventory) {
                if (ingredient.apply(stack)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false;
            }
        }

        return true;
    }


    public static boolean consumeInventory(EntityPlayer player, IRecipe recipe) {
        for (Ingredient ingredient : recipe.getIngredients()) {
            if (ingredient != Ingredient.EMPTY || ingredient != null) {
                int i = 0;
                boolean flag = false;

                for (ItemStack itemstack : player.inventory.mainInventory) {
                    if (ingredient.apply(itemstack)) {
                        player.inventory.decrStackSize(i, 1);
                        flag = true;
                        break;
                    }

                    i++;
                }

                if (!flag) {
                    return false;
                }
            }
        }

        return true;
    }
}
