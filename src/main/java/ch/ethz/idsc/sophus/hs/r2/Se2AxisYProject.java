// code by jph
package ch.ethz.idsc.sophus.hs.r2;

import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorScalarFunction;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.qty.QuantityUnit;
import ch.ethz.idsc.tensor.qty.Unit;
import ch.ethz.idsc.tensor.sca.Abs;
import ch.ethz.idsc.tensor.sca.ArcTan;
import ch.ethz.idsc.tensor.sca.Sign;

public class Se2AxisYProject implements TensorScalarFunction {
  private static class MapSingular implements TensorScalarFunction {
    private static final Scalar[] SIGNUM = { //
        DoubleScalar.NEGATIVE_INFINITY, //
        RealScalar.ZERO, //
        DoubleScalar.POSITIVE_INFINITY };
    // ---
    private final Unit unit;

    MapSingular(Unit unit) {
      this.unit = unit;
    }

    @Override
    public Scalar apply(Tensor p) {
      Scalar px = p.Get(0);
      int sign = Scalars.intValueExact(Sign.FUNCTION.apply(px));
      return Quantity.of(SIGNUM[1 + sign], unit);
    }
  }

  /** @param u == {vx, 0, rate} with units {[m*s^-1], ?, [s^-1]}
   * @param p == {px, py} with units {[m], [m]}
   * @return time to arrival of a point on the y axis that is subject to flow x to reach p.
   * negative return values are also possible. */
  public static TensorScalarFunction of(Tensor u) {
    Scalar vx = u.Get(0);
    Scalar be = u.Get(2);
    if (Scalars.isZero(be)) { // prevent division by 0 after arc tan
      if (Scalars.isZero(vx)) // prevent division by 0 of px
        return new MapSingular(QuantityUnit.of(be).negate());
      return p -> p.Get(0).divide(vx);
    }
    return new Se2AxisYProject(vx, be);
  }

  /***************************************************/
  private final Scalar vx;
  private final Scalar be;
  private final Scalar se;

  private Se2AxisYProject(Scalar vx, Scalar be) {
    this.vx = Abs.FUNCTION.apply(vx);
    this.be = be;
    se = be.multiply(Sign.FUNCTION.apply(vx));
  }

  @Override
  public Scalar apply(Tensor p) {
    Scalar px = p.Get(0);
    Scalar py = p.Get(1);
    return ArcTan.of(vx.subtract(py.multiply(se)), px.multiply(se)).divide(be);
  }
}
