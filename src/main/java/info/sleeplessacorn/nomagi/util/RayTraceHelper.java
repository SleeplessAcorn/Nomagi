package info.sleeplessacorn.nomagi.util;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class RayTraceHelper {

    @Nullable
    public static RayTraceResult rayTraceMultiAABB(List<AxisAlignedBB> aabbs, BlockPos pos, Vec3d start, Vec3d end) {
        List<RayTraceResult> list = aabbs.stream().map(aabb -> {
            double x = pos.getX(), y = pos.getY(), z = pos.getZ();
            RayTraceResult result = aabb.calculateIntercept(start.subtract(x, y, z), end.subtract(x, y, z));
            if (result != null) {
                Vec3d hitVec = result.hitVec.addVector(x, y, z);
                return new RayTraceResult(hitVec, result.sideHit, pos);
            }
            return null;
        }).collect(Collectors.toList());
        RayTraceResult returnResult = null;
        double sqrDis = 0.0D;
        for (RayTraceResult resultAt : list) {
            if (resultAt == null) continue;
            double newSqrDis = resultAt.hitVec.squareDistanceTo(end);
            if (newSqrDis > sqrDis) {
                returnResult = resultAt;
                sqrDis = newSqrDis;
            }
        }
        return returnResult;
    }

}
