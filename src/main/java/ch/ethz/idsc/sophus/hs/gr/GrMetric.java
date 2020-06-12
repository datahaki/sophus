// code by jph 
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.red.Trace;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** Reference:
 * "Geometric mean and geodesic regression on Grassmannians"
 * E. Batzies, K. Hueper, L. Machado, F. Silva Leite by 2015 */
public enum GrMetric implements TensorMetric {
  INSTANCE;

  private static final Scalar NEGATIVE_QUARTER = RationalScalar.of(-1, 4);
  private static final int MAX_ITERATIONS = 500;

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    Tensor id = IdentityMatrix.of(p.length());
    Tensor matrix = id.subtract(q.add(q)).dot(id.subtract(p.add(p)));
    Tensor log = log(matrix);
    return Sqrt.FUNCTION.apply(Trace.of(log.dot(log)).multiply(NEGATIVE_QUARTER));
  }

  /** @param matrix square
   * @return
   * @throws Exception if given matrix is non-square */
  /* package */ static Tensor log(Tensor matrix) {
    Tensor x = matrix.subtract(IdentityMatrix.of(matrix.length()));
    Tensor nxt = x;
    Tensor sum = nxt;
    for (int k = 2; k < MAX_ITERATIONS; ++k) {
      nxt = nxt.dot(x);
      Tensor prv = sum;
      sum = sum.add(nxt.divide(RealScalar.of(k % 2 == 0 ? -k : k)));
      if (Chop.NONE.close(sum, prv))
        return sum;
    }
    System.err.println("CONVERGENCE FAILURE");
    throw TensorRuntimeException.of(matrix); // insufficient convergence
  }
}
