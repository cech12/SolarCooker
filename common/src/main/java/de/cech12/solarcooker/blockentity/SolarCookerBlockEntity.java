package de.cech12.solarcooker.blockentity;

import com.google.common.collect.Lists;
import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.ModTags;
import de.cech12.solarcooker.block.SolarCookerBlock;
import de.cech12.solarcooker.inventory.SolarCookerContainer;
import de.cech12.solarcooker.platform.Services;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SolarCookerBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, RecipeCraftingHolder, StackedContentsCompatible, LidBlockEntity {

    public static final int CONTAINER_IS_SUNLIT = 0;
    public static final int CONTAINER_COOK_TIME = 1;
    public static final int CONTAINER_COOK_TIME_TOTAL = 2;

    public static final int INPUT = 0;
    public static final int OUTPUT = 1;
    public static final int REFLECTOR_LEFT = 2;
    public static final int REFLECTOR_RIGHT = 3;

    private static final int[] SLOTS = new int[]{INPUT, OUTPUT};
    private static final int[] SLOTS_UP = new int[]{};
    private static final int[] SLOTS_REFLECTORS = new int[]{REFLECTOR_LEFT, REFLECTOR_RIGHT};

    protected NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
    protected int cookTime;
    protected int cookTimeTotal;

    protected boolean reflectorLeft;
    protected boolean reflectorRight;

    protected final RecipeType<? extends AbstractCookingRecipe> specificRecipeType;
    private final ContainerOpenersCounter openersCounter;
    private final ChestLidController chestLidController;
    private boolean isLidOpen = false;
    private final Object2IntOpenHashMap<ResourceKey<Recipe<?>>> usedRecipes = new Object2IntOpenHashMap<>();

    public SolarCookerBlockEntity(BlockEntityType<?> tileTypeIn, BlockPos pos, BlockState state,
                                  RecipeType<? extends AbstractCookingRecipe> specificRecipeTypeIn) {
        super(tileTypeIn, pos, state);
        this.specificRecipeType = specificRecipeTypeIn;
        this.openersCounter = new ContainerOpenersCounter() {
            @Override
            protected void onOpen(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state) {
                if (level.getBlockEntity(pos) instanceof SolarCookerBlockEntity blockEntity && !blockEntity.shouldLidBeOpen(0)) {
                    playSound(level, pos, SoundEvents.CHEST_OPEN);
                }
            }

            @Override
            protected void onClose(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state) {
                if (level.getBlockEntity(pos) instanceof SolarCookerBlockEntity blockEntity && !blockEntity.shouldLidBeOpen(0)) {
                    playSound(level, pos, SoundEvents.CHEST_CLOSE);
                }
            }

            @Override
            protected void openerCountChanged(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, int unknown, int numPlayerUsing) {
                signalOpenCount(level, pos, state, numPlayerUsing, false);
            }

            @Override
            protected boolean isOwnContainer(@Nonnull Player player) {
                if (!(player.containerMenu instanceof SolarCookerContainer)) {
                    return false;
                } else {
                    Container container = ((SolarCookerContainer) player.containerMenu).getContainer();
                    return container == SolarCookerBlockEntity.this || container instanceof CompoundContainer && ((CompoundContainer) container).contains(SolarCookerBlockEntity.this);
                }
            }
        };
        this.chestLidController = new ChestLidController();
    }

    public SolarCookerBlockEntity(BlockPos pos, BlockState state) {
        this(Constants.SOLAR_COOKER_ENTITY_TYPE.get(), pos, state, Constants.SOLAR_COOKING_RECIPE_TYPE.get());
    }

    protected RecipeHolder<? extends AbstractCookingRecipe> curRecipe;
    protected ItemStack failedMatch = ItemStack.EMPTY;

    protected final ContainerData dataAccess = new ContainerData() {
        public int get(int index) {
            return switch (index) {
                case CONTAINER_IS_SUNLIT -> SolarCookerBlockEntity.this.isSunlit() ? 1 : 0;
                case CONTAINER_COOK_TIME -> SolarCookerBlockEntity.this.cookTime;
                case CONTAINER_COOK_TIME_TOTAL -> SolarCookerBlockEntity.this.cookTimeTotal;
                default -> 0;
            };
        }

        public void set(int index, int value) {
            switch (index) {
                //case CONTAINER_IS_SUNLIT: break; //do nothing
                case CONTAINER_COOK_TIME:
                    SolarCookerBlockEntity.this.cookTime = value;
                    break;
                case CONTAINER_COOK_TIME_TOTAL:
                    SolarCookerBlockEntity.this.cookTimeTotal = value;
            }
        }

        public int getCount() {
            return 3;
        }
    };

    @Override
    @Nonnull
    protected Component getDefaultName() {
        return Component.translatable("block.solarcooker.solar_cooker");
    }

    @Override
    @Nonnull
    protected AbstractContainerMenu createMenu(int id, @Nonnull Inventory player) {
        return new SolarCookerContainer(specificRecipeType, id, player, this, this.dataAccess);
    }

    private boolean hasShiningBlockAbove() {
        if (this.level != null && !this.level.isClientSide) {
            BlockPos checkPos = this.worldPosition.above();
            if (this.level.getBlockState(checkPos).propagatesSkylightDown()) {
                for (int i = 0; i < 5; i++) {
                    checkPos = checkPos.above();
                    BlockState state = this.level.getBlockState(checkPos);
                    if (state.is(ModTags.Blocks.SOLAR_COOKER_SHINING)) {
                        return true;
                    }
                    if (!state.propagatesSkylightDown()) {
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
                return SolarCookerBlockEntity.this.getBlockState().getValue(SolarCookerBlock.SUNLIT);
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
    protected void loadAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
        super.loadAdditional(compound, provider);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, this.items, provider);
        this.cookTime = compound.getInt("CookTime");
        this.cookTimeTotal = compound.getInt("CookTimeTotal");
        updateShouldLidBeOpen(this.openersCounter.getOpenerCount());
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        ContainerHelper.saveAllItems(compound, this.items, provider);
    }

    @Override
    @Nonnull
    public CompoundTag getUpdateTag(@Nonnull HolderLookup.Provider provider) {
        return this.saveWithoutMetadata(provider);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    //@Override //overrides a Forge / Neoforge method ?! TODO
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        this.loadAdditional(pkt.getTag(), lookupProvider);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SolarCookerBlockEntity entity) {
        if (level != null) {
            boolean dirty = false;
            entity.updateReflectorStates();
            boolean isSunlit = entity.isSunlit();
            if (isSunlit && !entity.items.get(INPUT).isEmpty()) {
                RecipeHolder<? extends AbstractCookingRecipe> recipe = entity.getRecipe();
                if (entity.canSmelt(level.registryAccess(), recipe)) {
                    entity.cookTime++;
                    if (entity.cookTime == entity.cookTimeTotal) {
                        entity.cookTime = 0;
                        entity.cookTimeTotal = entity.getRecipeCookTime();
                        if (!level.isClientSide) {
                            entity.smeltItem(level.registryAccess(), recipe);
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
                    (state.getValue(SolarCookerBlock.BURNING) != isBurning
                            || state.getValue(SolarCookerBlock.SUNLIT) != isSunlit
                            || state.getValue(SolarCookerBlock.LEFT_REFLECTOR) != entity.reflectorLeft
                            || state.getValue(SolarCookerBlock.RIGHT_REFLECTOR) != entity.reflectorRight)) {
                dirty = true;
                level.setBlock(entity.worldPosition, state
                        .setValue(SolarCookerBlock.SUNLIT, isSunlit)
                        .setValue(SolarCookerBlock.BURNING, isBurning)
                        .setValue(SolarCookerBlock.LEFT_REFLECTOR, entity.reflectorLeft)
                        .setValue(SolarCookerBlock.RIGHT_REFLECTOR, entity.reflectorRight), 3);
            }
            if (dirty) {
                entity.setChanged();
                signalOpenCount(level, pos, state, entity.openersCounter.getOpenerCount(), true);
            }
        }
    }

    public boolean shouldLidBeOpen(int numPlayersUsing) {
        boolean recipeActive = this.cookTime > 0 || (this.getLevel() != null && (this.canSmelt(this.getLevel().registryAccess(), getRecipe())));
        return numPlayersUsing > 0 || (recipeActive && this.isSunlit());
    }

    public void updateShouldLidBeOpen(int numPlayersUsing) {
        this.isLidOpen = shouldLidBeOpen(numPlayersUsing);
        this.chestLidController.shouldBeOpen(this.isLidOpen);
    }

    public static void lidAnimateTick(Level level, BlockPos pos, BlockState state, SolarCookerBlockEntity blockEntity) {
        blockEntity.chestLidController.tickLid();
        blockEntity.updateReflectorStates();
    }

    /**
     * This must return true serverside before it is called clientside.
     */
    @Override
    public boolean triggerEvent(int id, int numPlayersUsing) {
        if (id == 1) {
            updateShouldLidBeOpen(numPlayersUsing);
            return true;
        } else {
            return super.triggerEvent(id, numPlayersUsing);
        }
    }

    @Override
    public void startOpen(@Nonnull Player player) {
        if (!this.remove && !player.isSpectator() && this.getLevel() != null) {
            this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public void stopOpen(@Nonnull Player player) {
        if (!this.remove && !player.isSpectator() && this.getLevel() != null) {
            this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    public void recheckOpen() {
        if (!this.remove && this.getLevel() != null) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    public static void signalOpenCount(Level level, BlockPos pos, BlockState state, int numPlayersUsing, boolean playSound) {
        //play sound if state changed and no player is involved
        if (playSound && numPlayersUsing < 1 && level.getBlockEntity(pos) instanceof SolarCookerBlockEntity blockEntity) {
            if (blockEntity.isLidOpen != blockEntity.shouldLidBeOpen(numPlayersUsing)) {
                if (blockEntity.isLidOpen) {
                    playSound(blockEntity.level, blockEntity.getBlockPos(), SoundEvents.CHEST_CLOSE);
                } else {
                    playSound(blockEntity.level, blockEntity.getBlockPos(), SoundEvents.CHEST_OPEN);
                }
            }
        }
        Block block = state.getBlock();
        level.blockEvent(pos, block, 1, numPlayersUsing);
    }

    public static void playSound(Level level, BlockPos pos, SoundEvent soundEvent) {
        if (level != null && !level.isClientSide) {
            double x = (double)pos.getX() + 0.5D;
            double y = (double)pos.getY() + 0.5D;
            double z = (double)pos.getZ() + 0.5D;
            level.playSound(null, x, y, z, soundEvent, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
        }
    }

    protected boolean canSmelt(RegistryAccess registryAccess, @Nullable RecipeHolder<?> recipe) {
        if (!this.items.get(INPUT).isEmpty() && recipe != null && recipe.value() instanceof AbstractCookingRecipe cookingRecipe) {
            ItemStack recipeOutput = cookingRecipe.assemble(new SingleRecipeInput(this.items.get(INPUT)), registryAccess);
            if (!recipeOutput.isEmpty()) {
                ItemStack output = this.items.get(OUTPUT);
                if (output.isEmpty()) return true;
                else if (!ItemStack.isSameItem(output, recipeOutput)) return false;
                else return output.getCount() + recipeOutput.getCount() <= output.getMaxStackSize();
            }
        }
        return false;
    }

    private void smeltItem(RegistryAccess registryAccess, @Nullable RecipeHolder<?> recipe) {
        if (recipe != null && this.canSmelt(registryAccess, recipe) && recipe.value() instanceof AbstractCookingRecipe cookingRecipe) {
            ItemStack itemstack = this.items.get(INPUT);
            ItemStack itemstack1 = cookingRecipe.assemble(new SingleRecipeInput(itemstack), registryAccess);
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
        RecipeHolder<? extends AbstractCookingRecipe> rec = getRecipe();
        if (rec == null) {
            return 200;
        }
        int reflectorCount = this.getReflectorCount();
        double reflectorFactor = (reflectorCount > 0) ? 1D - ((1D - Services.CONFIG.getMaxReflectorTimeFactor()) / (double) SLOTS_REFLECTORS.length) * (double) reflectorCount : 1D;
        if (this.specificRecipeType.getClass().isInstance(rec.value().getType())) {
            return (int) (rec.value().cookingTime() * reflectorFactor);
        }
        return (int) (rec.value().cookingTime() * (Services.CONFIG.getCookTimeFactor() * reflectorFactor));
    }

    @SuppressWarnings("unchecked")
    protected RecipeHolder<? extends AbstractCookingRecipe> getRecipe() {
        ItemStack input = this.getItem(INPUT);
        if (input.isEmpty() || input == failedMatch) {
            return null;
        }
        SingleRecipeInput recipeInput = new SingleRecipeInput(input);
        if (this.level != null && curRecipe != null && curRecipe.value().matches(recipeInput, level)) {
            return curRecipe;
        } else {
            RecipeHolder<? extends AbstractCookingRecipe> rec = null;
            if (this.level != null && this.level.getServer() != null) {
                rec = this.level.getServer().getRecipeManager().getRecipeFor((RecipeType<AbstractCookingRecipe>) this.specificRecipeType, recipeInput, this.level).orElse(null);
                if (rec == null && Services.CONFIG.areVanillaRecipesEnabled()) {
                    rec = this.level.getServer().getRecipeManager().getRecipes().stream()
                            .filter(recipe -> recipe.value().getType() == Services.CONFIG.getRecipeType())
                            .filter(recipe -> recipe.value() instanceof AbstractCookingRecipe)
                            .map(recipe -> (RecipeHolder<AbstractCookingRecipe>) recipe)
                            .filter(recipe -> recipe.value().matches(recipeInput, this.level))
                            .filter(recipe -> Services.CONFIG.isRecipeAllowed(recipe.id().location()))
                            .findFirst().orElse(null);
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

    private boolean slotContainsReflector(int slot) {
        return this.getItem(slot).is(ModTags.Items.SOLAR_COOKER_REFLECTOR);
    }

    private int getReflectorCount() {
        int count = 0;
        for (int slot : SLOTS_REFLECTORS) {
            if (this.slotContainsReflector(slot)) {
                count++;
            }
        }
        return count;
    }

    private void updateReflectorStates() {
        boolean slotValue = slotContainsReflector(REFLECTOR_LEFT);
        if (this.reflectorLeft != slotValue) {
            this.reflectorLeft = slotValue;
        }
        slotValue = slotContainsReflector(REFLECTOR_RIGHT);
        if (this.reflectorRight != slotValue) {
            this.reflectorRight = slotValue;
        }
    }

    public boolean hasLeftReflector() {
        return this.reflectorLeft;
    }

    public boolean hasRightReflector() {
        return this.reflectorRight;
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

    @Override
    @Nonnull
    protected NonNullList<ItemStack> getItems() {
        return this.items;
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

    @Override
    protected void setItems(@Nonnull NonNullList<ItemStack> nonNullList) {
        this.items = nonNullList;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
    public void setItem(int index, ItemStack stack) {
        ItemStack itemstack = this.items.get(index);
        boolean flag = !stack.isEmpty() && ItemStack.isSameItemSameComponents(itemstack, stack);
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
    public void setRecipeUsed(@Nullable RecipeHolder<?> recipe) {
        if (recipe != null) {
            this.usedRecipes.addTo(recipe.id(), 1);
        }
    }

    @Override
    @Nullable
    public RecipeHolder<?> getRecipeUsed() {
        return null;
    }

    public void awardUsedRecipesAndPopExperience(Player p_235645_1_) {
        List<RecipeHolder<?>> list = this.getRecipesToAwardAndPopExperience(p_235645_1_.level(), p_235645_1_.position());
        p_235645_1_.awardRecipes(list);
        this.usedRecipes.clear();
    }

    public List<RecipeHolder<?>> getRecipesToAwardAndPopExperience(Level p_235640_1_, Vec3 p_235640_2_) {
        List<RecipeHolder<?>> list = Lists.newArrayList();

        if (p_235640_1_.getServer() != null) {
            for (Object2IntMap.Entry<ResourceKey<Recipe<?>>> entry : this.usedRecipes.object2IntEntrySet()) {
                p_235640_1_.getServer().getRecipeManager().byKey(entry.getKey()).ifPresent((recipeHolder) -> {
                    list.add(recipeHolder);
                    createExperience(p_235640_1_, p_235640_2_, entry.getIntValue(), ((AbstractCookingRecipe)recipeHolder.value()).experience());
                });
            }
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

    @Override
    public void fillStackedContents(@Nonnull StackedItemContents stackedContents) {
        for(ItemStack itemstack : this.items) {
            stackedContents.accountStack(itemstack);
        }
    }

    @Override
    public float getOpenNess(float partialTicks) {
        return this.chestLidController.getOpenness(partialTicks);
    }

}
