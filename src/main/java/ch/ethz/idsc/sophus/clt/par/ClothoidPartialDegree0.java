// code by jph
package ch.ethz.idsc.sophus.clt.par;

import java.io.Serializable;

import ch.ethz.idsc.tensor.ComplexScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.sca.Exp;

/* package */ class ClothoidPartialDegree0 implements ClothoidPartial, Serializable {
  private static final long serialVersionUID = -4702561791707966591L;
  // ---
  private final Scalar factor;

  public ClothoidPartialDegree0(Scalar c0) {
    factor = Exp.FUNCTION.apply(ComplexScalar.I.multiply(c0));
  }

  @Override // from Partial
  public Scalar il(Scalar t) {
    return t.multiply(factor);
  }
}
