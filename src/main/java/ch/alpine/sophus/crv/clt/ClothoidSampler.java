// code by jph
package ch.alpine.sophus.crv.clt;

import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.InverseCDF;
import ch.alpine.tensor.pdf.c.EqualizingDistribution;
import ch.alpine.tensor.sca.Abs;
import ch.alpine.tensor.sca.Ceiling;
import ch.alpine.tensor.sca.Sign;
import ch.alpine.tensor.sca.pow.Sqrt;

/** suitable for drawing */
public enum ClothoidSampler {
  ;
  private static final Scalar _0 = RealScalar.of(0.0);
  private static final Scalar _1 = RealScalar.of(1.0);
  private static final int MAX_INTERVALS = 511;

  /** @param clothoid
   * @param minResolution for instance 0.08
   * @return */
  public static Tensor of(Clothoid clothoid, Scalar minResolution) {
    return samples(clothoid, minResolution).map(clothoid);
  }

  public static Tensor samples(Clothoid clothoid, Scalar minResolution) {
    Sign.requirePositive(minResolution);
    LagrangeQuadraticD lagrangeQuadraticD = clothoid.curvature();
    if (lagrangeQuadraticD.isZero(Tolerance.CHOP))
      return UnitVector.of(2, 1);
    Scalar scalar = Sqrt.FUNCTION.apply(lagrangeQuadraticD.integralAbs().divide(minResolution)).multiply(clothoid.length());
    int intervals = Ceiling.intValueExact(scalar);
    Tensor uniform = Subdivide.of(_0, _1, Integers.clip(1, MAX_INTERVALS).applyAsInt(intervals));
    InverseCDF inverseCDF = (InverseCDF) EqualizingDistribution.fromUnscaledPDF( //
        uniform.map(lagrangeQuadraticD).map(Abs.FUNCTION).map(Sqrt.FUNCTION));
    return uniform.map(inverseCDF::quantile).divide(DoubleScalar.of(uniform.length()));
  }
}
