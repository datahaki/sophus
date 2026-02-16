// code by jph
package ch.alpine.sophus.hs.s;

import java.io.Serializable;

import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.Rational;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Hypot;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.num.ReIm;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.AbsSquared;
import ch.alpine.tensor.sca.Conjugate;
import ch.alpine.tensor.sca.pow.Sqrt;

/** Reference: formulas taken from ChatGPT */
public record S3Hopf(Scalar z1, Scalar z2) implements Serializable {
  public S3Hopf {
    Tolerance.CHOP.requireClose( //
        Total.of(Tensors.of(z1, z2).maps(AbsSquared.FUNCTION)), RealScalar.ONE);
  }

  public static S3Hopf of(Tensor xyza) {
    Scalar z1 = ComplexScalar.of(xyza.Get(0), xyza.Get(1));
    Scalar z2 = ComplexScalar.of(xyza.Get(2), xyza.Get(3));
    return new S3Hopf(z1, z2);
  }

  public static S3Hopf stereographic(Tensor xyz) {
    Scalar x = xyz.Get(0);
    Scalar y = xyz.Get(1);
    Scalar z = xyz.Get(2);
    Scalar w = ComplexScalar.of(x, y).divide(RealScalar.ONE.add(z));
    Scalar den = Hypot.withOne(w);
    return new S3Hopf( //
        w.divide(den), //
        den.reciprocal());
  }

  public static S3Hopf northernHemisphereGauge(Tensor xyz) {
    Scalar x = xyz.Get(0);
    Scalar y = xyz.Get(1);
    Scalar z = xyz.Get(2);
    Scalar z1 = Sqrt.FUNCTION.apply(RealScalar.ONE.add(z).multiply(Rational.HALF));
    Scalar z2 = ComplexScalar.of(x, y).divide(Sqrt.FUNCTION.apply(RealScalar.ONE.add(z).multiply(RealScalar.TWO)));
    return new S3Hopf(z1, z2);
  }

  /** @return base point in S^2, i.e. a vector of length 3 with Euclidean norm 1 */
  public Tensor project() {
    Scalar a1 = AbsSquared.FUNCTION.apply(z1);
    Scalar a2 = AbsSquared.FUNCTION.apply(z2);
    Tolerance.CHOP.requireClose(a1.add(a2), RealScalar.ONE);
    Scalar f1 = z1.multiply(Conjugate.FUNCTION.apply(z2));
    ReIm reIm = ReIm.of(f1.add(f1));
    Tensor hopf = reIm.vector().append(a1.subtract(a2));
    Scalar norm = Vector2Norm.of(hopf);
    Tolerance.CHOP.requireClose(norm, RealScalar.ONE);
    return hopf;
  }

  /** @param angle
   * @return point in S^3, i.e. a vector of length 4 with Euclidean norm 1 */
  public Tensor lift(Scalar angle) {
    Scalar expith = ComplexScalar.unit(angle);
    return Join.of( //
        ReIm.of(z1.multiply(expith)).vector(), //
        ReIm.of(z2.multiply(expith)).vector());
  }
}
