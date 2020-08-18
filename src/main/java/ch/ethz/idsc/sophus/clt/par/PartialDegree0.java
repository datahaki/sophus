// code by jph
package ch.ethz.idsc.sophus.clt.par;

import java.io.Serializable;

import ch.ethz.idsc.tensor.ComplexScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.sca.Exp;

/* package */ class PartialDegree0 implements PartialInterface, Serializable {
  private final Scalar factor;

  public PartialDegree0(Scalar c0) {
    factor = Exp.FUNCTION.apply(ComplexScalar.I.multiply(c0));
  }

  @Override // from Partial
  public Scalar il(Scalar t) {
    return t.multiply(factor);
  }
}
