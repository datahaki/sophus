// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import java.io.Serializable;

import ch.ethz.idsc.tensor.ComplexScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.sca.Exp;

public class LineIntegral implements ClothoidIntegral, Serializable {
  private final Scalar factor;

  /* package */ LineIntegral(Scalar c0) {
    factor = Exp.FUNCTION.apply(ComplexScalar.I.multiply(c0));
  }

  @Override // from ClothoidIntegral
  public Scalar normalized(Scalar t) {
    return il(t).divide(one());
  }

  @Override // from ClothoidIntegral
  public Scalar il(Scalar t) {
    return t.multiply(factor);
  }

  @Override // from ClothoidIntegral
  public Scalar one() {
    return il(RealScalar.ONE);
  }
}
