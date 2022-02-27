// code by jph
package ch.alpine.sophus.hs.sn;

import java.io.Serializable;
import java.util.Random;

import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

/** random sample on n-dimensional sphere
 * random samples are vectors of length n + 1
 * 
 * <p>Reference:
 * "Spheres and Rotations" in NR, 2007
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/Sphere.html">Sphere</a> */
public class SnRandomSample implements RandomSampleInterface, Serializable {
  /** Example:
   * dimension == 1 corresponds to the unit circle in the plane
   * dimension == 2 corresponds to the "standard", surface sphere embedded in 3-dimensional Euclidean space
   * 
   * @param dimension of sphere as manifold
   * @return */
  public static RandomSampleInterface of(int dimension) {
    return dimension == 1 //
        ? S1RandomSample.INSTANCE // points on the unit circle
        : new SnRandomSample(Integers.requirePositiveOrZero(dimension));
  }

  // ---
  private final int length;

  private SnRandomSample(int dimension) {
    this.length = dimension + 1;
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(Random random) {
    // ignore the risk that random vector could be (0, 0, ..., 0)
    return Vector2Norm.NORMALIZE.apply(RandomVariate.of(NormalDistribution.standard(), random, length));
  }
}
