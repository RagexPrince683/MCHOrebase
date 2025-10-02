package com.norwood.mcheli.light;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockLight extends Block {

    public BlockLight() {
        super(Material.ROCK); // rock so tanks/vehicles don’t break it
        this.setLightLevel(1.0F); // light level 15
        this.setBlockUnbreakable();
        this.setResistance(6000000F);
        this.setTickRandomly(false);
        this.setCreativeTab(null); // hidden from creative menu
    }

    // No drops
    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Nullable
    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    public void getDrops(List<ItemStack> drops, IBlockAccess world, BlockPos pos,
                         IBlockState state, int fortune) {
        drops.clear(); // never drop anything
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosion) {
        return false;
    }

    // Rendering / model
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    // Replaceable / air-like behavior
    @Override
    public boolean isReplaceable(IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isAir(IBlockState state, IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {

    }

    // Collision
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB; // no collision box
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos,
                                      AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes,
                                      @Nullable Entity entityIn, boolean isActualState) {
        // no collision added
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    // Placement updates (force relight)
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        worldIn.notifyBlockUpdate(pos, state, state, 3);
        worldIn.checkLightFor(EnumSkyBlock.BLOCK, pos);
    }
}
