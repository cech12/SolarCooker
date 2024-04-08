package de.cech12.solarcooker.init;

import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.inventory.SolarCookerContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Constants.MOD_ID);

    static {
        Constants.SOLAR_COOKER_MENU_TYPE = MENU_TYPES.register(Constants.SOLAR_COOKER_MENU_NAME, () -> IForgeMenuType.create((pWindowID, pInventory, pData) -> new SolarCookerContainer(Constants.SOLAR_COOKING_RECIPE_TYPE.get(), pWindowID, pInventory)));
    }
}
