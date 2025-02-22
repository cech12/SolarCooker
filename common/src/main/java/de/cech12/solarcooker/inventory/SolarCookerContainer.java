package de.cech12.solarcooker.inventory;

import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.blockentity.SolarCookerBlockEntity;
import de.cech12.solarcooker.platform.Services;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class SolarCookerContainer extends AbstractContainerMenu {
    private final RecipeType<? extends AbstractCookingRecipe> specificRecipeType;
    private final Container cooker;
    private final ContainerData data;
    protected final Level level;

    public SolarCookerContainer(RecipeType<? extends AbstractCookingRecipe> specificRecipeTypeIn, int id,
                                Inventory playerInventoryIn, Container cooker, ContainerData data) {
        super(Constants.SOLAR_COOKER_MENU_TYPE.get(), id);
        this.specificRecipeType = specificRecipeTypeIn;
        AbstractContainerMenu.checkContainerSize(cooker, 4);
        this.cooker = cooker;
        this.data = data;
        cooker.startOpen(playerInventoryIn.player);
        this.level = playerInventoryIn.player.level();

        //add cooker inventory slots
        this.addSlot(new Slot(cooker, 0, 56, 17));
        this.addSlot(new SolarCookerResultSlot(playerInventoryIn.player, cooker, 1, 116, 35));
        //add reflector slots
        this.addSlot(new SolarCookerReflectorSlot(cooker, 2, 153, 7));
        this.addSlot(new SolarCookerReflectorSlot(cooker, 3, 153, 25));

        //add player inventory
        for(int playerInvRow = 0; playerInvRow < 3; ++playerInvRow) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInventoryIn, j1 + playerInvRow * 9 + 9, 8 + j1 * 18, 84 + playerInvRow * 18));
            }
        }
        for(int playerHotbarSlot = 0; playerHotbarSlot < 9; ++playerHotbarSlot) {
            this.addSlot(new Slot(playerInventoryIn, playerHotbarSlot, 8 + playerHotbarSlot * 18, 142));
        }

        this.addDataSlots(this.data);
    }

    public SolarCookerContainer(RecipeType<? extends AbstractCookingRecipe> specificRecipeTypeIn, int id, Inventory playerInventoryIn) {
        this(specificRecipeTypeIn, id, playerInventoryIn, new SimpleContainer(4), new SimpleContainerData(3));
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
        if (this.level != null && this.level.getServer() != null) {
            SingleRecipeInput recipeInput = new SingleRecipeInput(stack);
            if (this.level.getServer().getRecipeManager().getRecipeFor(this.specificRecipeType, recipeInput, this.level).isPresent()) {
                return true;
            }
            if (Services.CONFIG.areVanillaRecipesEnabled()) {
                return this.level.getServer().getRecipeManager().getRecipes().stream()
                        .filter(recipe -> recipe.value().getType() == Services.CONFIG.getRecipeType())
                        .filter(recipe -> recipe.value() instanceof AbstractCookingRecipe)
                        .map(recipe -> (RecipeHolder<AbstractCookingRecipe>) recipe)
                        .filter(recipe -> recipe.value().matches(recipeInput, this.level))
                        .anyMatch(recipe -> Services.CONFIG.isRecipeAllowed(recipe.id().location()));
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

    public int getCookProgressionScaled() {
        int i = this.data.get(SolarCookerBlockEntity.CONTAINER_COOK_TIME);
        int j = this.data.get(SolarCookerBlockEntity.CONTAINER_COOK_TIME_TOTAL);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    public boolean isBurning() {
        return this.data.get(SolarCookerBlockEntity.CONTAINER_COOK_TIME) > 0;
    }

    public boolean isSunlit() {
        return this.data.get(SolarCookerBlockEntity.CONTAINER_IS_SUNLIT) > 0;
    }

    public Container getContainer() {
        return this.cooker;
    }
}
