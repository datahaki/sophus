// code by jph
// adapted from https://github.com/mcapino/trajectorytools
// adapted from https://github.com/AtsushiSakai/PythonRobotics
package ch.ethz.idsc.sophus.crv.dubins;

import java.io.Serializable;
import java.util.Optional;
import java.util.stream.Stream;

import ch.ethz.idsc.sophus.crv.dubins.DubinsPath.Type;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringGroup;
import ch.ethz.idsc.sophus.math.d2.ArcTan2D;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;
import ch.ethz.idsc.tensor.sca.Sign;

/** stream produces at least 2 and at most 6 {@link DubinsPath} */
public class FixedRadiusDubins implements DubinsPathGenerator, Serializable {
  /** @param xya vector of length 3
   * @param radius
   * @return */
  public static DubinsPathGenerator of(Tensor xya, Scalar radius) {
    return new FixedRadiusDubins(xya, radius);
  }

  /** @param start
   * @param end
   * @param radius
   * @return */
  public static DubinsPathGenerator of(Tensor start, Tensor end, Scalar radius) {
    return of(Se2CoveringGroup.INSTANCE.element(start).inverse().combine(end), radius);
  }

  /***************************************************/
  private final Tensor xya;
  private final Scalar radius;
  private final Scalar zero;

  private FixedRadiusDubins(Tensor xya, Scalar radius) {
    this.xya = xya;
    this.radius = Sign.requirePositive(radius);
    zero = radius.zero();
  }

  @Override // from DubinsPathGenerator
  public Stream<DubinsPath> stream() {
    return Stream.of(Type.values()) //
        .map(this::create) //
        .filter(Optional::isPresent) //
        .map(Optional::get);
  }

  private Optional<DubinsPath> create(Type type) {
    Tensor center1 = Tensors.of(zero, radius, xya.Get(2).zero());
    Tensor h = Tensors.of(zero, type.isFirstEqualsLast() ? radius : radius.negate(), xya.Get(2).zero());
    Tensor gnorm = type.isFirstTurnRight() ? Se2Flip.FUNCTION.apply(xya) : xya;
    Tensor center3 = Se2CoveringGroup.INSTANCE.element(gnorm).combine(h);
    Tensor deltacenter = Se2CoveringGroup.INSTANCE.element(center1).inverse().combine(center3);
    Scalar dist_tr = Vector2Norm.of(deltacenter.extract(0, 2));
    Scalar th_tr = ArcTan2D.of(deltacenter);
    Scalar th_total = deltacenter.Get(2);
    th_tr = StaticHelper.principalValue(th_tr);
    th_total = StaticHelper.principalValue(th_total);
    return type.dubinsSteer().steer(dist_tr, th_tr, th_total, radius) //
        .map(segLength -> DubinsPath.of(type, radius, segLength));
  }
}
