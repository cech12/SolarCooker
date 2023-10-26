package cech12.solarcooker.init;

import cech12.solarcooker.SolarCookerMod;
import cech12.solarcooker.inventory.SolarCookerContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, SolarCookerMod.MOD_ID);

    public static final RegistryObject<MenuType<? extends AbstractContainerMenu>> SOLAR_COOKER = MENU_TYPES.register("solarcooker", () -> IForgeMenuType.create((pWindowID, pInventory, pData) -> new SolarCookerContainer(ModRecipeTypes.SOLAR_COOKING.get(), pWindowID, pInventory)));

}
