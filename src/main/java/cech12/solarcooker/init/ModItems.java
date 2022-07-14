package cech12.solarcooker.init;

import cech12.solarcooker.SolarCookerMod;
import cech12.solarcooker.tileentity.SolarCookerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SolarCookerMod.MOD_ID);

    public static final RegistryObject<Item> SOLAR_COOKER = solarCookerItem();
    public static final RegistryObject<Item> REFLECTOR = fromBlock(ModBlocks.REFLECTOR, CreativeModeTab.TAB_DECORATIONS);
    public static final RegistryObject<Item> SHINING_DIAMOND_BLOCK = fromBlock(ModBlocks.SHINING_DIAMOND_BLOCK, CreativeModeTab.TAB_BUILDING_BLOCKS);

    private static RegistryObject<Item> fromBlock(RegistryObject<Block> block, CreativeModeTab tab) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    private static RegistryObject<Item> solarCookerItem() {
        return ITEMS.register(ModBlocks.SOLAR_COOKER.getId().getPath(), () -> new BlockItem(ModBlocks.SOLAR_COOKER.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)) {
            @Override
            public void initializeClient(@Nonnull Consumer<IItemRenderProperties> consumer) {
                consumer.accept(new IItemRenderProperties() {
                    final BlockEntityWithoutLevelRenderer myRenderer = new BlockEntityWithoutLevelRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()) {
                        private SolarCookerBlockEntity blockEntity;

                        @Override
                        public void renderByItem(@Nonnull ItemStack stack, @Nonnull ItemTransforms.TransformType transformType, @Nonnull PoseStack matrix, @Nonnull MultiBufferSource buffer, int x, int y) {
                            if (blockEntity == null) {
                                blockEntity = new SolarCookerBlockEntity(BlockPos.ZERO, ModBlocks.SOLAR_COOKER.get().defaultBlockState());
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
        });
    }

}
