package cech12.solarcooker.init;

import cech12.solarcooker.SolarCookerMod;
import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;

import javax.annotation.Nonnull;

public class ModTags {

    public static class Blocks {

        public static final ITag.INamedTag<Block> SOLAR_COOKER_SHINING = tag("solar_cooker_shining");

        private static ITag.INamedTag<Block> tag(@Nonnull String name) {
            return BlockTags.makeWrapperTag(SolarCookerMod.MOD_ID + ":" + name);
        }
    }
}
