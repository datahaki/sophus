// code by jph
package ch.alpine.sophus.gbc;

import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.AppendOne;
import ch.alpine.sophus.math.Genesis;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.cd.CholeskyDecomposition;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.sca.Chop;

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
    Tensor weights = x.dot(z);
    AffineQ.require(weights, Chop._02);
    return NormalizeTotal.FUNCTION.apply(weights);
  }
}
