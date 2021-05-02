// code by jph
package ch.alpine.sophus.ply.d2;

import java.io.Serializable;

import ch.alpine.sophus.math.Genesis;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.sca.Chop;

/** for k == 0 the coordinates are identical to three-point coordinates with mean value as barycenter
 * 
 * mean value coordinates are C^\infty and work for non-convex polygons
 * 
 * Reference:
 * "Iterative coordinates"
 * by Chongyang Deng, Qingjun Chang, Kai Hormann, 2020 */
public class IterativeCoordinateMatrix implements Genesis, Serializable {
  /** @param genesis
   * @param k non-negative
   * @return */
  public static Genesis of(int k) {
    return new IterativeCoordinateMatrix(Integers.requirePositiveOrZero(k));
  }

  /***************************************************/
  private final int k;

  /** @param genesis
   * @param k non-negative */
  private IterativeCoordinateMatrix(int k) {
    this.k = k;
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    Tensor scaling = InverseNorm.INSTANCE.origin(levers);
    Tensor matrix = DiagonalMatrix.with(scaling);
    Tensor normalized = scaling.pmul(levers);
    Tensor midmat = Adds.matrix(levers.length());
    for (int depth = 0; depth < k; ++depth) {
      Tensor midpoints = Adds.forward(normalized);
      scaling = InverseNorm.INSTANCE.origin(midpoints);
      matrix = scaling.pmul(midmat).dot(matrix);
      normalized = scaling.pmul(midpoints);
    }
    Chop._10.requireClose(matrix.dot(levers), normalized);
    return matrix;
  }
}
