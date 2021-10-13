// code by jph
package ch.alpine.sophus.crv.d2;

import java.util.List;
import java.util.stream.Collectors;

import ch.alpine.sophus.math.TripleReduceExtrapolation;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.lie.r2.SignedCurvature2D;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.QuantityUnit;
import ch.alpine.tensor.qty.Unit;

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
          List<Unit> list = Flatten.of(p, q, r).stream() //
              .map(Scalar.class::cast) //
              .map(QuantityUnit::of) //
              .distinct() //
              .limit(2) //
              .collect(Collectors.toList());
          Integers.requireEquals(list.size(), 1);
          return Quantity.of(0, list.get(0).negate());
        }
      };

  /** @param points of the form {{p1x, p1y}, {p2x, p2y}, ..., {pNx, pNy}}
   * @return vector with same length as points */
  public static Tensor string(Tensor points) {
    return TRIPLE_REDUCE_EXTRAPOLATION.apply(points);
  }
}
