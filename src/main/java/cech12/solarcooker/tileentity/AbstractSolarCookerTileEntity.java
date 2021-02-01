package cech12.solarcooker.tileentity;

import cech12.solarcooker.block.AbstractSolarCookerBlock;
import cech12.solarcooker.block.ReflectorBlock;
import cech12.solarcooker.block.SolarCookerBlock;
import cech12.solarcooker.config.ServerConfig;
import cech12.solarcooker.init.ModTags;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Block;
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
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d; //1.15
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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

    /** The current angle of the lid (between 0 and 1) */
    protected float lidAngle;
    /** The angle of the lid last tick */
    protected float prevLidAngle;
    /** The number of players currently using this cooker */
    protected int numPlayersUsing;
    /** The number of reflectors next to the cooker */
    protected int reflectorCount = 0;

    protected final IRecipeType<? extends AbstractCookingRecipe> specificRecipeType;
    private final Object2IntOpenHashMap<ResourceLocation> usedRecipes = new Object2IntOpenHashMap<>();

    public AbstractSolarCookerTileEntity(TileEntityType<?> tileTypeIn,
                                         IRecipeType<? extends AbstractCookingRecipe> specificRecipeTypeIn) {
        super(tileTypeIn);
        this.specificRecipeType = specificRecipeTypeIn;
    }

    protected AbstractCookingRecipe curRecipe;
    protected ItemStack failedMatch = ItemStack.EMPTY;

    private boolean hasShiningBlockAbove() {
        if (this.world != null && !this.world.isRemote) {
            BlockPos checkPos = this.pos.up();
            if (this.world.getBlockState(checkPos).propagatesSkylightDown(this.world, checkPos)) {
                for (int i = 0; i < 5; i++) {
                    checkPos = checkPos.up();
                    BlockState state = this.world.getBlockState(checkPos);
                    if (state.isIn(ModTags.Blocks.SOLAR_COOKER_SHINING)) {
                        return true;
                    }
                    if (!state.propagatesSkylightDown(this.world, checkPos)) {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public boolean isSunlit() {
        if (this.world != null) {
            if (!this.world.isRemote) {
                return this.hasShiningBlockAbove() || (
                        this.world.getDimension().hasSkyLight() //1.15
                        && this.world.isDaytime()
                        && !this.world.isRaining()
                        && this.world.canSeeSky(this.pos.up()));
            } else {
                //world.isDaytime() returns always true on client side
                return AbstractSolarCookerTileEntity.this.getBlockState().get(SolarCookerBlock.SUNLIT);
            }
        }
        return false;
    }

    public int getCookTime() {
        return cookTime;
    }

    public int getCookTimeTotal() {
        return this.cookTimeTotal;
    }

    @Override
    public void read(@Nonnull CompoundNBT nbtIn) { //1.15
        super.read(nbtIn); //1.15
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbtIn, this.items);
        this.cookTime = nbtIn.getInt("CookTime");
        this.cookTimeTotal = nbtIn.getInt("CookTimeTotal");
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        super.write(compound);
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        ItemStackHelper.saveAllItems(compound, this.items);
        return compound;
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.read(pkt.getNbtCompound()); //1.15
    }

    @Override
    public void tick() {
        if (this.world != null) {
            boolean dirty = false;
            this.calculateLidAngle();
            boolean isSunlit = this.isSunlit();
            if (isSunlit && !this.items.get(INPUT).isEmpty()) {
                AbstractCookingRecipe recipe = getRecipe();
                if (this.canSmelt(recipe)) {
                    this.cookTime++;
                    if (this.cookTime == this.cookTimeTotal) {
                        this.cookTime = 0;
                        this.cookTimeTotal = this.getRecipeCookTime();
                        if (!this.world.isRemote) {
                            this.smeltItem(recipe);
                            dirty = true;
                        }
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if (!isSunlit && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
            }

            boolean isBurning = this.cookTime > 0;
            if (!this.world.isRemote &&
                    (this.getBlockState().get(SolarCookerBlock.BURNING) != isBurning
                            || this.getBlockState().get(SolarCookerBlock.SUNLIT) != isSunlit)) {
                dirty = true;
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos)
                        .with(SolarCookerBlock.SUNLIT, isSunlit)
                        .with(SolarCookerBlock.BURNING, isBurning), 3);
            }
            if (dirty) {
                this.markDirty();
            }
        }
    }

    public boolean shouldLidBeOpen() {
        return this.numPlayersUsing > 0 || (this.canSmelt(getRecipe()) && this.isSunlit());
    }

    private void calculateLidAngle() {
        if (this.world != null) {
            this.prevLidAngle = this.lidAngle;

            boolean shouldLidBeOpen = shouldLidBeOpen();
            if (shouldLidBeOpen && this.lidAngle == 0.0F) {
                this.playSound(SoundEvents.BLOCK_CHEST_OPEN);
                if (!this.world.isRemote) {
                    this.markDirty();
                }
            }
            if (!shouldLidBeOpen && this.lidAngle > 0.0F || shouldLidBeOpen && this.lidAngle < 1.0F) {
                float f1 = this.lidAngle;
                if (shouldLidBeOpen) {
                    this.lidAngle += 0.1F;
                } else {
                    this.lidAngle -= 0.1F;
                }
                if (this.lidAngle > 1.0F) {
                    this.lidAngle = 1.0F;
                }
                if (this.lidAngle < 0.5F && f1 >= 0.5F) {
                    this.playSound(SoundEvents.BLOCK_CHEST_CLOSE);
                }
                if (this.lidAngle < 0.0F) {
                    this.lidAngle = 0.0F;
                }
            }
        }
    }

    /**
     * This must return true serverside before it is called clientside.
     */
    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            this.numPlayersUsing = type;
            return true;
        } else {
            return super.receiveClientEvent(id, type);
        }
    }

    @Override
    public void openInventory(PlayerEntity player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }
            ++this.numPlayersUsing;
            this.onOpenOrClose();
        }
    }

    @Override
    public void closeInventory(PlayerEntity player) {
        if (!player.isSpectator()) {
            --this.numPlayersUsing;
            this.onOpenOrClose();
        }
    }

    protected void onOpenOrClose() {
        Block block = this.getBlockState().getBlock();
        if (this.world != null && block instanceof SolarCookerBlock) {
            this.world.addBlockEvent(this.pos, block, 1, this.numPlayersUsing);
            //this.world.notifyNeighborsOfStateChange(this.pos, block);
        }
    }

    private void playSound(SoundEvent soundIn) {
        if (this.world != null && !this.world.isRemote) {
            double x = (double)this.pos.getX() + 0.5D;
            double y = (double)this.pos.getY() + 0.5D;
            double z = (double)this.pos.getZ() + 0.5D;
            this.world.playSound(null, x, y, z, soundIn, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        }
    }

    protected boolean canSmelt(@Nullable IRecipe<?> recipe) {
        if (!this.items.get(INPUT).isEmpty() && recipe != null) {
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
            ItemStack itemstack = this.items.get(INPUT);
            ItemStack itemstack1 = recipe.getRecipeOutput();
            ItemStack itemstack2 = this.items.get(OUTPUT);
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

    protected int getRecipeCookTime() {
        AbstractCookingRecipe rec = getRecipe();
        if (rec == null) {
            return 200;
        }
        this.checkForReflectors();
        double reflectorFactor = (this.reflectorCount > 0) ? 1 - ((1 - ServerConfig.MAX_REFLECTOR_TIME_FACTOR.get()) / 4.0D) * this.reflectorCount : 1;
        if (this.specificRecipeType.getClass().isInstance(rec.getType())) {
            return (int) (rec.getCookTime() * reflectorFactor);
        }
        return (int) (rec.getCookTime() * (ServerConfig.COOK_TIME_FACTOR.get() * reflectorFactor));
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

    private void checkForReflectors() {
        this.reflectorCount = 0;
        if (this.world != null) {
            BlockState state = this.world.getBlockState(this.pos);
            if (state.getBlock() instanceof AbstractSolarCookerBlock) {
                Direction facing = state.get(AbstractSolarCookerBlock.FACING);
                this.reflectorCount += countReflectorsOnSide(facing.rotateY());
                this.reflectorCount += countReflectorsOnSide(facing.rotateYCCW());
            }
        }
    }

    private int countReflectorsOnSide(Direction direction) {
        int count = 0;
        if (this.world != null) {
            BlockPos blockPos = this.pos.offset(direction);
            for (BlockPos position : new BlockPos[] {blockPos, blockPos.up()}) {
                BlockState state = this.world.getBlockState(position);
                if (state.getBlock() instanceof ReflectorBlock
                        && state.get(ReflectorBlock.HORIZONTAL_FACING) == direction.getOpposite()) {
                    count++;
                }
            }
        }
        return count;
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
            this.cookTimeTotal = this.getRecipeCookTime();
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
        if (recipe != null) {
            this.usedRecipes.addTo(recipe.getId(), 1);
        }
    }

    @Override
    @Nullable
    public IRecipe<?> getRecipeUsed() {
        return null;
    }

    public void func_235645_d_(PlayerEntity p_235645_1_) {
        List<IRecipe<?>> list = this.func_235640_a_(p_235645_1_.world, p_235645_1_.getPositionVec());
        p_235645_1_.unlockRecipes(list);
        this.usedRecipes.clear();
    }

    public List<IRecipe<?>> func_235640_a_(World p_235640_1_, Vec3d p_235640_2_) { //1.15
        List<IRecipe<?>> list = Lists.newArrayList();

        for(Object2IntMap.Entry<ResourceLocation> entry : this.usedRecipes.object2IntEntrySet()) {
            p_235640_1_.getRecipeManager().getRecipe(entry.getKey()).ifPresent((p_235642_4_) -> {
                list.add(p_235642_4_);
                func_235641_a_(p_235640_1_, p_235640_2_, entry.getIntValue(), ((AbstractCookingRecipe)p_235642_4_).getExperience());
            });
        }

        return list;
    }

    private static void func_235641_a_(World p_235641_0_, Vec3d p_235641_1_, int p_235641_2_, float p_235641_3_) { //1.15
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

    public void fillStackedContents(@Nonnull RecipeItemHelper helper) {
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
        for (LazyOptional<? extends IItemHandler> handler : handlers) handler.invalidate();
    }

    @OnlyIn(Dist.CLIENT)
    public float getLidAngle(float partialTicks) {
        if (this.world != null) {
            return MathHelper.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
        }
        return 0;
    }

}
