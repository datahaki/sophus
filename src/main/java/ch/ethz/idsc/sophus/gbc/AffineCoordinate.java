// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.math.AppendOne;
import ch.ethz.idsc.sophus.math.Genesis;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Unprotect;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.mat.CholeskyDecomposition;

/** Reference:
 * "Affine generalised barycentric coordinates"
 * by S. Waldron, Jaen Journal on Approximation, 3(2):209-226, 2011
 * 
 * Our implementation makes use of the fact that the n weights are an
 * affine linear combination (encoded by the coefficient vector z of
 * length d+1) in the lever coordinates:
 * weights == [levers | ones] . z */
public enum AffineCoordinate implements Genesis {
  INSTANCE;

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    Tensor x = Tensor.of(levers.stream().map(AppendOne.FUNCTION));
    int d = Unprotect.dimension1Hint(levers);
    Tensor u = UnitVector.of(d + 1, d);
    Tensor matrix = Transpose.of(x).dot(x);
    Tensor z = CholeskyDecomposition.of(matrix).solve(u);
    return x.dot(z);
  }
}
