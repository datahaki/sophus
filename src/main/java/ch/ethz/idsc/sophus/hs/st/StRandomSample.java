// code by jph
package ch.ethz.idsc.sophus.hs.st;

import java.io.Serializable;
import java.util.Random;

import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.ext.Integers;
import ch.ethz.idsc.tensor.mat.Orthogonalize;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;

/** uniform distribution on St(n, k) according to Haar measure
 * 
 * References:
 * geomstats - stiefel.py */
public class StRandomSample implements RandomSampleInterface, Serializable {
  /** @param n positive
   * @param k no greater than n
   * @return */
  public static RandomSampleInterface of(int n, int k) {
    return new StRandomSample(n, k);
  }

  /***************************************************/
  private final int n;
  private final int k;

  private StRandomSample(int n, int k) {
    this.n = Integers.requirePositive(n);
    this.k = k;
    if (k < 0 || n < k)
      throw new IllegalArgumentException("k=" + k);
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(Random random) {
    return Orthogonalize.usingPD(RandomVariate.of(NormalDistribution.standard(), random, k, n));
  }
}
