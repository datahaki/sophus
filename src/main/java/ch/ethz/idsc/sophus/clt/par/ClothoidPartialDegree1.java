// code by jph
package ch.ethz.idsc.sophus.clt.par;

import java.io.Serializable;

import ch.ethz.idsc.tensor.ComplexScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.sca.Exp;

/* package */ class ClothoidPartialDegree1 implements ClothoidPartial, Serializable {
  private static final long serialVersionUID = 8387109520968276351L;
  // ---
  private final Scalar c0;
  private final Scalar c1;
  private final Scalar factor;
  private final Scalar ofs;

  public ClothoidPartialDegree1(Scalar c0, Scalar c1) {
    this.c0 = c0;
    this.c1 = c1;
    factor = ComplexScalar.I.divide(c1);
    ofs = Exp.FUNCTION.apply(ComplexScalar.I.multiply(c0));
  }

  @Override // from Partial
  public Scalar il(Scalar t) {
    Scalar ofs2 = Exp.FUNCTION.apply(ComplexScalar.I.multiply(c1.multiply(t).add(c0)));
    return ofs.subtract(ofs2).multiply(factor);
  }
}
