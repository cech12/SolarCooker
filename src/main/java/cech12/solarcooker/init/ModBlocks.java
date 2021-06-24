package cech12.solarcooker.init;

import cech12.solarcooker.SolarCookerMod;
import cech12.solarcooker.api.block.SolarCookerBlocks;
import cech12.solarcooker.block.ReflectorBlock;
import cech12.solarcooker.block.ShiningDiamondBlock;
import cech12.solarcooker.block.SolarCookerBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid= SolarCookerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModBlocks {

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        SolarCookerBlocks.SOLAR_COOKER = registerBlock("solar_cooker", ItemGroup.TAB_DECORATIONS, new SolarCookerBlock(AbstractBlock.Properties.of(Material.WOOD).strength(2.5F, 3.5F).sound(SoundType.WOOD)));
        SolarCookerBlocks.REFLECTOR = registerBlock("reflector", ItemGroup.TAB_DECORATIONS, new ReflectorBlock(AbstractBlock.Properties.of(Material.WOOD).strength(2.0F).sound(SoundType.WOOD)));
        SolarCookerBlocks.SHINING_DIAMOND_BLOCK = registerBlock("shining_diamond_block", ItemGroup.TAB_BUILDING_BLOCKS, new ShiningDiamondBlock(AbstractBlock.Properties.of(Material.METAL, MaterialColor.DIAMOND).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL).lightLevel(state -> 15)));
    }

    public static Block registerBlock(String name, ItemGroup itemGroup, Block block) {
        Item.Properties itemProperties = new Item.Properties().tab(itemGroup);
        try {
            if (block instanceof SolarCookerBlock) {
                ((SolarCookerBlock)block).setISTER(itemProperties);
            }
        } catch (NoSuchMethodError ignore) {}
        BlockItem itemBlock = new BlockItem(block, itemProperties);
        block.setRegistryName(name);
        itemBlock.setRegistryName(name);
        ForgeRegistries.BLOCKS.register(block);
        ForgeRegistries.ITEMS.register(itemBlock);
        return block;
    }

}