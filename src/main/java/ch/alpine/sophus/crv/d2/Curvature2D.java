// code by jph
package ch.alpine.sophus.crv.d2;

import ch.alpine.sophus.hs.r2.SignedCurvature2D;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.red.EqualsReduce;

/** @see CurvatureComb */
public enum Curvature2D {
  ;
  private static final TripleReduceExtrapolation TRIPLE_REDUCE_EXTRAPOLATION = //
      new TripleReduceExtrapolation() {
        @Override
        protected Scalar reduce(Tensor p, Tensor q, Tensor r) {
          return SignedCurvature2D.of(p, q, r).orElseGet(() -> fallback(p, q, r));
        }

        private Scalar fallback(Tensor p, Tensor q, Tensor r) {
          return Unprotect.negateUnit(EqualsReduce.zero(Flatten.of(p, q, r)));
        }
      };

  /** @param points of the form {{p1x, p1y}, {p2x, p2y}, ..., {pNx, pNy}}
   * @return vector with same length as points */
  public static Tensor string(Tensor points) {
    return TRIPLE_REDUCE_EXTRAPOLATION.apply(points);
  }
}
