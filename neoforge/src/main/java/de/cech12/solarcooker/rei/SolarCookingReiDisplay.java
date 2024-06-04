package de.cech12.solarcooker.rei;

import de.cech12.solarcooker.crafting.SolarCookingRecipe;
import de.cech12.solarcooker.platform.Services;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.registry.RecipeManagerContext;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SolarCookingReiDisplay extends BasicDisplay {

    private final float xp;
    private final double cookTime;

    public <T extends AbstractCookingRecipe> SolarCookingReiDisplay(RecipeHolder<T> recipe) {
        this(EntryIngredients.ofIngredients(
                recipe.value().getIngredients()),
                Collections.singletonList(EntryIngredients.of(recipe.value().getResultItem(null))),
                recipe,
                recipe.value().getExperience(),
                recipe.value() instanceof SolarCookingRecipe ? recipe.value().getCookingTime() : (recipe.value().getCookingTime() * Services.CONFIG.getCookTimeFactor()));
    }

    public SolarCookingReiDisplay(List<EntryIngredient> input, List<EntryIngredient> output, CompoundTag tag) {
        this(input, output, RecipeManagerContext.getInstance().byId(tag, "location"),
                tag.getFloat("xp"), tag.getDouble("cookTime"));
    }

    public SolarCookingReiDisplay(List<EntryIngredient> input, List<EntryIngredient> output, RecipeHolder<?> recipe, float xp, double cookTime) {
        super(input, output, Optional.ofNullable(recipe).map(RecipeHolder::id));
        this.xp = xp;
        this.cookTime = cookTime;
    }

    public static Serializer<SolarCookingReiDisplay> getSerializer() {
        return Serializer.ofRecipeLess(SolarCookingReiDisplay::new, (display, tag) -> {
            tag.putFloat("xp", display.xp);
            tag.putDouble("cookTime", display.cookTime);
        });
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return SolarCookingReiDisplayCategory.ID;
    }

    public double getCookTime() {
        return cookTime;
    }

    public float getXp() {
        return xp;
    }
}
