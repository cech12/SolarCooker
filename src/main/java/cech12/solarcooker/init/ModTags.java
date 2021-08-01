package cech12.solarcooker.init;

import cech12.solarcooker.SolarCookerMod;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;

import javax.annotation.Nonnull;

public class ModTags {

    public static class Blocks {

        public static final Tag.Named<Block> SOLAR_COOKER_SHINING = tag("solar_cooker_shining");

        private static Tag.Named<Block> tag(@Nonnull String name) {
            return BlockTags.bind(SolarCookerMod.MOD_ID + ":" + name);
        }
    }
}
