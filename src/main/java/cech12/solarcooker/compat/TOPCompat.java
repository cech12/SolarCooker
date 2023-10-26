package cech12.solarcooker.compat;
/*
import cech12.solarcooker.SolarCookerMod;
import cech12.solarcooker.blockentity.AbstractSolarCookerBlockEntity;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.InterModComms;

import java.util.function.Function;

public class TOPCompat {

    public static void register() {
        InterModComms.sendTo("theoneprobe", "getTheOneProbe", PluginTOPRegistry::new);
    }

    public static class PluginTOPRegistry implements Function<ITheOneProbe, Void> {

        @Override
        public Void apply(ITheOneProbe probe) {
            probe.registerProvider(new IProbeInfoProvider() {
                @Override
                public ResourceLocation getID() {
                    return new ResourceLocation(SolarCookerMod.MOD_ID, "solarcookerinfo");
                }

                @Override
                public void addProbeInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, Player player, Level level, BlockState blockState, IProbeHitData iProbeHitData) {
                    BlockEntity blockEntity = level.getBlockEntity(iProbeHitData.getPos());
                    if (!(blockEntity instanceof AbstractSolarCookerBlockEntity)) {
                        return;
                    }
                    CompoundTag nbt = blockEntity.getUpdateTag();
                    if (!nbt.contains("CookTime") || !nbt.contains("CookTimeTotal")) {
                        return;
                    }
                    final int cookTime = nbt.getInt("CookTime");
                    final int cookTimeTotal = nbt.getInt("CookTimeTotal");
                    if (cookTime > 0) {
                        iProbeInfo.progress(cookTime, cookTimeTotal, new ProgressStyle()
                                .suffix(Component.literal(" / " + cookTimeTotal))
                                .alignment(ElementAlignment.ALIGN_CENTER)
                        );
                    }
                }
            });
            return null;
        }
    }
}
 */
