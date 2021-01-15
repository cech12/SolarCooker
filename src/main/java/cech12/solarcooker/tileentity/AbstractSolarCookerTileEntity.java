package cech12.solarcooker.tileentity;

import cech12.solarcooker.block.SolarCookerBlock;
import cech12.solarcooker.config.ServerConfig;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractSolarCookerTileEntity extends LockableTileEntity implements ISidedInventory, IRecipeHolder, IRecipeHelperPopulator, ITickableTileEntity {

    private static final int[] SLOTS = new int[]{0, 1};
    private static final int[] SLOTS_UP = new int[]{};

    protected static final int INPUT = 0;
    protected static final int OUTPUT = 1;

    protected NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    protected int cookTime;
    protected int cookTimeTotal;

    public final IIntArray cookerData = new IIntArray() {
        public int get(int index) {
            switch(index) {
                case 0:
                    return AbstractSolarCookerTileEntity.this.cookTime;
                case 1:
                    return AbstractSolarCookerTileEntity.this.cookTimeTotal;
                case 2:
                    return AbstractSolarCookerTileEntity.this.isSunlit() ? 1 : 0;
                default:
                    return 0;
            }
        }
        public void set(int index, int value) {
            switch(index) {
                case 0:
                    AbstractSolarCookerTileEntity.this.cookTime = value;
                    break;
                case 1:
                    AbstractSolarCookerTileEntity.this.cookTimeTotal = value;
            }

        }
        public int size() {
            return 3;
        }
    };

    protected final IRecipeType<? extends AbstractCookingRecipe> specificRecipeType;
    private final Object2IntOpenHashMap<ResourceLocation> field_214022_n = new Object2IntOpenHashMap<>();

    public AbstractSolarCookerTileEntity(TileEntityType<?> tileTypeIn,
                                         IRecipeType<? extends AbstractCookingRecipe> specificRecipeTypeIn) {
        super(tileTypeIn);
        this.specificRecipeType = specificRecipeTypeIn;
    }

    protected AbstractCookingRecipe curRecipe;
    protected ItemStack failedMatch = ItemStack.EMPTY;

    private boolean isSunlit() {
        return this.world != null
                && this.world.func_230315_m_().hasSkyLight()
                && this.world.isDaytime()
                && !this.world.isRaining()
                && this.world.canSeeSky(this.pos.up());
    }

    public void read(BlockState stateIn, CompoundNBT nbtIn) {
        super.read(stateIn, nbtIn);
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbtIn, this.items);
        this.cookTime = nbtIn.getInt("CookTime");
        this.cookTimeTotal = nbtIn.getInt("CookTimeTotal");
    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        ItemStackHelper.saveAllItems(compound, this.items);
        return compound;
    }

    @Override
    public void tick() {
        boolean dirty = false;
        if (this.world != null && !this.world.isRemote) {
            boolean wasBurning = this.world.getBlockState(this.pos).get(SolarCookerBlock.BURNING);
            boolean wasSunlit = this.world.getBlockState(this.pos).get(SolarCookerBlock.SUNLIT);
            boolean isSunlit = this.isSunlit();
            if (isSunlit && !this.items.get(INPUT).isEmpty()) {
                AbstractCookingRecipe recipe = getRecipe();
                if (this.canSmelt(recipe)) {
                    this.cookTime++;
                    if (this.cookTime == this.cookTimeTotal) {
                        this.cookTime = 0;
                        this.cookTimeTotal = this.getCookTime();
                        this.smeltItem(recipe);
                        dirty = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if (!isSunlit && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
            }

            boolean isBurning = this.cookTime > 0;
            if (wasBurning != isBurning || wasSunlit != isSunlit) {
                dirty = true;
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos)
                        .with(SolarCookerBlock.SUNLIT, isSunlit)
                        .with(SolarCookerBlock.BURNING, isBurning), 3);
            }
        }
        if (dirty) {
            this.markDirty();
        }
    }

    protected boolean canSmelt(@Nullable IRecipe<?> recipe) {
        if (!this.items.get(0).isEmpty() && recipe != null) {
            ItemStack recipeOutput = recipe.getRecipeOutput();
            if (!recipeOutput.isEmpty()) {
                ItemStack output = this.items.get(OUTPUT);
                if (output.isEmpty()) return true;
                else if (!output.isItemEqual(recipeOutput)) return false;
                else return output.getCount() + recipeOutput.getCount() <= output.getMaxStackSize();
            }
        }
        return false;
    }

    private void smeltItem(@Nullable IRecipe<?> recipe) {
        if (recipe != null && this.canSmelt(recipe)) {
            ItemStack itemstack = this.items.get(0);
            ItemStack itemstack1 = recipe.getRecipeOutput();
            ItemStack itemstack2 = this.items.get(1);
            if (itemstack2.isEmpty()) {
                this.items.set(1, itemstack1.copy());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.grow(itemstack1.getCount());
            }

            if (this.world != null && !this.world.isRemote) {
                this.setRecipeUsed(recipe);
            }

            itemstack.shrink(1);
        }
    }

    protected int getCookTime() {
        AbstractCookingRecipe rec = getRecipe();
        if (rec == null) {
            return 200;
        } else if (this.specificRecipeType.getClass().isInstance(rec.getType())) {
            return rec.getCookTime();
        }
        return (int) (rec.getCookTime() * ServerConfig.COOK_TIME_FACTOR.get());
    }

    @SuppressWarnings("unchecked")
    protected AbstractCookingRecipe getRecipe() {
        ItemStack input = this.getStackInSlot(INPUT);
        if (input.isEmpty() || input == failedMatch) {
            return null;
        }
        if (this.world != null && curRecipe != null && curRecipe.matches(this, world)) {
            return curRecipe;
        } else {
            AbstractCookingRecipe rec = null;
            if (this.world != null) {
                rec = this.world.getRecipeManager().getRecipe((IRecipeType<AbstractCookingRecipe>) this.specificRecipeType, this, this.world).orElse(null);
                if (rec == null && ServerConfig.VANILLA_RECIPES_ENABLED.get()) {
                    rec = this.world.getRecipeManager().getRecipes((IRecipeType<AbstractCookingRecipe>) ServerConfig.getRecipeType(), this, this.world)
                            .stream().filter(abstractCookingRecipe -> ServerConfig.isRecipeNotBlacklisted(abstractCookingRecipe.getId())).findFirst().orElse(null);
                }
            }
            if (rec == null) {
                failedMatch = input;
            } else {
                failedMatch = ItemStack.EMPTY;
            }
            return curRecipe = rec;
        }
    }

    @Override
    @Nonnull
    public int[] getSlotsForFace(@Nonnull Direction side) {
        if (side == Direction.UP) {
            return SLOTS_UP;
        }
        return SLOTS;
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side.
     */
    @Override
    public boolean canInsertItem(int index, @Nonnull ItemStack itemStackIn, @Nullable Direction direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
    @Override
    public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nullable Direction direction) {
        return direction != Direction.UP && index == OUTPUT;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the stack in the given slot.
     */
    @Override
    @Nonnull
    public ItemStack getStackInSlot(int index) {
        return this.items.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Override
    @Nonnull
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.items, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Override
    @Nonnull
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.items, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.items.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.items.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
        if (index == 0 && !flag) {
            this.cookTimeTotal = this.getCookTime();
            this.cookTime = 0;
            this.markDirty();
        }
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    @Override
    public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {
        if (this.world != null && this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
        return index == INPUT;
    }

    @Override
    public void clear() {
        this.items.clear();
    }

    @Override
    public void setRecipeUsed(@Nullable IRecipe<?> recipe) {
        //TODO
        /*
        if (recipe != null) {
            ResourceLocation resourcelocation = recipe.getId();
            this.field_214022_n.addTo(resourcelocation, 1);
        }
         */
    }

    @Override
    @Nullable
    public IRecipe<?> getRecipeUsed() {
        //TODO
        return null;
    }

    public void func_235645_d_(PlayerEntity p_235645_1_) {
        List<IRecipe<?>> list = this.func_235640_a_(p_235645_1_.world, p_235645_1_.getPositionVec());
        p_235645_1_.unlockRecipes(list);
        this.field_214022_n.clear();
    }

    public List<IRecipe<?>> func_235640_a_(World p_235640_1_, Vector3d p_235640_2_) {
        List<IRecipe<?>> list = Lists.newArrayList();

        for(Object2IntMap.Entry<ResourceLocation> entry : this.field_214022_n.object2IntEntrySet()) {
            p_235640_1_.getRecipeManager().getRecipe(entry.getKey()).ifPresent((p_235642_4_) -> {
                list.add(p_235642_4_);
                func_235641_a_(p_235640_1_, p_235640_2_, entry.getIntValue(), ((AbstractCookingRecipe)p_235642_4_).getExperience());
            });
        }

        return list;
    }

    private static void func_235641_a_(World p_235641_0_, Vector3d p_235641_1_, int p_235641_2_, float p_235641_3_) {
        int i = MathHelper.floor((float)p_235641_2_ * p_235641_3_);
        float f = MathHelper.frac((float)p_235641_2_ * p_235641_3_);
        if (f != 0.0F && Math.random() < (double)f) {
            ++i;
        }

        while(i > 0) {
            int j = ExperienceOrbEntity.getXPSplit(i);
            i -= j;
            p_235641_0_.addEntity(new ExperienceOrbEntity(p_235641_0_, p_235641_1_.x, p_235641_1_.y, p_235641_1_.z, j));
        }

    }

    public void fillStackedContents(RecipeItemHelper helper) {
        for(ItemStack itemstack : this.items) {
            helper.accountStack(itemstack);
        }
    }

    LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.NORTH);

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (!this.removed && facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == Direction.UP)
                return handlers[0].cast();
            else
                return handlers[1].cast();
        }
        return super.getCapability(capability, facing);
    }

    /**
     * invalidates a tile entity
     */
    @Override
    public void remove() {
        super.remove();
        for (int x = 0; x < handlers.length; x++)
            handlers[x].invalidate();
    }

}
