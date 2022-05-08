// code by jph
package ch.alpine.sophus.hs.rs;

import java.util.Optional;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.NormalizeUnlessZero;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.num.Polynomial;
import ch.alpine.tensor.red.Times;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.red.VectorAngle;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.tri.Cos;

/** Reference:
 * "Geometric Hermite Interpolation in Rn by Refinements"
 * by Hofit Ben-Zion Vardi, Nira Dyn, Nir Sharon, 2022 */
public enum RnSBezierSplit implements GeodesicSpace {
  METHOD_0 {
    @Override
    protected Scalar theta(Tensor p0, Tensor v0, Tensor p1, Tensor v1) {
      Tensor d01 = p1.subtract(p0);
      // Scalar n01 = Vector2Norm.of(d01);
      // if (Tolerance.CHOP.isZero(n01))
      //
      Optional<Scalar> t0 = VectorAngle.of(v0, d01);
      Optional<Scalar> t1 = VectorAngle.of(v1, d01);
      if (t0.isEmpty() || t1.isEmpty())
        return RealScalar.ZERO;
      return t0.orElseThrow().add(t1.orElseThrow()).divide(RealScalar.of(4));
    }
  }, //
  METHOD_1 {
    @Override
    protected Scalar theta(Tensor p0, Tensor v0, Tensor p1, Tensor v1) {
      Optional<Scalar> t0 = VectorAngle.of(v0, v1);
      return t0.orElseThrow().divide(RealScalar.of(4));
    }
  }, //
  ;

  private static final TensorUnaryOperator NORMALIZE = NormalizeUnlessZero.with(Vector2Norm::of);

  protected abstract Scalar theta(Tensor p0, Tensor v0, Tensor p1, Tensor v1);

  @Override
  public ScalarTensorFunction curve(Tensor pv0, Tensor pv1) {
    // TODO SOPHUS IMPL this can be improved
    return t -> split(pv0, pv1, t);
  }

  @Override
  public Tensor split(Tensor pv0, Tensor pv1, Scalar t) {
    Tensor p0 = pv0.get(0);
    Tensor v0 = pv0.get(1);
    Tensor p1 = pv1.get(0);
    Tensor v1 = pv1.get(1);
    // ---
    Tolerance.CHOP.requireClose(Vector2Norm.of(v0), RealScalar.ONE);
    Tolerance.CHOP.requireClose(Vector2Norm.of(v1), RealScalar.ONE);
    Scalar cos = Cos.FUNCTION.apply(theta(p0, v0, p1, v1));
    Scalar alpha = Chop._12.isZero(cos) //
        ? RealScalar.ZERO
        : Vector2Norm.between(p0, p1).divide(cos.multiply(cos).multiply(RealScalar.of(3)));
    // ---
    Scalar omt = RealScalar.ONE.subtract(t);
    Tensor p;
    {
      // (1-t)**2*(1+2*t)
      // Scalar ap0 = Polynomial.of(Tensors.vector(1, 0, -3, 2)).apply(t);
      Scalar fp0 = Times.of(omt, omt, RealScalar.ONE.add(t).add(t));
      // Tolerance.CHOP.requireClose(ap0, fp0);
      // 3*alpha*t*(1-t)**2
      // Scalar av0 = Polynomial.of(Tensors.vector(0, 3, -6, 3).multiply(alpha)).apply(t);
      Scalar fv0 = Times.of(RealScalar.of(3), alpha, t, omt, omt);
      // Tolerance.CHOP.requireClose(av0, fv0);
      // t**2*(3-2*t)
      // Scalar ap1 = Polynomial.of(Tensors.vector(0, 0, 3, -2)).apply(t);
      Scalar fp1 = Times.of(t, t, RealScalar.of(3).subtract(t).subtract(t));
      // Tolerance.CHOP.requireClose(ap1, fp1);
      // -3*alpha*t**2*(1-t)
      // Scalar av1 = Polynomial.of(Tensors.vector(0, 0, -3, 3).multiply(alpha)).apply(t);
      Scalar fv1 = Times.of(RealScalar.of(-3), alpha, t, t, omt);
      // Tolerance.CHOP.requireClose(av1, fv1);
      p = Total.of(Tensors.of( //
          p0.multiply(fp0), //
          v0.multiply(fv0), //
          p1.multiply(fp1), //
          v1.multiply(fv1)));
    }
    Tensor v;
    {
      // 2*t*(t-1)
      // Scalar ap0 = Polynomial.of(Tensors.vector(0, -2, 2)).apply(t);
      Scalar gp0 = Times.of(RealScalar.of(-2), t, omt);
      // Tolerance.CHOP.requireClose(ap0, gp0);
      // alpha*(3*t**2-4*t+1)
      Scalar gv0 = Polynomial.of(Tensors.vector(1, -4, 3).multiply(alpha)).apply(t);
      // 2*t*(1-t)
      // Scalar ap1 = Polynomial.of(Tensors.vector(0, 2, -2)).apply(t);
      Scalar gp1 = Times.of(RealScalar.of(2), t, omt);
      // Tolerance.CHOP.requireClose(ap1, gp1);
      // -alpha*t*(2-3*t)
      Scalar gv1 = Polynomial.of(Tensors.vector(0, -2, 3).multiply(alpha)).apply(t);
      v = NORMALIZE.apply(Total.of(Tensors.of( //
          p0.multiply(gp0), //
          v0.multiply(gv0), //
          p1.multiply(gp1), //
          v1.multiply(gv1))));
    }
    return Tensors.of(p, v);
  }
}
