// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import java.io.Serializable;

import ch.ethz.idsc.tensor.ComplexScalar;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.opt.Pi;
import ch.ethz.idsc.tensor.pdf.Erfi;
import ch.ethz.idsc.tensor.red.Times;
import ch.ethz.idsc.tensor.sca.Exp;
import ch.ethz.idsc.tensor.sca.Sqrt;

/* package */ class ErfClothoidIntegral implements ClothoidIntegral, Serializable {
  private static final Scalar _N1_1_4 = ComplexScalar.of(+0.7071067811865476, 0.7071067811865475);
  private static final Scalar _N1_3_4 = ComplexScalar.of(-0.7071067811865475, 0.7071067811865476);
  private static final Scalar _1_4 = RationalScalar.of(1, 4);

  public static ClothoidIntegral interp(LagrangeQuadratic lagrangeQuadratic) {
    return of(lagrangeQuadratic.c0, lagrangeQuadratic.c1, lagrangeQuadratic.c2);
  }

  public static ClothoidIntegral of(Scalar c0, Scalar c1, Scalar c2) {
    if (Scalars.isZero(c2))
      return Scalars.isZero(c1) //
          ? new LineIntegral(c0)
          : new CircleIntegral(c0, c1);
    return new ErfClothoidIntegral(c0, c1, c2);
  }

  /***************************************************/
  private final Scalar c1;
  private final Scalar c2;
  private final Scalar f4;
  private final Scalar factor;
  private final Scalar ofs;

  private ErfClothoidIntegral(Scalar c0, Scalar c1, Scalar c2) {
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
  public Scalar normalized(Scalar t) {
    return il(t).divide(one());
  }

  @Override // from ClothoidIntegral
  public Scalar il(Scalar t) {
    Scalar c2t = c2.multiply(t);
    Scalar ofs2 = Erfi.FUNCTION.apply(_N1_1_4.multiply(c1.add(c2t).add(c2t)).multiply(f4));
    return ofs.subtract(ofs2).multiply(factor);
  }

  @Override // from ClothoidIntegral
  public Scalar one() {
    return il(RealScalar.ONE);
  }
}
