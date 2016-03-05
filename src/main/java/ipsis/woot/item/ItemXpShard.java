package ipsis.woot.item;

import ipsis.oss.client.ModelHelper;
import ipsis.woot.init.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemXpShard extends ItemWoot {

    public static final String BASENAME = "xpShard";

    public ItemXpShard() {

        super(BASENAME);
        setMaxStackSize(64);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initModel() {

        ModelHelper.registerItem(ModItems.itemXpShard, BASENAME);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {

        if (itemStackIn != null) {

            worldIn.playSoundAtEntity(playerIn, "random.orb", 0.1F, 0.5F * ((itemRand.nextFloat() - itemRand.nextFloat()) * 0.7F + 1.8F));
            playerIn.addExperience(1);

            if (!playerIn.capabilities.isCreativeMode)
                itemStackIn.stackSize--;
        }

        return itemStackIn;
    }
}
