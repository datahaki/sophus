// code by jph
package ch.alpine.sophus.hs.gr;

import java.io.Serializable;
import java.util.Random;

import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.mat.gr.InfluenceMatrix;
import ch.alpine.tensor.pdf.NormalDistribution;
import ch.alpine.tensor.pdf.RandomVariate;

/** uniform distribution on Gr(n, k)
 * 
 * References:
 * geomstats
 * 
 * "Statistics on Special Manifolds"
 * by Yasuko Chikuse, 2003 */
public class GrRandomSample implements RandomSampleInterface, Serializable {
  /** @param n positive
   * @param k no greater than n
   * @return */
  public static RandomSampleInterface of(int n, int k) {
    return new GrRandomSample(n, k);
  }

  /***************************************************/
  private final int n;
  private final int k;

  private GrRandomSample(int n, int k) {
    this.n = Integers.requirePositive(n);
    this.k = k;
    if (k < 0 || n < k)
      throw new IllegalArgumentException("k=" + k);
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(Random random) {
    return 0 < k //
        ? InfluenceMatrix.of(RandomVariate.of(NormalDistribution.standard(), random, n, k)).matrix()
        : Array.zeros(n, n);
  }
}
