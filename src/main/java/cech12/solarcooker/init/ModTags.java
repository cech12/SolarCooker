package cech12.solarcooker.init;

import cech12.solarcooker.SolarCookerMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nonnull;

public class ModTags {

    public static class Blocks {

        public static final TagKey<Block> SOLAR_COOKER_SHINING = tag("solar_cooker_shining");

        private static TagKey<Block> tag(@Nonnull String name) {
            return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(SolarCookerMod.MOD_ID, name));
        }
    }
}
