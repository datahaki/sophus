// code by jph
package ch.alpine.sophus.hs.gr;

import java.util.random.RandomGenerator;

import ch.alpine.sophus.math.api.SpecificManifold;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.mat.gr.InfluenceMatrix;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

/** uniform distribution on Gr(n, k)
 * 
 * References:
 * geomstats
 * 
 * "Statistics on Special Manifolds"
 * by Yasuko Chikuse, 2003
 * 
 * @param n positive
 * @param k no greater than n */
public class Grassmannian extends GrManifold implements SpecificManifold {
  private final int n;
  private final int k;

  /** @param n as in n x n projection matrices
   * @param k each matrix with k ev equals 1 and (n-k) ev equals 0 */
  public Grassmannian(int n, int k) {
    this.n = Integers.requirePositive(n);
    this.k = Integers.requirePositiveOrZero(k);
    Integers.requireLessEquals(k, n);
  }

  @Deprecated
  public Tensor matrixBasis() { // if this is the tangent vector at Id ?
    Tensor basis = Tensors.empty();
    for (int i = 0; i < k; ++i)
      for (int j = k; j < n; ++j) {
        Tensor elem = Array.sparse(n, n);
        elem.set(RealScalar.ONE, i, j);
        elem.set(RealScalar.ONE.negate(), j, i);
        basis.append(elem);
      }
    for (int i = 0; i < n; ++i)
      for (int j = i + 1; j < n; ++j)
        if (k <= i || j < k) {
          Tensor elem = Array.sparse(n, n);
          elem.set(RealScalar.ONE, i, j);
          elem.set(RealScalar.ONE.negate(), j, i);
          basis.append(elem);
        }
    return basis;
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return 0 < k //
        ? InfluenceMatrix.of(RandomVariate.of(NormalDistribution.standard(), randomGenerator, n, k)).matrix()
        : Array.zeros(n, n);
  }

  @Override
  public int dimensions() {
    return k * (n - k);
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("Gr", n, k);
  }
}
