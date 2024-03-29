// code by jph
package ch.alpine.sophus.crv.clt.par;

import java.io.Serializable;

import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.red.Times;
import ch.alpine.tensor.sca.erf.Erfi;
import ch.alpine.tensor.sca.exp.Exp;
import ch.alpine.tensor.sca.pow.Sqrt;

/* package */ class ClothoidPartialDegree2 implements ClothoidPartial, Serializable {
  private static final Scalar _N1_1_4 = ComplexScalar.of(+0.7071067811865476, 0.7071067811865475);
  private static final Scalar _N1_3_4 = ComplexScalar.of(-0.7071067811865475, 0.7071067811865476);
  private static final Scalar _1_4 = RationalScalar.of(1, 4);
  // ---
  private final Scalar c1;
  private final Scalar c2;
  private final Scalar f4;
  private final Scalar factor;
  private final Scalar ofs;

  public ClothoidPartialDegree2(Scalar c0, Scalar c1, Scalar c2) {
    this.c1 = c1;
    this.c2 = c2;
    Scalar f1 = _N1_3_4;
    Scalar f2 = Exp.FUNCTION.apply(c0.subtract(_1_4.multiply(c1).multiply(c1).divide(c2)).multiply(ComplexScalar.I));
    Scalar f3 = Sqrt.FUNCTION.apply(Pi.VALUE);
    f4 = RationalScalar.HALF.divide(Sqrt.FUNCTION.apply(c2));
    factor = Times.of(f1, f2, f3, f4);
    ofs = Erfi.FUNCTION.apply(_N1_1_4.multiply(c1).multiply(f4));
  }

  @Override // from ClothoidIntegral
  public Scalar il(Scalar t) {
    Scalar c2t = c2.multiply(t);
    Scalar ofs2 = Erfi.FUNCTION.apply(_N1_1_4.multiply(c1.add(c2t).add(c2t)).multiply(f4));
    return ofs.subtract(ofs2).multiply(factor);
  }
}
