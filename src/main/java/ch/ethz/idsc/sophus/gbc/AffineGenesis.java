// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Append;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.mat.LinearSolve;

/** Reference:
 * "Affine generalised barycentric coordinates"
 * by S. Waldron, Jaen Journal on Approximation, 3(2):209-226, 2011 */
public enum AffineGenesis implements Genesis {
  INSTANCE;

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    Tensor x = Tensor.of(levers.stream().map(lever -> Append.of(lever, RealScalar.ONE)));
    int d = levers.get(0).length();
    return x.dot(LinearSolve.of(Transpose.of(x).dot(x), UnitVector.of(d + 1, d)));
  }
}
