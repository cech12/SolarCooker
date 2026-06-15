package de.cech12.solarcooker.rei;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.cech12.solarcooker.crafting.SolarCookingRecipe;
import de.cech12.solarcooker.platform.Services;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SolarCookingReiDisplay extends BasicDisplay {
    public static DisplaySerializer<SolarCookingReiDisplay> SERIALIZER = serializer();

    private final float xp;
    private final double cookTime;

    public <T extends AbstractCookingRecipe> SolarCookingReiDisplay(RecipeHolder<T> recipe) {
        this(List.of(EntryIngredients.ofIngredient((recipe.value()).input())),
                List.of(EntryIngredients.of(recipe.value().assemble(new SingleRecipeInput(new ItemStack(recipe.value().input().items().findFirst().get().value()))))),
                Optional.of(recipe.id().identifier()),
                (recipe.value()).experience(),
                recipe.value() instanceof SolarCookingRecipe ? recipe.value().cookingTime() : (recipe.value().cookingTime() * Services.CONFIG.getCookTimeFactor()));
    }

    public SolarCookingReiDisplay(List<EntryIngredient> input, List<EntryIngredient> output, Optional<Identifier> id, CompoundTag tag) {
        this(input, output, id, tag.getFloatOr("xp", 0), tag.getDoubleOr("cookTime", 200));
    }

    public SolarCookingReiDisplay(List<EntryIngredient> input, List<EntryIngredient> output, Optional<Identifier> id, float xp, double cookTime) {
        super(input, output, id);
        this.xp = xp;
        this.cookTime = cookTime;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return SolarCookingReiDisplayCategory.ID;
    }

    @Override
    @Nullable
    public DisplaySerializer<? extends Display> getSerializer() {
        return SERIALIZER;
    }

    public double getCookTime() {
        return cookTime;
    }

    public float getXp() {
        return xp;
    }

    private static DisplaySerializer<SolarCookingReiDisplay> serializer() {
        return DisplaySerializer.of(
                RecordCodecBuilder.mapCodec(instance -> instance.group(
                        EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(SolarCookingReiDisplay::getInputEntries),
                        EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(SolarCookingReiDisplay::getOutputEntries),
                        Identifier.CODEC.optionalFieldOf("location").forGetter(SolarCookingReiDisplay::getDisplayLocation),
                        Codec.FLOAT.fieldOf("xp").forGetter(display -> display.xp),
                        Codec.DOUBLE.fieldOf("cookTime").forGetter(display -> display.cookTime)
                ).apply(instance, SolarCookingReiDisplay::new)),
                StreamCodec.composite(
                        EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                        SolarCookingReiDisplay::getInputEntries,
                        EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                        SolarCookingReiDisplay::getOutputEntries,
                        ByteBufCodecs.optional(Identifier.STREAM_CODEC),
                        SolarCookingReiDisplay::getDisplayLocation,
                        ByteBufCodecs.FLOAT,
                        display -> display.xp,
                        ByteBufCodecs.DOUBLE,
                        display -> display.cookTime,
                        SolarCookingReiDisplay::new
                ));
    }
}
