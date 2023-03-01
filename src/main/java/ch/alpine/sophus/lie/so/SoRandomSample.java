// code by jph
package ch.alpine.sophus.lie.so;

import java.io.Serializable;
import java.util.random.RandomGenerator;

import ch.alpine.sophus.lie.so2.So2RandomSample;
import ch.alpine.sophus.lie.so3.So3RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.mat.qr.QRDecomposition;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

/** Reference:
 * "Spheres and Rotations" in NR, 2007
 * 
 * for n == 2 and n == 3 there are faster alternatives
 * 
 * @see So2RandomSample
 * @see So3RandomSample */
public class SoRandomSample implements RandomSampleInterface, Serializable {
  /** @param n positive
   * @return random sampler in SO(n) */
  public static RandomSampleInterface of(int n) {
    return new SoRandomSample(Integers.requirePositive(n));
  }

  // ---
  private final int n;

  private SoRandomSample(int n) {
    this.n = n;
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(RandomGenerator random) {
    return QRDecomposition.of(RandomVariate.of(NormalDistribution.standard(), random, n, n)).getQConjugateTranspose();
  }
}
