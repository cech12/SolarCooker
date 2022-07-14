package cech12.solarcooker.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.BlockGetter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ShiningDiamondBlock extends Block {

    public ShiningDiamondBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable BlockGetter worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(Component.translatable("item.solarcooker.shining_diamond_block.description").withStyle(ChatFormatting.BLUE));
    }

}
