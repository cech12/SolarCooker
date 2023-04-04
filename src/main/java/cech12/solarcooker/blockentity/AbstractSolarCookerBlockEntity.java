package cech12.solarcooker.blockentity;

import cech12.solarcooker.block.AbstractSolarCookerBlock;
import cech12.solarcooker.block.ReflectorBlock;
import cech12.solarcooker.block.SolarCookerBlock;
import cech12.solarcooker.config.ServerConfig;
import cech12.solarcooker.init.ModTags;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractSolarCookerBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, RecipeHolder, StackedContentsCompatible, LidBlockEntity {

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

    protected final RecipeType<? extends AbstractCookingRecipe> specificRecipeType;
    private final Object2IntOpenHashMap<ResourceLocation> usedRecipes = new Object2IntOpenHashMap<>();

    public AbstractSolarCookerBlockEntity(BlockEntityType<?> tileTypeIn, BlockPos pos, BlockState state,
                                          RecipeType<? extends AbstractCookingRecipe> specificRecipeTypeIn) {
        super(tileTypeIn, pos, state);
        this.specificRecipeType = specificRecipeTypeIn;
    }

    protected AbstractCookingRecipe curRecipe;
    protected ItemStack failedMatch = ItemStack.EMPTY;

    private boolean hasShiningBlockAbove() {
        if (this.level != null && !this.level.isClientSide) {
            BlockPos checkPos = this.worldPosition.above();
            if (this.level.getBlockState(checkPos).propagatesSkylightDown(this.level, checkPos)) {
                for (int i = 0; i < 5; i++) {
                    checkPos = checkPos.above();
                    BlockState state = this.level.getBlockState(checkPos);
                    if (state.is(ModTags.Blocks.SOLAR_COOKER_SHINING)) {
                        return true;
                    }
                    if (!state.propagatesSkylightDown(this.level, checkPos)) {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public boolean isSunlit() {
        if (this.level != null) {
            if (!this.level.isClientSide) {
                return this.hasShiningBlockAbove() || (
                        this.level.dimensionType().hasSkyLight()
                        && this.level.isDay()
                        && !this.level.isRaining()
                        && this.level.canSeeSky(this.worldPosition.above()));
            } else {
                //world.isDaytime() returns always true on client side
                return AbstractSolarCookerBlockEntity.this.getBlockState().getValue(SolarCookerBlock.SUNLIT);
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
    public void load(@Nonnull CompoundTag nbtIn) {
        super.load(nbtIn);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbtIn, this.items);
        this.cookTime = nbtIn.getInt("CookTime");
        this.cookTimeTotal = nbtIn.getInt("CookTimeTotal");
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        ContainerHelper.saveAllItems(compound, this.items);
    }

    @Override
    @Nonnull
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AbstractSolarCookerBlockEntity entity) {
        if (level != null) {
            boolean dirty = false;
            entity.calculateLidAngle();
            boolean isSunlit = entity.isSunlit();
            if (isSunlit && !entity.items.get(INPUT).isEmpty()) {
                AbstractCookingRecipe recipe = entity.getRecipe();
                if (entity.canSmelt(recipe)) {
                    entity.cookTime++;
                    if (entity.cookTime == entity.cookTimeTotal) {
                        entity.cookTime = 0;
                        entity.cookTimeTotal = entity.getRecipeCookTime();
                        if (!level.isClientSide) {
                            entity.smeltItem(recipe);
                            dirty = true;
                        }
                    }
                } else {
                    entity.cookTime = 0;
                }
            } else if (!isSunlit && entity.cookTime > 0) {
                entity.cookTime = Mth.clamp(entity.cookTime - 2, 0, entity.cookTimeTotal);
            }

            boolean isBurning = entity.cookTime > 0;
            if (!level.isClientSide &&
                    (entity.getBlockState().getValue(SolarCookerBlock.BURNING) != isBurning
                            || entity.getBlockState().getValue(SolarCookerBlock.SUNLIT) != isSunlit)) {
                dirty = true;
                entity.level.setBlock(entity.worldPosition, entity.level.getBlockState(entity.worldPosition)
                        .setValue(SolarCookerBlock.SUNLIT, isSunlit)
                        .setValue(SolarCookerBlock.BURNING, isBurning), 3);
            }
            if (dirty) {
                entity.setChanged();
            }
        }
    }

    public boolean shouldLidBeOpen() {
        return this.numPlayersUsing > 0 || (this.canSmelt(getRecipe()) && this.isSunlit());
    }

    private void calculateLidAngle() {
        if (this.level != null) {
            this.prevLidAngle = this.lidAngle;

            boolean shouldLidBeOpen = shouldLidBeOpen();
            if (shouldLidBeOpen && this.lidAngle == 0.0F) {
                this.playSound(SoundEvents.CHEST_OPEN);
                if (!this.level.isClientSide) {
                    this.setChanged();
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
                    this.playSound(SoundEvents.CHEST_CLOSE);
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
    public boolean triggerEvent(int id, int type) {
        if (id == 1) {
            this.numPlayersUsing = type;
            return true;
        } else {
            return super.triggerEvent(id, type);
        }
    }

    @Override
    public void startOpen(Player player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }
            ++this.numPlayersUsing;
            this.onOpenOrClose();
        }
    }

    @Override
    public void stopOpen(Player player) {
        if (!player.isSpectator()) {
            --this.numPlayersUsing;
            this.onOpenOrClose();
        }
    }

    protected void onOpenOrClose() {
        Block block = this.getBlockState().getBlock();
        if (this.level != null && block instanceof SolarCookerBlock) {
            this.level.blockEvent(this.worldPosition, block, 1, this.numPlayersUsing);
            //this.world.notifyNeighborsOfStateChange(this.pos, block);
        }
    }

    private void playSound(SoundEvent soundIn) {
        if (this.level != null && !this.level.isClientSide) {
            double x = (double)this.worldPosition.getX() + 0.5D;
            double y = (double)this.worldPosition.getY() + 0.5D;
            double z = (double)this.worldPosition.getZ() + 0.5D;
            this.level.playSound(null, x, y, z, soundIn, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
        }
    }

    protected boolean canSmelt(@Nullable Recipe<?> recipe) {
        if (!this.items.get(INPUT).isEmpty() && recipe != null) {
            ItemStack recipeOutput = ((Recipe<WorldlyContainer>) recipe).assemble(this, this.getLevel().registryAccess());
            if (!recipeOutput.isEmpty()) {
                ItemStack output = this.items.get(OUTPUT);
                if (output.isEmpty()) return true;
                else if (!output.sameItem(recipeOutput)) return false;
                else return output.getCount() + recipeOutput.getCount() <= output.getMaxStackSize();
            }
        }
        return false;
    }

    private void smeltItem(@Nullable Recipe<?> recipe) {
        if (recipe != null && this.canSmelt(recipe)) {
            ItemStack itemstack = this.items.get(INPUT);
            ItemStack itemstack1 = ((Recipe<WorldlyContainer>) recipe).assemble(this, this.getLevel().registryAccess());
            ItemStack itemstack2 = this.items.get(OUTPUT);
            if (itemstack2.isEmpty()) {
                this.items.set(1, itemstack1.copy());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.grow(itemstack1.getCount());
            }

            if (this.level != null && !this.level.isClientSide) {
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
            return (int) (rec.getCookingTime() * reflectorFactor);
        }
        return (int) (rec.getCookingTime() * (ServerConfig.COOK_TIME_FACTOR.get() * reflectorFactor));
    }

    @SuppressWarnings("unchecked")
    protected AbstractCookingRecipe getRecipe() {
        ItemStack input = this.getItem(INPUT);
        if (input.isEmpty() || input == failedMatch) {
            return null;
        }
        if (this.level != null && curRecipe != null && curRecipe.matches(this, level)) {
            return curRecipe;
        } else {
            AbstractCookingRecipe rec = null;
            if (this.level != null) {
                rec = this.level.getRecipeManager().getRecipeFor((RecipeType<AbstractCookingRecipe>) this.specificRecipeType, this, this.level).orElse(null);
                if (rec == null && ServerConfig.VANILLA_RECIPES_ENABLED.get()) {
                    rec = this.level.getRecipeManager().getRecipesFor((RecipeType<AbstractCookingRecipe>) ServerConfig.getRecipeType(), this, this.level)
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
        if (this.level != null) {
            BlockState state = this.level.getBlockState(this.worldPosition);
            if (state.getBlock() instanceof AbstractSolarCookerBlock) {
                Direction facing = state.getValue(AbstractSolarCookerBlock.FACING);
                this.reflectorCount += countReflectorsOnSide(facing.getClockWise());
                this.reflectorCount += countReflectorsOnSide(facing.getCounterClockWise());
            }
        }
    }

    private int countReflectorsOnSide(Direction direction) {
        int count = 0;
        if (this.level != null) {
            BlockPos blockPos = this.worldPosition.relative(direction);
            for (BlockPos position : new BlockPos[] {blockPos, blockPos.above()}) {
                BlockState state = this.level.getBlockState(position);
                if (state.getBlock() instanceof ReflectorBlock
                        && ReflectorBlock.isFacingTo(state, direction.getOpposite())) {
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
    public boolean canPlaceItemThroughFace(int index, @Nonnull ItemStack itemStackIn, @Nullable Direction direction) {
        return this.canPlaceItem(index, itemStackIn);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
    @Override
    public boolean canTakeItemThroughFace(int index, @Nonnull ItemStack stack, @Nullable Direction direction) {
        return direction != Direction.UP && index == OUTPUT;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getContainerSize() {
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
    public ItemStack getItem(int index) {
        return this.items.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Override
    @Nonnull
    public ItemStack removeItem(int index, int count) {
        return ContainerHelper.removeItem(this.items, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Override
    @Nonnull
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(this.items, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
    public void setItem(int index, ItemStack stack) {
        ItemStack itemstack = this.items.get(index);
        boolean flag = !stack.isEmpty() && stack.sameItem(itemstack) && ItemStack.tagMatches(stack, itemstack);
        this.items.set(index, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        if (index == 0 && !flag) {
            this.cookTimeTotal = this.getRecipeCookTime();
            this.cookTime = 0;
            this.setChanged();
        }
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    @Override
    public boolean stillValid(@Nonnull Player player) {
        if (this.level != null && this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    @Override
    public boolean canPlaceItem(int index, @Nonnull ItemStack stack) {
        return index == INPUT;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            this.usedRecipes.addTo(recipe.getId(), 1);
        }
    }

    @Override
    @Nullable
    public Recipe<?> getRecipeUsed() {
        return null;
    }

    public void awardUsedRecipesAndPopExperience(Player p_235645_1_) {
        List<Recipe<?>> list = this.getRecipesToAwardAndPopExperience(p_235645_1_.level, p_235645_1_.position());
        p_235645_1_.awardRecipes(list);
        this.usedRecipes.clear();
    }

    public List<Recipe<?>> getRecipesToAwardAndPopExperience(Level p_235640_1_, Vec3 p_235640_2_) {
        List<Recipe<?>> list = Lists.newArrayList();

        for(Object2IntMap.Entry<ResourceLocation> entry : this.usedRecipes.object2IntEntrySet()) {
            p_235640_1_.getRecipeManager().byKey(entry.getKey()).ifPresent((p_235642_4_) -> {
                list.add(p_235642_4_);
                createExperience(p_235640_1_, p_235640_2_, entry.getIntValue(), ((AbstractCookingRecipe)p_235642_4_).getExperience());
            });
        }

        return list;
    }

    private static void createExperience(Level p_235641_0_, Vec3 p_235641_1_, int p_235641_2_, float p_235641_3_) {
        int i = Mth.floor((float)p_235641_2_ * p_235641_3_);
        float f = Mth.frac((float)p_235641_2_ * p_235641_3_);
        if (f != 0.0F && Math.random() < (double)f) {
            ++i;
        }

        while(i > 0) {
            int j = ExperienceOrb.getExperienceValue(i);
            i -= j;
            p_235641_0_.addFreshEntity(new ExperienceOrb(p_235641_0_, p_235641_1_.x, p_235641_1_.y, p_235641_1_.z, j));
        }

    }

    public void fillStackedContents(@Nonnull StackedContents helper) {
        for(ItemStack itemstack : this.items) {
            helper.accountStack(itemstack);
        }
    }

    LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.NORTH);

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (!this.remove && facing != null && capability == ForgeCapabilities.ITEM_HANDLER) {
            if (facing == Direction.UP)
                return handlers[0].cast();
            else
                return handlers[1].cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        for (LazyOptional<? extends IItemHandler> handler : handlers) handler.invalidate();
    }

    @Override
    public float getOpenNess(float partialTicks) {
        if (this.level != null) {
            return Mth.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
        }
        return 0;
    }

}
