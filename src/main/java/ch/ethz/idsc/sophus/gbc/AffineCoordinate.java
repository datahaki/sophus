// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.math.AppendOne;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.mat.LeastSquares;
import ch.ethz.idsc.tensor.mat.LinearSolve;

/** Reference:
 * "Affine generalised barycentric coordinates"
 * by S. Waldron, Jaen Journal on Approximation, 3(2):209-226, 2011 */
public enum AffineCoordinate implements Genesis {
  INSTANCE;

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    Tensor x = Tensor.of(levers.stream().map(AppendOne.FUNCTION));
    int d = levers.get(0).length();
    return x.dot(LinearSolve.of(Transpose.of(x).dot(x), UnitVector.of(d + 1, d)));
  }

  public static Tensor defect(Tensor levers, Tensor weights) {
    Tensor x = Tensor.of(levers.stream().map(AppendOne.FUNCTION));
    return x.dot(LeastSquares.of(x, weights)).subtract(weights);
  }
}
