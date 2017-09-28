package info.sleeplessacorn.nomagi.common.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.Arrays;
import java.util.function.Function;

public class BoundingBoxHelper {

    public static AxisAlignedBB createAABB(
            double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return new AxisAlignedBB(minX / 16.0D, minY / 16.0D, minZ / 16.0D, maxX / 16.0D, maxY / 16.0D, maxZ / 16.0D);
    }

    public static ImmutableMap<EnumFacing, AxisAlignedBB> computeAABBsForFacing(
            double northMinX, double northMinY, double northMinZ,
            double northMaxX, double northMaxY, double northMaxZ) {
        return Arrays.stream(EnumFacing.HORIZONTALS)
                .collect(ImmutableMap.toImmutableMap(Function.identity(), facing -> {
                    AxisAlignedBB aabb = new AxisAlignedBB(
                            northMinX / 16.0D, northMinY / 16.0D, northMinZ / 16.0D,
                            northMaxX / 16.0D, northMaxY / 16.0D, northMaxZ / 16.0D);
                    switch (facing) {
                        case SOUTH:
                            return new AxisAlignedBB(
                                    1.0D - aabb.maxX, aabb.minY, 1.0D - aabb.maxZ,
                                    1.0D - aabb.minX, aabb.maxY, 1.0D - aabb.minZ);
                        case WEST:
                            return new AxisAlignedBB(
                                    aabb.minZ, aabb.minY, aabb.minX,
                                    aabb.maxZ, aabb.maxY, aabb.maxX);
                        case EAST:
                            return new AxisAlignedBB(
                                    1.0D - aabb.maxZ, aabb.minY, 1.0D - aabb.maxX,
                                    1.0D - aabb.minZ, aabb.maxY, 1.0D - aabb.minX);
                    }
                    return aabb;
                }));
    }

}
