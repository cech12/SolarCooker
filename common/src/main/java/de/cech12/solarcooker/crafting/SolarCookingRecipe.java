package de.cech12.solarcooker.crafting;

import com.mojang.serialization.MapCodec;
import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.platform.Services;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SolarCookingRecipe extends AbstractCookingRecipe {

    public static final MapCodec<SolarCookingRecipe> MAP_CODEC = cookingMapCodec(SolarCookingRecipe::new, 200);
    public static final StreamCodec<RegistryFriendlyByteBuf, SolarCookingRecipe> STREAM_CODEC = cookingStreamCodec(SolarCookingRecipe::new);
    public static final RecipeSerializer<SolarCookingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    public SolarCookingRecipe(Recipe.CommonInfo commonInfo, AbstractCookingRecipe.CookingBookInfo bookInfo, Ingredient ingredient, ItemStackTemplate result, float experience, int cookingTime) {
        super(commonInfo, bookInfo, ingredient, result, experience, cookingTime);
    }

    public static SolarCookingRecipe convert(@NotNull AbstractCookingRecipe recipe) {
        ItemStack resultStack = recipe.assemble(new SingleRecipeInput(new ItemStack(recipe.input().items().findFirst().get())));
        return new SolarCookingRecipe(new CommonInfo(recipe.showNotification()), new CookingBookInfo(recipe.category(), recipe.group()), recipe.input(), ItemStackTemplate.fromNonEmptyStack(resultStack), recipe.experience(), (int) (recipe.cookingTime() * Services.CONFIG.getCookTimeFactor()));
    }

    @Override
    @NotNull
    public List<RecipeDisplay> display() {
        return List.of(new FurnaceRecipeDisplay(this.input().display(), SlotDisplay.Empty.INSTANCE, new SlotDisplay.ItemStackSlotDisplay(this.result()), new SlotDisplay.ItemSlotDisplay(this.furnaceIcon()), (int) (this.cookingTime() * Services.CONFIG.getCookTimeFactor()), this.experience()));
    }

    @Override
    @NotNull
    public RecipeType<? extends AbstractCookingRecipe> getType() {
        return Constants.SOLAR_COOKING_RECIPE_TYPE.get();
    }

    @Override
    @NotNull
    public Item furnaceIcon() {
        return Constants.SOLAR_COOKER_BLOCK.get().asItem();
    }

    @Override
    @NotNull
    public RecipeSerializer<? extends AbstractCookingRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    @NotNull
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.FURNACE_MISC;
    }
}
