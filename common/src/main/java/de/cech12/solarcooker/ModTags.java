package de.cech12.solarcooker;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nonnull;

public class ModTags {

    public static class Blocks {

        public static final TagKey<Block> SOLAR_COOKER_SHINING = tag("solar_cooker_shining");

        private static TagKey<Block> tag(@Nonnull String name) {
            return TagKey.create(Registries.BLOCK, new ResourceLocation(Constants.MOD_ID, name));
        }
    }
}
