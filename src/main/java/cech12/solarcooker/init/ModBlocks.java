package cech12.solarcooker.init;

import cech12.solarcooker.SolarCookerMod;
import cech12.solarcooker.block.ReflectorBlock;
import cech12.solarcooker.block.ShiningDiamondBlock;
import cech12.solarcooker.block.SolarCookerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid= SolarCookerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModBlocks {

    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SolarCookerMod.MOD_ID);

    public static RegistryObject<Block> SOLAR_COOKER = BLOCKS.register("solar_cooker", () -> new SolarCookerBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.5F, 3.5F).sound(SoundType.WOOD)));
    public static RegistryObject<Block> REFLECTOR = BLOCKS.register("reflector", () -> new ReflectorBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.0F).sound(SoundType.WOOD)));
    public static RegistryObject<Block> SHINING_DIAMOND_BLOCK = BLOCKS.register("shining_diamond_block", () -> new ShiningDiamondBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.DIAMOND).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL).lightLevel(state -> 15)));

}