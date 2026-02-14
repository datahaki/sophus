// code by jph
package ch.alpine.sophus.lie.se3;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.lie.rot.QuaternionToRotationMatrix;

/** code based on derivation by Ethan Eade
 * "Lie Groups for 2D and 3D Transformations", p. 8 */
public enum Se3Matrix {
  ;
  private static final Tensor UNIT_VECTOR_3 = UnitVector.of(4, 3);

  /** @param R orthogonal matrix of dimensions 3 x 3
   * @param t vector of length at least 3
   * @return matrix of dimensions 4 x 4
   * @throws Exception if first 3 entries of t are not scalars
   * @see QuaternionToRotationMatrix */
  public static Tensor of(Tensor R, Tensor t) {
    return Unprotect.byRef( //
        R.get(0).append(t.Get(0)), //
        R.get(1).append(t.Get(1)), //
        R.get(2).append(t.Get(2)), //
        UNIT_VECTOR_3.copy());
  }

  public static Tensor ofT(Tensor R, Tensor t) {
    return Unprotect.byRef( //
        R.get(0).append(t.Get(0)), //
        R.get(1).append(t.Get(1)), //
        R.get(2).append(t.Get(2)), //
        ConstantArray.of(RealScalar.ZERO, 4));
  }

  /** @param matrix of dimensions 4 x 4
   * @return 3 x 3 rotation matrix that is part of given matrix */
  public static Tensor rotation(Tensor matrix) {
    return Tensor.of(matrix.stream().limit(3).map(row -> row.extract(0, 3)));
  }

  /** @param matrix of dimensions 4 x 4
   * @return vector of length 3 that is part of given matrix */
  public static Tensor translation(Tensor matrix) {
    return Tensor.of(matrix.stream().limit(3).map(row -> row.get(3)));
  }

  public static Tensor inverse(Tensor g) {
    Tensor R = rotation(g);
    Tensor t = translation(g);
    Tensor tR = Transpose.of(R);
    return of(tR, tR.dot(t).negate());
  }
}
