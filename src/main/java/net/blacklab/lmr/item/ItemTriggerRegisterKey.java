package net.blacklab.lmr.item;

import java.util.List;

import javax.annotation.Nullable;

import net.blacklab.lmr.LittleMaidReengaged;
import net.blacklab.lmr.util.TriggerSelect;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemTriggerRegisterKey extends Item {

	public static final String RK_MODE_TAG = LittleMaidReengaged.DOMAIN + ":RK_MODE";
	public static final String RK_COUNT = LittleMaidReengaged.DOMAIN + ":RK_COUNT";

	public static final int RK_MAX_COUNT = 32;

	public ItemTriggerRegisterKey() {
		setUnlocalizedName(LittleMaidReengaged.DOMAIN + ":registerkey");
		setRegistryName(getUnlocalizedName());
		setCreativeTab(CreativeTabs.MISC);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn,
			EntityPlayer playerIn, EnumHand pHand) {
		ItemStack itemStackIn = playerIn.getHeldItem(pHand);
		NBTTagCompound tagCompound = itemStackIn.getTagCompound();
		if(tagCompound==null) {
			tagCompound = new NBTTagCompound();
			itemStackIn.setTagCompound(tagCompound);
		}

		int index = 0;
		String modeString = tagCompound.getString(RK_MODE_TAG);

		// 登録モードを切り替える．
		index = TriggerSelect.selector.indexOf(modeString) + 1;
		if(index >= TriggerSelect.selector.size()) index = 0;

		modeString = TriggerSelect.selector.get(index);
		tagCompound.setString(RK_MODE_TAG, modeString);

		if(!worldIn.isRemote)
			playerIn.sendMessage(new TextComponentTranslation("littleMaidMob.chat.text.changeregistermode", modeString));

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		NBTTagCompound tagCompound = stack.getTagCompound();
		if(tagCompound != null) {
			tooltip.add("Mode: "+tagCompound.getString(RK_MODE_TAG));
			tooltip.add("Remains: " + (RK_MAX_COUNT-tagCompound.getInteger(RK_COUNT)));
		}
	}

	@Override
	public boolean isDamageable() {
		return true;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn,
			int itemSlot, boolean isSelected) {
		if(!stack.hasTagCompound()){
			NBTTagCompound t = new NBTTagCompound();
			t.setString(RK_MODE_TAG, TriggerSelect.selector.get(0));
			stack.setTagCompound(t);
		}
	}

}
