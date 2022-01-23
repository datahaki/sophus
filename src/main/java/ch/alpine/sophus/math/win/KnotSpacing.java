// code by ob, jph
package ch.alpine.sophus.math.win;

import java.util.Objects;

import ch.alpine.sophus.math.TensorMetric;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Accumulate;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.sca.Power;
import ch.alpine.tensor.sca.Sign;

/** Reference:
 * B-Spline Interpolation and Approximation
 * Hongxin Zhang and Jieqing Feng
 * http://www.cad.zju.edu.cn/home/zhx/GM/009/00-bsia.pdf */
public record KnotSpacing(TensorMetric tensorMetric, ScalarUnaryOperator variogram) implements TensorUnaryOperator {
  /** @param tensorMetric for instance Se2ParametricDistance.INSTANCE
   * @param exponent typically in the interval [0, 1] */
  public static TensorUnaryOperator centripetal(TensorMetric tensorMetric, Scalar exponent) {
    return new KnotSpacing(Objects.requireNonNull(tensorMetric), Power.function(exponent));
  }

  /** @param tensorMetric for instance Se2ParametricDistance.INSTANCE
   * @param exponent in the interval [0, 1] */
  public static TensorUnaryOperator centripetal(TensorMetric tensorMetric, Number exponent) {
    return centripetal(tensorMetric, RealScalar.of(exponent));
  }

  // ---
  private static final TensorUnaryOperator UNIFORM = tensor -> Range.of(0, tensor.length());

  /** uniform knot spacing is equivalent to centripetal with exponent == 0
   * 
   * @return */
  public static TensorUnaryOperator uniform() {
    return UNIFORM;
  }

  /** chordal knot spacing is equivalent to centripetal with exponent == 1
   * 
   * @param tensorMetric non-null
   * @return */
  public static TensorUnaryOperator chordal(TensorMetric tensorMetric) {
    return new KnotSpacing(Objects.requireNonNull(tensorMetric), scalar -> scalar);
  }

  // ---
  public KnotSpacing {
    Objects.requireNonNull(tensorMetric);
    Objects.requireNonNull(variogram);
  }

  @Override // from TensorUnaryOperator
  public Tensor apply(Tensor control) {
    Tensor knots = Tensors.reserve(control.length());
    if (0 < control.length()) {
      Tensor prev = control.get(0);
      knots.append(variogram.apply(tensorMetric.distance(prev, prev)).zero());
      for (int index = 1; index < control.length(); ++index)
        knots.append(Sign.requirePositiveOrZero(variogram.apply(tensorMetric.distance(prev, prev = control.get(index)))));
    }
    return Accumulate.of(knots);
  }
}
