// code by jph
package ch.alpine.sophus.ref.d1;

import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.Cross;
import ch.alpine.tensor.lie.r2.SignedCurvature2D;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.nrm.Vector2NormSquared;
import ch.alpine.tensor.red.Times;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.pow.Sqrt;

/* package */ enum DodgsonSabinHelper {
  ;
  static final CurveSubdivision BSPLINE3_EUCLIDEAN = new BSpline3CurveSubdivision(RnGroup.INSTANCE);
  private static final Scalar HALF = RealScalar.of(0.5);
  private static final Scalar _1_4 = RationalScalar.of(1, 4);

  /** @param b
   * @param c
   * @param d
   * @return point between b and c */
  static Tensor midpoint(Tensor b, Tensor c, Tensor d) {
    Scalar r = SignedCurvature2D.of(d, c, b).orElseThrow();
    return intersectCircleLine(b, c, r, RealScalar.ZERO);
  }

  static Tensor midpoint(Tensor a, Tensor b, Tensor c, Tensor d) {
    Scalar R = averageCurvature(a, b, c, d);
    Scalar lam = lambda(a, b, c, d, R);
    return intersectCircleLine(b, c, R, lam);
  }

  static Tensor intersectCircleLine(Tensor b, Tensor c, Scalar r, Scalar lam) {
    Tensor D = c.subtract(b);
    double d = Vector2NormSquared.of(D).number().doubleValue();
    double l2 = lam.multiply(lam).number().doubleValue();
    double R2 = r.multiply(r).number().doubleValue();
    double fa = 1 / (1 + Math.sqrt(1 - R2 * d * 0.25));
    double fb = l2 / (1 + Math.sqrt(1 - l2 * R2 * d * 0.25));
    return Total.of(Tensors.of( //
        b.add(c).multiply(HALF), //
        D.multiply(lam.multiply(HALF)), //
        Cross.of(D).multiply(RealScalar.of(r.number().doubleValue() * Math.sqrt(d) * 0.25 * (fa - fb)))));
  }

  static Scalar lambda(Tensor a, Tensor b, Tensor c, Tensor d, Scalar r) {
    Scalar ac = Vector2Norm.between(a, c);
    Scalar bd = Vector2Norm.between(b, d);
    Scalar bc = Vector2NormSquared.between(b, c); // squared
    Scalar mu = ac.divide(bd);
    Scalar res = Times.of(r, r, bc, _1_4);
    Scalar h = res.divide(RealScalar.ONE.add(Sqrt.of(RealScalar.ONE.subtract(res))));
    Scalar mu1 = mu.add(RealScalar.ONE);
    Scalar mu1_2 = mu1.multiply(mu1);
    // (1 - h * 2 * mu / mu1_2)
    Scalar den = Times.of(RealScalar.TWO, h, mu).divide(mu1_2);
    return mu.subtract(RealScalar.ONE).divide(mu.add(RealScalar.ONE)).divide(RealScalar.ONE.subtract(den));
  }

  static Scalar averageCurvature(Tensor a, Tensor b, Tensor c, Tensor d) {
    Scalar ac = Vector2Norm.between(a, c);
    Scalar bd = Vector2Norm.between(b, d);
    return SignedCurvature2D.of(c, b, a).orElseThrow().multiply(bd).add( //
        SignedCurvature2D.of(d, c, b).orElseThrow().multiply(ac)) //
        .divide(bd.add(ac));
  }
}
