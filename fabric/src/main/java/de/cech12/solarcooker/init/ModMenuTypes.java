package de.cech12.solarcooker.init;

import de.cech12.solarcooker.CommonLoader;
import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.inventory.SolarCookerContainer;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class ModMenuTypes {

    private static final MenuType<SolarCookerContainer> SOLAR_COOKER_MENU_TYPE = register(Constants.SOLAR_COOKER_MENU_NAME, new ExtendedScreenHandlerType<>((pWindowID, pInventory, pData) -> new SolarCookerContainer(Constants.SOLAR_COOKING_RECIPE_TYPE.get(), pWindowID, pInventory)));

    static {
        Constants.SOLAR_COOKER_MENU_TYPE = () -> SOLAR_COOKER_MENU_TYPE;
    }

    public static void init() {}

    private static <T extends AbstractContainerMenu> MenuType<T> register(String name, ExtendedScreenHandlerType<T> screenHandlerType) {
        return Registry.register(BuiltInRegistries.MENU, CommonLoader.id(name), screenHandlerType);
    }

}
