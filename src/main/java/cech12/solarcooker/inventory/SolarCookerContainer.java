package cech12.solarcooker.inventory;

import cech12.solarcooker.config.ServerConfig;
import cech12.solarcooker.init.ModMenuTypes;
import cech12.solarcooker.tileentity.AbstractSolarCookerBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class SolarCookerContainer extends AbstractContainerMenu {
    private final RecipeType<? extends AbstractCookingRecipe> specificRecipeType;
    private final AbstractSolarCookerBlockEntity cooker;
    protected final Level world;

    public SolarCookerContainer(RecipeType<? extends AbstractCookingRecipe> specificRecipeTypeIn, int id,
                                Inventory playerInventoryIn, AbstractSolarCookerBlockEntity cooker) {
        super(ModMenuTypes.SOLAR_COOKER.get(), id);
        this.specificRecipeType = specificRecipeTypeIn;
        checkContainerSize(cooker, 2);
        this.cooker = cooker;
        cooker.startOpen(playerInventoryIn.player);
        this.world = playerInventoryIn.player.level;

        //add cooker inventory slots
        this.addSlot(new Slot(cooker, 0, 56, 17));
        this.addSlot(new SolarCookerResultSlot(playerInventoryIn.player, cooker, 1, 116, 35));

        //add player inventory
        for(int playerInvRow = 0; playerInvRow < 3; ++playerInvRow) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInventoryIn, j1 + playerInvRow * 9 + 9, 8 + j1 * 18, 84 + playerInvRow * 18));
            }
        }
        for(int playerHotbarSlot = 0; playerHotbarSlot < 9; ++playerHotbarSlot) {
            this.addSlot(new Slot(playerInventoryIn, playerHotbarSlot, 8 + playerHotbarSlot * 18, 142));
        }
    }

    public SolarCookerContainer(RecipeType<? extends AbstractCookingRecipe> specificRecipeTypeIn, int id,
                                Inventory playerInventoryIn, BlockPos pos) {
        this(specificRecipeTypeIn, id, playerInventoryIn, (AbstractSolarCookerBlockEntity) playerInventoryIn.player.level.getBlockEntity(pos));
    }

    @Override
    public boolean stillValid(@Nonnull Player playerIn) {
        return this.cooker.stillValid(playerIn);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    @Override
    @Nonnull
    public ItemStack quickMoveStack(@Nonnull Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == 1) {
                if (!this.moveItemStackTo(itemstack1, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index != 0) {
                if (this.hasRecipe(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 2 && index < 29) {
                    if (!this.moveItemStackTo(itemstack1, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 29 && index < 38 && !this.moveItemStackTo(itemstack1, 2, 29, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 2, 38, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    @Override
    public void setItem(int slotID, int stateId, @Nonnull ItemStack stack) {
        super.setItem(slotID, stateId, stack);
    }

    protected boolean hasRecipe(ItemStack stack) {
        if (this.world != null) {
            if (this.world.getRecipeManager().getRecipeFor(this.specificRecipeType, new SimpleContainer(stack), this.world).isPresent()) {
                return true;
            }
            if (ServerConfig.VANILLA_RECIPES_ENABLED.get()) {
                return this.world.getRecipeManager().getRecipesFor(ServerConfig.getRecipeType(), new SimpleContainer(stack), this.world)
                        .stream().anyMatch(abstractCookingRecipe -> ServerConfig.isRecipeNotBlacklisted(abstractCookingRecipe.getId()));
            }
        }
        return false;
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void removed(@Nonnull Player playerIn) {
        super.removed(playerIn);
        this.cooker.stopOpen(playerIn);
    }

    @OnlyIn(Dist.CLIENT)
    public int getCookProgressionScaled() {
        int i = this.cooker.getCookTime();
        int j = this.cooker.getCookTimeTotal();
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isBurning() {
        return this.cooker.getCookTime() > 0;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isSunlit() {
        return this.cooker.isSunlit();
    }
}
