package ipsis.woot.plugins.top;

import ipsis.woot.tileentity.ui.ControllerUIInfo;
import ipsis.woot.tileentity.ui.FarmUIInfo;
import ipsis.woot.util.StringHelper;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.elements.ElementProgress;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class TOPUIInfoConvertors {

    public static void controllerConvertor(ControllerUIInfo controller, ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

        probeInfo.text(TextFormatting.GREEN + "Mob: " + StringHelper.localize(controller.wootMob.getDisplayName()));
        probeInfo.text(TextFormatting.BLUE + controller.requiredTier.getTranslated("info.woot.tier"));
    }

    public static void farmConvertor(FarmUIInfo farm, ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

        /**
         * Progress
         */
        if (farm.isRunning) {
            int p = (int)((100.0F / (float)farm.recipeTotalPower) * (float)farm.consumedPower);
            p = MathHelper.clamp(p, 0, 100);
            probeInfo.progress(p, 100,
                    probeInfo.defaultProgressStyle().suffix("%").filledColor(0x99ffffdd).alternateFilledColor(0xff000043));
        }

        /**
         * Redstone state
         */
        probeInfo.horizontal().item(new ItemStack(Items.REDSTONE), probeInfo.defaultItemStyle().width(14).height(14)).text("State: " + (farm.isRunning ? "On" : "Off"));
        probeInfo.text(TextFormatting.GREEN + farm.mobName + " * " + farm.mobCount);

        if (mode == ProbeMode.EXTENDED) {
            /**
             * Recipe
             */
            String total = ElementProgress.format(farm.recipeTotalPower, NumberFormat.COMPACT, "RF");
            String perTick = ElementProgress.format(farm.recipePowerPerTick, NumberFormat.COMPACT, "RF/tick");

            probeInfo.text(TextFormatting.GREEN + total + " @ " + perTick);
            probeInfo.text(TextFormatting.GREEN + Integer.toString(farm.recipeTotalTime) + " ticks");

            /**
             * Ingredients
             */
            if (!farm.ingredientsItems.isEmpty() || !farm.ingredientsFluids.isEmpty()) {
                IProbeInfo vertical = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(0xffffffff).spacing(0));
                IProbeInfo horizontal = null;
                int rows = 0;
                int idx = 0;
                for (ItemStack itemStack : farm.ingredientsItems) {
                    if (idx % 10 == 0) {
                        horizontal = vertical.horizontal(probeInfo.defaultLayoutStyle().spacing(0));
                        rows++;
                        if (rows > 4)
                            break;
                        }

                    horizontal.item(itemStack);
                    idx++;
                }

                for (FluidStack fluidStack : farm.ingredientsFluids) {

                    int contents = fluidStack.amount;
                    probeInfo.text("Liquid: " + fluidStack.getLocalizedName());
                    probeInfo.progress(contents, fluidStack.amount,
                        probeInfo.defaultProgressStyle()
                        .filledColor(0xff0000dd)
                        .alternateFilledColor(0xff000043)
                        .borderColor(0xff555555)
                        .numberFormat(NumberFormat.FULL));
                }
            }

            /**
             * Drops
             */
            if (!farm.drops.isEmpty()) {

                IProbeInfo vertical = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(0xffffffff).spacing(0));
                IProbeInfo horizontal = null;
                int rows = 0;
                int idx = 0;
                for (ItemStack itemStack : farm.drops) {
                    if (idx % 10 == 0) {
                        horizontal = vertical.horizontal(probeInfo.defaultLayoutStyle().spacing(0));
                        rows++;
                        if (rows > 4)
                            break;
                    }

                    horizontal.item(itemStack);
                    idx++;
                }
            }
        }
    }
}
