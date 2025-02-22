package de.cech12.solarcooker.init;

import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.block.ShiningDiamondBlock;
import de.cech12.solarcooker.block.SolarCookerBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModBlocks {

    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);

    static {
        Constants.SOLAR_COOKER_BLOCK = BLOCKS.register(Constants.SOLAR_COOKER_NAME, () -> new SolarCookerBlock(BlockBehaviour.Properties.of().setId(id(Constants.SOLAR_COOKER_NAME)).mapColor(MapColor.WOOD).strength(2.5F, 3.5F).sound(SoundType.WOOD)));
        Constants.SHINING_DIAMOND_BLOCK_BLOCK = BLOCKS.register(Constants.SHINING_DIAMOND_BLOCK_NAME, () -> new ShiningDiamondBlock(BlockBehaviour.Properties.of().setId(id(Constants.SHINING_DIAMOND_BLOCK_NAME)).mapColor(MapColor.DIAMOND).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL).lightLevel(state -> 15)));
    }

    private static ResourceKey<Block> id(String name) {
        return ResourceKey.create(BuiltInRegistries.BLOCK.key(), Constants.id(name));
    }

}