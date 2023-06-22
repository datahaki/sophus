// code by jph
package ch.alpine.sophus.lie.se3;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.num.Boole;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.N;
import ch.alpine.tensor.sca.ply.Polynomial;
import ch.alpine.tensor.sca.tri.Cos;
import ch.alpine.tensor.sca.tri.Sinc;

/** The computation of the exponential and logarithm functions for SE3
 * require the evaluation of taylor series to prevent numerical
 * instability
 * 
 * from "Lie Groups for 2D and 3D Transformations" by Ethan Eade
 * http://ethaneade.com/ */
/* package */ class Se3Numerics {
  private static final ScalarUnaryOperator SERIES1 = Polynomial.of(Tensors.fromString( //
      "{1/2, 0, -1/24, 0, 1/720, 0, -1/40320, 0, 1/3628800, 0, -1/479001600, 0, 1/87178291200, 0, -1/20922789888000}").map(N.DOUBLE));
  private static final ScalarUnaryOperator SERIES2 = Polynomial.of(Tensors.fromString( //
      "{1/6, 0, -1/120, 0, 1/5040, 0, -1/362880, 0, 1/39916800, 0, -1/6227020800, 0, 1/1307674368000, 0, -1/355687428096000}").map(N.DOUBLE));
  private static final ScalarUnaryOperator SERIES3 = Polynomial.of(Tensors.fromString( //
      "{1/12, 0, 1/720, 0, 1/30240, 0, 1/1209600, 0, 1/47900160, 0, 691/1307674368000, 0, 1/74724249600, 0, 3617/10670622842880000}").map(N.DOUBLE));
  final boolean series;
  final Scalar A;
  final Scalar B;
  final Scalar C;
  /** D is only used in log function */
  final Scalar D;

  public Se3Numerics(Scalar theta) {
    A = Sinc.FUNCTION.apply(theta);
    Scalar theta2 = theta.multiply(theta);
    series = Chop._04.isZero(theta2);
    if (series) {
      B = SERIES1.apply(theta);
      C = SERIES2.apply(theta);
      D = SERIES3.apply(theta);
    } else {
      B = RealScalar.ONE.subtract(Cos.FUNCTION.apply(theta)).divide(theta2);
      C = RealScalar.ONE.subtract(A).divide(theta2);
      // D is difficult to evaluate
      D = RealScalar.ONE.subtract(A.divide(B.add(B))).divide(theta2);
    }
  }

  // for testing
  Tensor vector() {
    return Tensors.of(Boole.of(series), A, B, C, D);
  }
}
