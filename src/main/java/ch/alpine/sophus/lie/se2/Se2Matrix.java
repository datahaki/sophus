// code by jph
package ch.alpine.sophus.lie.se2;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.SquareMatrixQ;
import ch.alpine.tensor.sca.ArcTan;
import ch.alpine.tensor.sca.Cos;
import ch.alpine.tensor.sca.Sin;

public enum Se2Matrix {
  ;
  /** maps a vector from the group SE2 to a matrix in SE2
   * 
   * @param g = {px, py, angle}
   * @return matrix with dimensions 3x3
   * <pre>
   * [+Ca -Sa px]
   * [+Sa +Ca py]
   * [0 0 1]
   * </pre>
   * @throws Exception if parameter g is not a vector of length 3 */
  public static Tensor of(Tensor xya) {
    Scalar angle = xya.Get(2);
    Scalar cos = Cos.FUNCTION.apply(angle);
    Scalar sin = Sin.FUNCTION.apply(angle);
    return Tensors.matrix(new Scalar[][] { //
        { cos, sin.negate(), xya.Get(0) }, //
        { sin, cos /*----*/, xya.Get(1) }, //
        { RealScalar.ZERO, RealScalar.ZERO, RealScalar.ONE }, //
    });
  }

  /** maps a matrix from the group SE2 to a vector in the group SE2
   * 
   * @param matrix
   * @return {px, py, angle} */
  public static Tensor toVector(Tensor matrix) {
    SquareMatrixQ.require(matrix);
    return Tensors.of(matrix.Get(0, 2), matrix.Get(1, 2), //
        ArcTan.of(matrix.Get(0, 0), matrix.Get(1, 0))); // arc tan is numerically stable
  }
}
