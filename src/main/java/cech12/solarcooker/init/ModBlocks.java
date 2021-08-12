package cech12.solarcooker.init;

import cech12.solarcooker.SolarCookerMod;
import cech12.solarcooker.api.block.SolarCookerBlocks;
import cech12.solarcooker.block.ReflectorBlock;
import cech12.solarcooker.block.ShiningDiamondBlock;
import cech12.solarcooker.block.SolarCookerBlock;
import cech12.solarcooker.tileentity.SolarCookerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid= SolarCookerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModBlocks {

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        SolarCookerBlocks.SOLAR_COOKER = registerBlock("solar_cooker", CreativeModeTab.TAB_DECORATIONS, new SolarCookerBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.5F, 3.5F).sound(SoundType.WOOD)));
        SolarCookerBlocks.REFLECTOR = registerBlock("reflector", CreativeModeTab.TAB_DECORATIONS, new ReflectorBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.0F).sound(SoundType.WOOD)));
        SolarCookerBlocks.SHINING_DIAMOND_BLOCK = registerBlock("shining_diamond_block", CreativeModeTab.TAB_BUILDING_BLOCKS, new ShiningDiamondBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.DIAMOND).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL).lightLevel(state -> 15)));
    }

    public static Block registerBlock(String name, CreativeModeTab itemGroup, Block block) {
        Item.Properties itemProperties = new Item.Properties().tab(itemGroup);
        BlockItem itemBlock;
        if (block instanceof SolarCookerBlock) {
            itemBlock = new BlockItem(block, itemProperties) {
                @Override
                public void initializeClient(@Nonnull Consumer<IItemRenderProperties> consumer) {
                    consumer.accept(new IItemRenderProperties() {
                        final BlockEntityWithoutLevelRenderer myRenderer = new BlockEntityWithoutLevelRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()) {
                            private SolarCookerBlockEntity blockEntity;

                            @Override
                            public void renderByItem(@Nonnull ItemStack stack, @Nonnull ItemTransforms.TransformType transformType, @Nonnull PoseStack matrix, @Nonnull MultiBufferSource buffer, int x, int y) {
                                if (blockEntity == null) {
                                    blockEntity = new SolarCookerBlockEntity(BlockPos.ZERO, SolarCookerBlocks.SOLAR_COOKER.defaultBlockState());
                                }
                                Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(blockEntity, matrix, buffer, x, y);
                            }
                        };

                        @Override
                        public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                            return myRenderer;
                        }
                    });
                }
            };
        } else {
            itemBlock = new BlockItem(block, itemProperties);
        }
        block.setRegistryName(name);
        itemBlock.setRegistryName(name);
        ForgeRegistries.BLOCKS.register(block);
        ForgeRegistries.ITEMS.register(itemBlock);
        return block;
    }

}