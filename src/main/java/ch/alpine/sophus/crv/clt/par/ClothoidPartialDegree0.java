// code by jph
package ch.alpine.sophus.crv.clt.par;

import java.io.Serializable;

import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.sca.exp.Exp;

/* package */ class ClothoidPartialDegree0 implements ClothoidPartial, Serializable {
  private final Scalar factor;

  public ClothoidPartialDegree0(Scalar c0) {
    factor = Exp.FUNCTION.apply(ComplexScalar.I.multiply(c0));
  }

  @Override // from Partial
  public Scalar il(Scalar t) {
    return t.multiply(factor);
  }
}
