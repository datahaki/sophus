// code by jph
package ch.ethz.idsc.sophus.lie.son;

import java.io.Serializable;
import java.util.Random;

import ch.ethz.idsc.sophus.lie.so2.So2RandomSample;
import ch.ethz.idsc.sophus.lie.so3.So3RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Integers;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.QRDecomposition;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;

/** Reference:
 * "Spheres and Rotations" in NR, 2007
 * 
 * for n == 2 and n == 3 there are faster alternatives
 * 
 * @see So2RandomSample
 * @see So3RandomSample */
public class SonRandomSample implements RandomSampleInterface, Serializable {
  /** @param n positive
   * @return */
  public static RandomSampleInterface of(int n) {
    return new SonRandomSample(Integers.requirePositive(n));
  }

  /***************************************************/
  private final int n;

  private SonRandomSample(int n) {
    this.n = n;
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(Random random) {
    return QRDecomposition.of(RandomVariate.of(NormalDistribution.standard(), random, n, n)).getInverseQ();
  }
}
