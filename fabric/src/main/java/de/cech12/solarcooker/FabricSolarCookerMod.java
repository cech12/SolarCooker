package de.cech12.solarcooker;

import de.cech12.solarcooker.init.ModBlockEntityTypes;
import de.cech12.solarcooker.init.ModBlocks;
import de.cech12.solarcooker.init.ModItems;
import de.cech12.solarcooker.init.ModMenuTypes;
import de.cech12.solarcooker.init.ModRecipeTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;

public class FabricSolarCookerMod implements ModInitializer {

    @Override
    public void onInitialize() {
        ModBlocks.init();
        ModBlockEntityTypes.init();
        ModItems.init();
        ModRecipeTypes.init();
        ModMenuTypes.init();
        //Config
        CommonLoader.init();
        //Register items in the creative tab.
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(content -> {
            content.accept(Constants.SOLAR_COOKER_ITEM.get());
            content.accept(Constants.REFLECTOR_ITEM.get());
            content.accept(Constants.SHINING_DIAMOND_BLOCK_ITEM.get());
        });
    }

}
