// code by jph
package ch.alpine.sophus.lie.se2;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.mat.SquareMatrixQ;
import ch.alpine.tensor.sca.tri.ArcTan;
import ch.alpine.tensor.sca.tri.Cos;
import ch.alpine.tensor.sca.tri.Sin;

public enum Se2Matrix {
  ;
  /** maps a vector from the group SE2 to a matrix in SE2
   * 
   * @param xya = {px, py, angle}
   * @return matrix with dimensions 3x3
   * <pre>
   * [+Ca -Sa px]
   * [+Sa +Ca py]
   * [0 0 1]
   * </pre>
   * @throws Exception if parameter g is not a vector of length 3 */
  public static Tensor of(Tensor xya) {
    Scalar x = xya.Get(0);
    Scalar y = xya.Get(1);
    Scalar zx = Unprotect.zero_negateUnit(x);
    Scalar zy = Unprotect.zero_negateUnit(y);
    Scalar angle = xya.Get(2);
    Scalar cos = Cos.FUNCTION.apply(angle);
    Scalar sin = Sin.FUNCTION.apply(angle);
    return Tensors.matrix(new Scalar[][] { //
        { cos, sin.negate(), x }, //
        { sin, cos /*----*/, y }, //
        { zx, zy, RealScalar.ONE }, //
    });
  }

  /** maps a matrix from the group SE2 to a vector in the group SE2
   * 
   * @param matrix
   * @return {px, py, angle} */
  public static Tensor toVector(Tensor matrix) {
    SquareMatrixQ.INSTANCE.require(matrix);
    return Tensors.of(matrix.Get(0, 2), matrix.Get(1, 2), //
        ArcTan.of(matrix.Get(0, 0), matrix.Get(1, 0))); // arc tan is numerically stable
  }

  /** @param xy of the form {px, py, ...}
   * @return
   * <pre>
   * [1 0 px]
   * [0 1 py]
   * [0 0 1]
   * </pre> */
  public static Tensor translation(Tensor xy) {
    Scalar x = xy.Get(0);
    Scalar y = xy.Get(1);
    Scalar zx = Unprotect.zero_negateUnit(x);
    Scalar zy = Unprotect.zero_negateUnit(y);
    return Tensors.matrix(new Scalar[][] { //
        { RealScalar.ONE, RealScalar.ZERO, x }, //
        { RealScalar.ZERO, RealScalar.ONE, y }, //
        { zx, zy, RealScalar.ONE }, //
    });
  }

  /** @param s for instance 1[m^-1]
   * @return matrix of size 3 x 3 that is used to convert from
   * model coordinates to pixel coordinates */
  public static Tensor model2pixel(Scalar s) {
    return Tensors.matrix(new Scalar[][] { //
        { s, s.zero(), RealScalar.ZERO }, //
        { s.zero(), s, RealScalar.ZERO }, //
        { s.zero(), s.zero(), RealScalar.ONE }, //
    });
  }

  /** Hint: function is useful to construct a pixel2model matrix
   * for an image of given height.
   * 
   * @param height
   * @return matrix with determinant -1
   * <pre>
   * [1 0 0]
   * [0 -1 height]
   * [0 0 1]
   * </pre> */
  public static Tensor flipY(int height) {
    return Tensors.matrix(new Scalar[][] { //
        { RealScalar.ONE, RealScalar.ZERO, RealScalar.ZERO }, //
        { RealScalar.ZERO, RealScalar.ONE.negate(), RealScalar.of(height) }, //
        { RealScalar.ZERO, RealScalar.ZERO, RealScalar.ONE }, //
    });
  }
}
