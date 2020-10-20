// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.io.Serializable;

import ch.ethz.idsc.sophus.gbc.Genesis;
import ch.ethz.idsc.sophus.lie.rn.RnGeodesic;
import ch.ethz.idsc.sophus.ref.d1.CurveSubdivision;
import ch.ethz.idsc.tensor.Integers;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.DiagonalMatrix;
import ch.ethz.idsc.tensor.sca.Chop;

/** for k == 0 the coordinates are identical to three-point coordinates with mean value as barycenter
 * 
 * mean value coordinates are C^\infty and work for non-convex polygons
 * 
 * Reference:
 * "Iterative coordinates"
 * by Chongyang Deng, Qingjun Chang, Kai Hormann, 2020 */
public class IterativeCoordinateMatrix implements Genesis, Serializable {
  private static final CurveSubdivision MIDPOINTS = ControlMidpoints.of(RnGeodesic.INSTANCE);

  /** @param genesis
   * @param k non-negative
   * @return */
  public static Genesis of(int k) {
    return new IterativeCoordinateMatrix(k);
  }

  /***************************************************/
  private final int k;

  /** @param genesis
   * @param k non-negative */
  private IterativeCoordinateMatrix(int k) {
    this.k = Integers.requirePositiveOrZero(k);
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    Tensor scaling = InverseNorm.INSTANCE.origin(levers);
    Tensor matrix = DiagonalMatrix.with(scaling);
    Tensor normalized = scaling.pmul(levers);
    Tensor midmat = MidpointMatrix.of(levers.length());
    for (int depth = 0; depth < k; ++depth) {
      Tensor midpoints = MIDPOINTS.cyclic(normalized);
      scaling = InverseNorm.INSTANCE.origin(midpoints);
      matrix = scaling.pmul(midmat).dot(matrix); // DiagonalMatrix.with(scaling).dot(midmat).dot(matrix)
      normalized = scaling.pmul(midpoints);
    }
    Chop._10.requireClose(matrix.dot(levers), normalized);
    return matrix;
  }
}
