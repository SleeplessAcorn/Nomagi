package info.sleeplessacorn.nomagi.common.entity;

import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.common.block.BlockChair;
import info.sleeplessacorn.nomagi.common.util.MaterialHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityChair extends Entity {

    private static final ResourceLocation ID = new ResourceLocation(Nomagi.ID, "entity_chair");

    public EntityChair(World world, BlockPos pos) {
        super(world);
        setPosition(pos.getX() + 0.5, pos.getY() + 0.375, pos.getZ() + 0.5);
        setSize(0.0F, 0.0F);
    }

    @Deprecated
    public EntityChair(World world) {
        this(world, BlockPos.ORIGIN);
    }

    public static ResourceLocation getRegistryName() {
        return ID;
    }

    @Override
    public float getExplosionResistance(Explosion explosion, World world, BlockPos pos, IBlockState state) {
        return MaterialHelper.getResistance(Material.WOOD);
    }

    @Override
    protected void entityInit() {}

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!getEntityWorld().isRemote && getEntityWorld().isBlockLoaded(getPosition())) {
            IBlockState state = getEntityWorld().getBlockState(getPosition());
            if (getPassengers().isEmpty() || !(state.getBlock() instanceof BlockChair)) {
                setDead();
            }
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {}

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {}

}
