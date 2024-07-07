package de.cech12.solarcooker;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nonnull;

public class ModTags {

    public static class Blocks {

        public static final TagKey<Block> SOLAR_COOKER_SHINING = tag("solar_cooker_shining");

        private static TagKey<Block> tag(@Nonnull String name) {
            return TagKey.create(Registries.BLOCK, Constants.id(name));
        }
    }

    public static class Items {

        public static final TagKey<Item> SOLAR_COOKER_REFLECTOR = tag("solar_cooker_reflector");

        private static TagKey<Item> tag(@Nonnull String name) {
            return TagKey.create(Registries.ITEM, Constants.id(name));
        }
    }
}
