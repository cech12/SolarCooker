package de.cech12.solarcooker.init;

import de.cech12.solarcooker.Constants;
import com.mojang.blaze3d.vertex.PoseStack;
import de.cech12.solarcooker.blockentity.SolarCookerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(Constants.MOD_ID);

    static {
        Constants.SOLAR_COOKER_ITEM = solarCookerItem();
        Constants.REFLECTOR_ITEM = fromBlock(Constants.REFLECTOR_NAME, Constants.REFLECTOR_BLOCK);
        Constants.SHINING_DIAMOND_BLOCK_ITEM = fromBlock(Constants.SHINING_DIAMOND_BLOCK_NAME, Constants.SHINING_DIAMOND_BLOCK_BLOCK);
    }

    private static DeferredHolder<Item, Item> fromBlock(String name, Supplier<Block> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static DeferredHolder<Item, Item> solarCookerItem() {
        return ITEMS.register(Constants.SOLAR_COOKER_NAME, () -> new BlockItem(Constants.SOLAR_COOKER_BLOCK.get(), new Item.Properties()) {
            @Override
            public void initializeClient(@Nonnull Consumer<IClientItemExtensions> consumer) {
                consumer.accept(new IClientItemExtensions() {
                    final BlockEntityWithoutLevelRenderer myRenderer = new BlockEntityWithoutLevelRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()) {
                        private SolarCookerBlockEntity blockEntity;

                        @Override
                        public void renderByItem(@Nonnull ItemStack stack, @Nonnull ItemDisplayContext displayContext, @Nonnull PoseStack matrix, @Nonnull MultiBufferSource buffer, int x, int y) {
                            if (blockEntity == null) {
                                blockEntity = new SolarCookerBlockEntity(BlockPos.ZERO, Constants.SOLAR_COOKER_BLOCK.get().defaultBlockState());
                            }
                            Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(blockEntity, matrix, buffer, x, y);
                        }
                    };

                    @Override
                    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                        return myRenderer;
                    }
                });
            }
        });
    }

}
