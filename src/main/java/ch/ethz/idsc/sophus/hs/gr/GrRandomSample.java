// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import java.io.Serializable;
import java.util.Random;

import ch.ethz.idsc.sophus.hs.HsInfluence;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.ext.Integers;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;

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
    if (k < 1 || n < k)
      throw new IllegalArgumentException("k=" + k);
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(Random random) {
    return HsInfluence.of(RandomVariate.of(NormalDistribution.standard(), random, n, k)).matrix();
  }
}
