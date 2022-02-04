// code by jph
package ch.alpine.sophus.hs.gr;

import java.io.Serializable;
import java.util.Random;

import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.ext.Integers;
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
public record GrRandomSample(int n, int k) implements RandomSampleInterface, Serializable {
  public GrRandomSample {
    Integers.requirePositive(n);
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
