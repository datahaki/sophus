// code by jph
package ch.alpine.sophus.hs.st;

import java.io.Serializable;
import java.util.Random;

import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.Orthogonalize;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

/** uniform distribution on St(n, k) according to Haar measure
 * 
 * References:
 * geomstats - stiefel.py */
public class StRandomSample implements RandomSampleInterface, Serializable {
  /** @param n positive
   * @param k non-negative, no greater than n
   * @return matrix that satisfies {@link OrthogonalMatrixQ} typically up to precision of 1e-10
   * @see StMemberQ */
  public static RandomSampleInterface of(int n, int k) {
    return new StRandomSample(n, k);
  }

  // ---
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
