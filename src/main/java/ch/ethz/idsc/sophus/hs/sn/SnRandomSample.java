// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.Serializable;
import java.util.Random;

import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Integers;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.red.Norm;

/** random sample on n-dimensional sphere
 * random samples are vectors of length n + 1
 * 
 * <p>Reference:
 * "Spheres and Rotations" in NR, 2007
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/Sphere.html">Sphere</a> */
public class SnRandomSample implements RandomSampleInterface, Serializable {
  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Norm._2);

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

  /***************************************************/
  private final int length;

  private SnRandomSample(int dimension) {
    this.length = dimension + 1;
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(Random random) {
    return NORMALIZE.apply(RandomVariate.of(NormalDistribution.standard(), length));
  }
}
