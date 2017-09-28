package info.sleeplessacorn.nomagi.common.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.Arrays;
import java.util.function.Function;

public class BoundingBoxHelper {

    public static ImmutableMap<EnumFacing, AxisAlignedBB> computeAABBsForFacing(
            double northMinX, double northMinY, double northMinZ,
            double northMaxX, double northMaxY, double northMaxZ) {
        return Arrays.stream(EnumFacing.HORIZONTALS)
                .collect(ImmutableMap.toImmutableMap(Function.identity(), facing -> {
                    AxisAlignedBB aabb = new AxisAlignedBB(
                            northMinX / 16.0D, northMinY / 16.0D, northMinZ / 16.0D,
                            northMaxX / 16.0D, northMaxY / 16.0D, northMaxZ / 16.0D
                    );
                    switch (facing) {
                        case SOUTH: return new AxisAlignedBB(
                                1 - aabb.maxX, aabb.minY, 1 - aabb.maxZ,
                                1 - aabb.minX, aabb.maxY, 1 - aabb.minZ
                        );
                        case WEST: return new AxisAlignedBB(
                                aabb.minZ, aabb.minY, aabb.minX,
                                aabb.maxZ, aabb.maxY, aabb.maxX
                        );
                        case EAST: return new AxisAlignedBB(
                                1 - aabb.maxZ, aabb.minY, 1 - aabb.maxX,
                                1 - aabb.minZ, aabb.maxY, 1 - aabb.minX
                        );
                    }
                    return aabb;
                }));
    }

}
