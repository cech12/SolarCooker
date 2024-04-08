package de.cech12.solarcooker.init;

import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.block.ReflectorBlock;
import de.cech12.solarcooker.block.ShiningDiamondBlock;
import de.cech12.solarcooker.block.SolarCookerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {

    public static DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);

    static {
        Constants.SOLAR_COOKER_BLOCK = BLOCKS.register(Constants.SOLAR_COOKER_NAME, () -> new SolarCookerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.5F, 3.5F).sound(SoundType.WOOD)));
        Constants.REFLECTOR_BLOCK = BLOCKS.register(Constants.REFLECTOR_NAME, () -> new ReflectorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));
        Constants.SHINING_DIAMOND_BLOCK_BLOCK = BLOCKS.register(Constants.SHINING_DIAMOND_BLOCK_NAME, () -> new ShiningDiamondBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIAMOND).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL).lightLevel(state -> 15)));
    }

}