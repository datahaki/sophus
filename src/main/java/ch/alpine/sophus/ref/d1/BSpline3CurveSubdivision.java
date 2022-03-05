// code by jph
package ch.alpine.sophus.ref.d1;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.api.SplitInterface;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

/** cubic B-spline
 * 
 * Dyn/Sharon 2014 p.16 show that the scheme has a contractivity factor of mu = 1/2 */
public class BSpline3CurveSubdivision extends RefiningBSpline3CurveSubdivision implements Serializable {
  private static final Scalar _1_4 = RationalScalar.of(1, 4);
  private static final Scalar _3_4 = RationalScalar.of(3, 4);
  // ---
  protected final SplitInterface splitInterface;

  /** @param splitInterface */
  public BSpline3CurveSubdivision(SplitInterface splitInterface) {
    this.splitInterface = Objects.requireNonNull(splitInterface);
  }

  @Override // from MidpointInterface
  public final Tensor midpoint(Tensor p, Tensor q) { // point between p and q
    return splitInterface.midpoint(p, q);
  }

  @Override // from AbstractBSpline3CurveSubdivision
  protected final Tensor center(Tensor p, Tensor q, Tensor r) { // reposition of point q
    return midpoint( //
        splitInterface.split(p, q, _3_4), //
        splitInterface.split(q, r, _1_4));
  }
}
