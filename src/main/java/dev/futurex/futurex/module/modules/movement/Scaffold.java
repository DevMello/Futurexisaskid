package dev.futurex.futurex.module.modules.movement;

import dev.futurex.futurex.managers.ModuleManager;
import dev.futurex.futurex.mixin.MixinInterface;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.setting.slider.Slider;
import dev.futurex.futurex.util.world.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.List;

/**
 * @author ZimTheDestroyer
 * @since 12/8/20 @ 12:35 PM CST
 */

public class Scaffold extends Module {
    public Scaffold() {
        super("Scaffold", Category.MOVEMENT, "Rapidly places blocks underneath you");
    }

    public static Mode mode = new Mode("Mode", "Tower", "Static");
    public static Checkbox swing = new Checkbox("Swing Arm", false);
    public static Checkbox bSwitch = new Checkbox("Switch", false);
    public static Checkbox center = new Checkbox("Center", false);
    public static Checkbox keepY = new Checkbox("KeepYLevel", false);
    public static Checkbox sprint = new Checkbox("UseSprint", true);
    public static Checkbox replenishBlocks = new Checkbox("ReplenishBlocks", true);
    public static Checkbox down = new Checkbox("Down", false);
    public static Slider expand = new Slider("Expand", 1.0, 1.0, 6.0, 0);

    @Override
    public void setup() {
        addSetting(mode);
        addSetting(swing);
        addSetting(bSwitch);
        addSetting(center);
        addSetting(keepY);
        addSetting(sprint);
        addSetting(replenishBlocks);
        addSetting(down);
        addSetting(expand);
    }

    private final List<Block> invalid = Arrays.asList(
            Blocks.ENCHANTING_TABLE, Blocks.FURNACE, Blocks.CARPET, Blocks.CRAFTING_TABLE, Blocks.TRAPPED_CHEST, Blocks.CHEST, Blocks.DISPENSER,
            Blocks.AIR, Blocks.WATER, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.FLOWING_LAVA,
            Blocks.SNOW_LAYER, Blocks.TORCH, Blocks.ANVIL, Blocks.JUKEBOX, Blocks.STONE_BUTTON, Blocks.WOODEN_BUTTON, Blocks.LEVER,
            Blocks.NOTEBLOCK, Blocks.STONE_PRESSURE_PLATE, Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE,
            Blocks.WOODEN_PRESSURE_PLATE, Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM,
            Blocks.YELLOW_FLOWER, Blocks.RED_FLOWER, Blocks.ANVIL, Blocks.CACTUS, Blocks.LADDER, Blocks.ENDER_CHEST
    );

    private final Timer timerMotion = new Timer();
    private final Timer itemTimer = new Timer();
    private BlockData blockData;
    private int lastY;
    private BlockPos pos;
    private boolean teleported;

    @Override
    public void onUpdate() {
        if (ModuleManager.getModuleByClass(Sprint.class).isEnabled()) {
            if ((down.getValue() && MixinInterface.mc.gameSettings.keyBindSneak.isKeyDown()) || !sprint.getValue()){
                MixinInterface.mc.player.setSprinting(false);
                ModuleManager.getModuleByClass(Sprint.class).toggle();
            }
        }

        if (replenishBlocks.getValue() && !(MixinInterface.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBlock) && getBlockCountHotbar() <= 0 && itemTimer.passed(100L, Timer.Format.System)) {
            for (int i = 9; i < 45; ++i) {
                if (MixinInterface.mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                    final ItemStack is = MixinInterface.mc.player.inventoryContainer.getSlot(i).getStack();
                    if (is.getItem() instanceof ItemBlock && !invalid.contains(Block.getBlockFromItem(is.getItem()))) {
                        if (i < 36) {
                            swap(getItemSlot(MixinInterface.mc.player.inventoryContainer, is.getItem()), 44);
                        }
                    }
                }
            }
        }

        if (keepY.getValue()) {
            if ((!isMoving(MixinInterface.mc.player) && MixinInterface.mc.gameSettings.keyBindJump.isKeyDown()) || MixinInterface.mc.player.collidedVertically || MixinInterface.mc.player.onGround)
                lastY = MathHelper.floor(MixinInterface.mc.player.posY);
        }

        else
            lastY = MathHelper.floor(MixinInterface.mc.player.posY);

        blockData = null;
        double x = MixinInterface.mc.player.posX;
        double z = MixinInterface.mc.player.posZ;
        double y = keepY.getValue() ? lastY : MixinInterface.mc.player.posY;
        double forward = MixinInterface.mc.player.movementInput.moveForward;
        double strafe = MixinInterface.mc.player.movementInput.moveStrafe;
        float yaw = MixinInterface.mc.player.rotationYaw;

        if (!MixinInterface.mc.player.collidedHorizontally){
            double[] coords = getExpandCoords(x,z,forward,strafe,yaw);
            x = coords[0];
            z = coords[1];
        }

        if (canPlace(MixinInterface.mc.world.getBlockState(new BlockPos(MixinInterface.mc.player.posX, MixinInterface.mc.player.posY - (MixinInterface.mc.gameSettings.keyBindSneak.isKeyDown() && down.getValue() ? 2 : 1), MixinInterface.mc.player.posZ)).getBlock())) {
            x = MixinInterface.mc.player.posX;
            z = MixinInterface.mc.player.posZ;
        }

        BlockPos blockBelow = new BlockPos(x, y-1, z);
        if (MixinInterface.mc.gameSettings.keyBindSneak.isKeyDown() && down.getValue()) {
            blockBelow = new BlockPos(x, y - 2, z);
        }

        pos = blockBelow;
        if (MixinInterface.mc.world.getBlockState(blockBelow).getBlock() == Blocks.AIR) {
            blockData = getBlockData2(blockBelow);
        }

        if (blockData != null) {
            if (getBlockCountHotbar() <= 0 || !bSwitch.getValue() && !(MixinInterface.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock)) {
                return;
            }

            final int heldItem = MixinInterface.mc.player.inventory.currentItem;
            if (bSwitch.getValue()) {
                for (int j = 0; j < 9; ++j) {
                    MixinInterface.mc.player.inventory.getStackInSlot(j);
                    if (MixinInterface.mc.player.inventory.getStackInSlot(j).getCount() != 0 && MixinInterface.mc.player.inventory.getStackInSlot(j).getItem() instanceof ItemBlock && !invalid.contains(((ItemBlock) MixinInterface.mc.player.inventory.getStackInSlot(j).getItem()).getBlock())) {
                        MixinInterface.mc.player.inventory.currentItem = j;
                        break;
                    }
                }
            }

            if (mode.getValue() == 0) {
                if (MixinInterface.mc.gameSettings.keyBindJump.isKeyDown() && MixinInterface.mc.player.moveForward == 0.0f && MixinInterface.mc.player.moveStrafing == 0.0f && !MixinInterface.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                    if (!teleported && center.getValue()) {
                        teleported = true;
                        BlockPos pos = new BlockPos(MixinInterface.mc.player.posX, MixinInterface.mc.player.posY, MixinInterface.mc.player.posZ);
                        MixinInterface.mc.player.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                    }

                    if (center.getValue() && !teleported)
                        return;

                    MixinInterface.mc.player.motionY = 0.42f;
                    MixinInterface.mc.player.motionZ = 0;
                    MixinInterface.mc.player.motionX = 0;
                    if (timerMotion.sleep(1500L)) {
                        MixinInterface.mc.player.motionY = -0.28;
                    }
                }

                else {
                    timerMotion.reset();
                    if (teleported && center.getValue())
                        teleported = false;
                }
            }

            if (MixinInterface.mc.playerController.processRightClickBlock(MixinInterface.mc.player, MixinInterface.mc.world, blockData.position, blockData.face, new Vec3d(blockData.position.getX() + Math.random(), blockData.position.getY() + Math.random(), blockData.position.getZ() + Math.random()), EnumHand.MAIN_HAND) != EnumActionResult.FAIL) {
                if (swing.getValue())
                    MixinInterface.mc.player.swingArm(EnumHand.MAIN_HAND);
                else
                    MixinInterface.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
            }

            MixinInterface.mc.player.inventory.currentItem = heldItem;
        }
    }

    public static void swap(int slot, int hotbarNum) {
        MixinInterface.mc.playerController.windowClick(MixinInterface.mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, MixinInterface.mc.player);
        MixinInterface.mc.playerController.windowClick(MixinInterface.mc.player.inventoryContainer.windowId, hotbarNum, 0, ClickType.PICKUP, MixinInterface.mc.player);
        MixinInterface.mc.playerController.windowClick(MixinInterface.mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, MixinInterface.mc.player);
        MixinInterface.mc.playerController.updateController();
    }

    public static int getItemSlot(Container container, Item item) {
        int slot = 0;
        for (int i = 9; i < 45; ++i) {
            if (container.getSlot(i).getHasStack()) {
                ItemStack is = container.getSlot(i).getStack();
                if (is.getItem() == item)
                    slot = i;
            }
        }
        return slot;
    }

    public static boolean isMoving(EntityLivingBase entity) {
        return entity.moveForward != 0 || entity.moveStrafing != 0;
    }

    public double[] getExpandCoords(double x, double z, double forward, double strafe, float YAW){
        BlockPos underPos = new BlockPos(x, MixinInterface.mc.player.posY - (MixinInterface.mc.gameSettings.keyBindSneak.isKeyDown() && down.getValue() ? 2 : 1), z);
        Block underBlock = MixinInterface.mc.world.getBlockState(underPos).getBlock();
        double xCalc = -999, zCalc = -999;
        double dist = 0;
        double expandDist = expand.getValue() * 2;
        while(!canPlace(underBlock)){
            xCalc = x;
            zCalc = z;
            dist ++;
            if(dist > expandDist){
                dist = expandDist;
            }
            xCalc += (forward * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f)) + strafe * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f))) * dist;
            zCalc += (forward * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f)) - strafe * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f))) * dist;
            if(dist == expandDist){
                break;
            }
            underPos = new BlockPos(xCalc, MixinInterface.mc.player.posY - (MixinInterface.mc.gameSettings.keyBindSneak.isKeyDown() && down.getValue() ? 2 : 1), zCalc);
            underBlock = MixinInterface.mc.world.getBlockState(underPos).getBlock();
        }
        return new double[]{xCalc,zCalc};
    }

    public boolean canPlace(Block block) {
        return (block instanceof BlockAir || block instanceof BlockLiquid) && MixinInterface.mc.world != null && MixinInterface.mc.player != null && pos != null && MixinInterface.mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos)).isEmpty();
    }

    private int getBlockCountHotbar() {
        int blockCount = 0;
        for (int i = 36; i < 45; ++i) {
            if (MixinInterface.mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = MixinInterface.mc.player.inventoryContainer.getSlot(i).getStack();
                final Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock) {
                    if (!invalid.contains(((ItemBlock)item).getBlock())) {
                        blockCount += is.getCount();
                    }
                }
            }
        }
        return blockCount;
    }

    // the second most spaghetti code shit i've seen - linus
    private BlockData getBlockData2(final BlockPos pos) {
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos.add(0, 1, 0), EnumFacing.DOWN);
        }
        final BlockPos pos2 = pos.add(-1, 0, 0);
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos2.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos2.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos2.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos2.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos2.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos2.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos2.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos3 = pos.add(1, 0, 0);
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos3.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos3.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos3.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos3.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos3.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos3.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos3.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos4 = pos.add(0, 0, 1);
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos4.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos4.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos4.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos4.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos4.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos4.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos4.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos5 = pos.add(0, 0, -1);
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos5.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos5.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos5.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos5.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos5.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos5.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos5.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos6 = pos.add(-2, 0, 0);
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos2.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos2.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos2.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos2.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos2.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos2.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos2.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos7 = pos.add(2, 0, 0);
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos3.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos3.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos3.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos3.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos3.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos3.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos3.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos8 = pos.add(0, 0, 2);
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos4.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos4.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos4.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos4.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos4.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos4.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos4.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        final BlockPos pos9 = pos.add(0, 0, -2);
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos5.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos5.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos5.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos5.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos5.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos5.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos5.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }

        final BlockPos pos10 = pos.add(0, -1, 0);
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos10.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos10.add(0, -1, 0), EnumFacing.UP);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos10.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos10.add(0, 1, 0), EnumFacing.DOWN);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos10.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos10.add(-1, 0, 0), EnumFacing.EAST);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos10.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos10.add(1, 0, 0), EnumFacing.WEST);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos10.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos10.add(0, 0, 1), EnumFacing.NORTH);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos10.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos10.add(0, 0, -1), EnumFacing.SOUTH);
        }

        final BlockPos pos11 = pos10.add(1, 0, 0);
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos11.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos11.add(0, -1, 0), EnumFacing.UP);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos11.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos11.add(0, 1, 0), EnumFacing.DOWN);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos11.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos11.add(-1, 0, 0), EnumFacing.EAST);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos11.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos11.add(1, 0, 0), EnumFacing.WEST);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos11.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos11.add(0, 0, 1), EnumFacing.NORTH);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos11.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos11.add(0, 0, -1), EnumFacing.SOUTH);
        }

        final BlockPos pos12 = pos10.add(-1, 0, 0);
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos12.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos12.add(0, -1, 0), EnumFacing.UP);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos12.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos12.add(0, 1, 0), EnumFacing.DOWN);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos12.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos12.add(-1, 0, 0), EnumFacing.EAST);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos12.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos12.add(1, 0, 0), EnumFacing.WEST);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos12.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos12.add(0, 0, 1), EnumFacing.NORTH);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos12.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos12.add(0, 0, -1), EnumFacing.SOUTH);
        }

        final BlockPos pos13 = pos10.add(0, 0, 1);
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos13.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos13.add(0, -1, 0), EnumFacing.UP);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos13.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos13.add(-1, 0, 0), EnumFacing.EAST);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos13.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos13.add(0, 1, 0), EnumFacing.DOWN);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos13.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos13.add(1, 0, 0), EnumFacing.WEST);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos13.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos13.add(0, 0, 1), EnumFacing.NORTH);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos13.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos13.add(0, 0, -1), EnumFacing.SOUTH);
        }

        final BlockPos pos14 = pos10.add(0, 0, -1);
        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos14.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos14.add(0, -1, 0), EnumFacing.UP);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos14.add(0, 1, 0)).getBlock())) {
            return new BlockData(pos14.add(0, 1, 0), EnumFacing.DOWN);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos14.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos14.add(-1, 0, 0), EnumFacing.EAST);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos14.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos14.add(1, 0, 0), EnumFacing.WEST);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos14.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos14.add(0, 0, 1), EnumFacing.NORTH);
        }

        if (!invalid.contains(MixinInterface.mc.world.getBlockState(pos14.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos14.add(0, 0, -1), EnumFacing.SOUTH);
        }

        return null;
    }

    private static class BlockData {
        public BlockPos position;
        public EnumFacing face;

        public BlockData(final BlockPos position, final EnumFacing face) {
            this.position = position;
            this.face = face;
        }
    }
}
