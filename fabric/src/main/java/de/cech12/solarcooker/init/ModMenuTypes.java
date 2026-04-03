package de.cech12.solarcooker.init;

import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.FabricSolarCookerMod;
import de.cech12.solarcooker.inventory.SolarCookerContainer;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class ModMenuTypes {

    private static final MenuType<SolarCookerContainer> SOLAR_COOKER_MENU_TYPE = register(Constants.SOLAR_COOKER_MENU_NAME, new ExtendedMenuType<>((pWindowID, pInventory, pData) -> new SolarCookerContainer(Constants.SOLAR_COOKING_RECIPE_TYPE.get(), pWindowID, pInventory), FabricSolarCookerMod.SolarCookerData.CODEC));

    static {
        Constants.SOLAR_COOKER_MENU_TYPE = () -> SOLAR_COOKER_MENU_TYPE;
    }

    public static void init() {}

    private static <T extends AbstractContainerMenu, D> MenuType<T> register(String name, ExtendedMenuType<T, D> screenHandlerType) {
        return Registry.register(BuiltInRegistries.MENU, Constants.id(name), screenHandlerType);
    }

}
