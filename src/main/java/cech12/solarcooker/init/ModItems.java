package cech12.solarcooker.init;

import cech12.solarcooker.SolarCookerMod;
import cech12.solarcooker.blockentity.SolarCookerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
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

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SolarCookerMod.MOD_ID);

    public static final RegistryObject<Item> SOLAR_COOKER = solarCookerItem();
    public static final RegistryObject<Item> REFLECTOR = fromBlock(ModBlocks.REFLECTOR);
    public static final RegistryObject<Item> SHINING_DIAMOND_BLOCK = fromBlock(ModBlocks.SHINING_DIAMOND_BLOCK);

    private static RegistryObject<Item> fromBlock(RegistryObject<Block> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static RegistryObject<Item> solarCookerItem() {
        return ITEMS.register(ModBlocks.SOLAR_COOKER.getId().getPath(), () -> new BlockItem(ModBlocks.SOLAR_COOKER.get(), new Item.Properties()) {
            @Override
            public void initializeClient(@Nonnull Consumer<IClientItemExtensions> consumer) {
                consumer.accept(new IClientItemExtensions() {
                    final BlockEntityWithoutLevelRenderer myRenderer = new BlockEntityWithoutLevelRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()) {
                        private SolarCookerBlockEntity blockEntity;

                        @Override
                        public void renderByItem(@Nonnull ItemStack stack, @Nonnull ItemDisplayContext displayContext, @Nonnull PoseStack matrix, @Nonnull MultiBufferSource buffer, int x, int y) {
                            if (blockEntity == null) {
                                blockEntity = new SolarCookerBlockEntity(BlockPos.ZERO, ModBlocks.SOLAR_COOKER.get().defaultBlockState());
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
