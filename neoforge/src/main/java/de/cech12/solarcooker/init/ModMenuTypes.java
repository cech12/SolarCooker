package de.cech12.solarcooker.init;

import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.inventory.SolarCookerContainer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, Constants.MOD_ID);

    static {
        Constants.SOLAR_COOKER_MENU_TYPE = MENU_TYPES.register(Constants.SOLAR_COOKER_MENU_NAME, () -> IMenuTypeExtension.create((pWindowID, pInventory, pData) -> new SolarCookerContainer(Constants.SOLAR_COOKING_RECIPE_TYPE.get(), pWindowID, pInventory)));
    }
}
