// code by jph
package ch.alpine.sophus.gbc.d2;

import java.io.Serializable;

import ch.alpine.sophus.hs.Genesis;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.red.Times;
import ch.alpine.tensor.sca.Chop;

/** for k == 0 the coordinates are identical to three-point coordinates with mean value as barycenter
 * 
 * mean value coordinates are C^\infty and work for non-convex polygons
 * 
 * Reference:
 * "Iterative coordinates"
 * by Chongyang Deng, Qingjun Chang, Kai Hormann, 2020
 * 
 * @param k non-negative */
public record IterativeCoordinateMatrix(int k) implements Genesis, Serializable {
  public IterativeCoordinateMatrix {
    Integers.requirePositiveOrZero(k);
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    Tensor scaling = InverseNorm.INSTANCE.origin(levers);
    Tensor matrix = DiagonalMatrix.with(scaling);
    Tensor normalized = Times.of(scaling, levers);
    Tensor midmat = Adds.matrix(levers.length());
    for (int depth = 0; depth < k; ++depth) {
      Tensor midpoints = Adds.forward(normalized);
      scaling = InverseNorm.INSTANCE.origin(midpoints);
      matrix = Times.of(scaling, midmat).dot(matrix);
      normalized = Times.of(scaling, midpoints);
    }
    Chop._10.requireClose(matrix.dot(levers), normalized);
    return matrix;
  }
}
