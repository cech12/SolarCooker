package de.cech12.solarcooker.init;

import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.block.FabricSolarCookerBlock;
import de.cech12.solarcooker.block.ShiningDiamondBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class ModBlocks {

    private static final Block SOLAR_COOKER_BLOCK = register(Constants.SOLAR_COOKER_NAME, new FabricSolarCookerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.5F, 3.5F).sound(SoundType.WOOD)));
    private static final Block SHINING_DIAMOND_BLOCK_BLOCK = register(Constants.SHINING_DIAMOND_BLOCK_NAME, new ShiningDiamondBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIAMOND).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL).lightLevel(state -> 15)));

    static {
        Constants.SOLAR_COOKER_BLOCK = () -> SOLAR_COOKER_BLOCK;
        Constants.SHINING_DIAMOND_BLOCK_BLOCK = () -> SHINING_DIAMOND_BLOCK_BLOCK;
    }

    public static void init() {}

    private static Block register(String name, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, Constants.id(name), block);
    }

}