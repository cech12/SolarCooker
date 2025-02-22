package de.cech12.solarcooker.init;

import com.mojang.blaze3d.vertex.PoseStack;
import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.blockentity.ForgeSolarCookerBlockEntity;
import de.cech12.solarcooker.item.ReflectorItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);

    static {
        Constants.SOLAR_COOKER_ITEM = solarCookerItem();
        Constants.REFLECTOR_ITEM = item(Constants.REFLECTOR_NAME, () -> new ReflectorItem(new Item.Properties().setId(id(Constants.REFLECTOR_NAME))));
        Constants.SHINING_DIAMOND_BLOCK_ITEM = fromBlock(Constants.SHINING_DIAMOND_BLOCK_NAME, Constants.SHINING_DIAMOND_BLOCK_BLOCK);
    }

    private static ResourceKey<Item> id(String name) {
        return ResourceKey.create(BuiltInRegistries.ITEM.key(), Constants.id(name));
    }

    private static RegistryObject<Item> item(String name, Supplier<Item> itemSupplier) {
        return ITEMS.register(name, itemSupplier);
    }

    private static RegistryObject<Item> fromBlock(String name, Supplier<Block> block) {
        return item(name, () -> new BlockItem(block.get(), new Item.Properties().setId(id(name))));
    }

    private static RegistryObject<Item> solarCookerItem() {
        return item(Constants.SOLAR_COOKER_NAME, () -> new BlockItem(Constants.SOLAR_COOKER_BLOCK.get(), new Item.Properties().setId(id(Constants.SOLAR_COOKER_NAME))) {
            @Override
            public void initializeClient(@Nonnull Consumer<IClientItemExtensions> consumer) {
                consumer.accept(new IClientItemExtensions() {
                    final BlockEntityWithoutLevelRenderer myRenderer = new BlockEntityWithoutLevelRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()) {
                        private ForgeSolarCookerBlockEntity blockEntity;

                        @Override
                        public void renderByItem(@Nonnull ItemStack stack, @Nonnull ItemDisplayContext displayContext, @Nonnull PoseStack matrix, @Nonnull MultiBufferSource buffer, int x, int y) {
                            if (blockEntity == null) {
                                blockEntity = new ForgeSolarCookerBlockEntity(BlockPos.ZERO, Constants.SOLAR_COOKER_BLOCK.get().defaultBlockState());
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
