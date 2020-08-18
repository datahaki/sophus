// code by jph
package ch.ethz.idsc.sophus.clt;

import ch.ethz.idsc.sophus.crv.ArcTan2D;
import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringGroup;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

// TODO EXPERIMENTAL
public class ClothoidContext {
  private static final Scalar HALF = RealScalar.of(0.5);
  // ---
  public final Tensor p;
  public final Tensor q;
  public final LieGroupElement lieGroupElement;
  public final Tensor diff;
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

  public Scalar b0() {
    return b0;
  }

  public Scalar b1() {
    return b1;
  }

  public Scalar s1() {
    return b0().add(b1()).multiply(HALF);
  }

  public Scalar s2() {
    return b0().subtract(b1()).multiply(HALF);
  }
}
