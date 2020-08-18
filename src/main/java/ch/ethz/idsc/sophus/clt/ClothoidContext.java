// code by jph
package ch.ethz.idsc.sophus.clt;

import ch.ethz.idsc.sophus.crv.ArcTan2D;
import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringGroup;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** Hint: class is designed to be generic for all clothoid variants */
public final class ClothoidContext {
  private static final Scalar HALF = RealScalar.of(0.5);
  // ---
  private final Tensor p;
  private final Tensor q;
  private final LieGroupElement lieGroupElement;
  private final Tensor diff;
  private final Scalar b0;
  private final Scalar b1;

  public ClothoidContext(Tensor p, Tensor q) {
    this.p = p;
    this.q = q;
    lieGroupElement = Se2CoveringGroup.INSTANCE.element(p);
    Tensor _q = lieGroupElement.inverse().combine(q);
    diff = _q.extract(0, 2);
    Scalar da = ArcTan2D.of(diff); // special case when diff == {0, 0}
    b0 = da.negate();
    b1 = _q.Get(2).subtract(da);
  }

  public LieGroupElement lieGroupElement() {
    return lieGroupElement;
  }

  public Scalar b0() {
    return b0;
  }

  public Scalar b1() {
    return b1;
  }

  public Scalar s1() {
    return b0.add(b1).multiply(HALF);
  }

  public Scalar s2() {
    return b0.subtract(b1).multiply(HALF);
  }

  public Tensor diff() {
    return diff;
  }

  /***************************************************/
  /** Hint: function only for the purpose of demonstration
   * 
   * @return */
  public Tensor p() {
    return p.unmodifiable();
  }

  /** Hint: function only for the purpose of demonstration
   * 
   * @return */
  public Tensor q() {
    return q.unmodifiable();
  }
}
