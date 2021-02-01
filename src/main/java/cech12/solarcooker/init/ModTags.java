package cech12.solarcooker.init;

import cech12.solarcooker.SolarCookerMod;
import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
//import net.minecraft.tags.ITag; //1.16
import net.minecraft.tags.Tag; //1.15
import net.minecraft.util.ResourceLocation; //1.15

import javax.annotation.Nonnull;

public class ModTags {

    public static class Blocks {

        public static final Tag<Block> SOLAR_COOKER_SHINING = tag("solar_cooker_shining"); //1.15

        private static Tag<Block> tag(@Nonnull String name) { //1.15
            return new BlockTags.Wrapper(new ResourceLocation(SolarCookerMod.MOD_ID + ":" + name)); //1.15
        }
    }
}
