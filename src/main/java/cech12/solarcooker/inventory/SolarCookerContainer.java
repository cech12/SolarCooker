package cech12.solarcooker.inventory;

import cech12.solarcooker.config.ServerConfig;
import cech12.solarcooker.tileentity.AbstractSolarCookerTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

import static cech12.solarcooker.api.inventory.ContainerTypes.SOLAR_COOKER;

public class SolarCookerContainer extends Container {
    private final IRecipeType<? extends AbstractCookingRecipe> specificRecipeType;
    private final IInventory cookerInventory;
    private final IIntArray cookerData;
    protected final World world;

    public SolarCookerContainer(IRecipeType<? extends AbstractCookingRecipe> specificRecipeTypeIn, int id,
                                PlayerInventory playerInventoryIn, IInventory cookerInventoryIn,
                                IIntArray cookerDataIn) {
        super(SOLAR_COOKER, id);
        this.specificRecipeType = specificRecipeTypeIn;
        assertInventorySize(cookerInventoryIn, 2);
        assertIntArraySize(cookerDataIn, 2);
        this.cookerInventory = cookerInventoryIn;
        this.cookerData = cookerDataIn;
        this.world = playerInventoryIn.player.world;

        //add cooker inventory slots
        this.addSlot(new Slot(cookerInventoryIn, 0, 56, 17));
        this.addSlot(new SolarCookerResultSlot(playerInventoryIn.player, cookerInventoryIn, 1, 116, 35));

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

    public SolarCookerContainer(IRecipeType<? extends AbstractCookingRecipe> specificRecipeTypeIn, int id,
                                PlayerInventory playerInventoryIn, BlockPos pos) {
        this(specificRecipeTypeIn, id, playerInventoryIn, (AbstractSolarCookerTileEntity) playerInventoryIn.player.world.getTileEntity(pos), ((AbstractSolarCookerTileEntity) playerInventoryIn.player.world.getTileEntity(pos)).cookerData);
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity playerIn) {
        return this.cookerInventory.isUsableByPlayer(playerIn);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    @Override
    @Nonnull
    public ItemStack transferStackInSlot(@Nonnull PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 1) {
                if (!this.mergeItemStack(itemstack1, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemstack1, itemstack);
            } else if (index != 0) {
                if (this.hasRecipe(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 2 && index < 29) {
                    if (!this.mergeItemStack(itemstack1, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 29 && index < 38 && !this.mergeItemStack(itemstack1, 2, 29, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 2, 38, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    @Override
    public void putStackInSlot(int slotID, ItemStack stack) {
        super.putStackInSlot(slotID, stack);
    }

    protected boolean hasRecipe(ItemStack stack) {
        if (this.world != null) {
            if (this.world.getRecipeManager().getRecipe(this.specificRecipeType, new Inventory(stack), this.world).isPresent()) {
                return true;
            }
            if (ServerConfig.VANILLA_RECIPES_ENABLED.get()) {
                return this.world.getRecipeManager().getRecipes(ServerConfig.getRecipeType(), new Inventory(stack), this.world)
                        .stream().anyMatch(abstractCookingRecipe -> ServerConfig.isRecipeNotBlacklisted(abstractCookingRecipe.getId()));
            }
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public int getCookProgressionScaled() {
        int i = this.cookerData.get(0);
        int j = this.cookerData.get(1);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isBurning() {
        return this.cookerData.get(0) > 0;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isSunlit() {
        return this.cookerData.get(2) > 0;
    }
}
