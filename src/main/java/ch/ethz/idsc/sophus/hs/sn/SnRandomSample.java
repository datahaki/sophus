// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.Serializable;
import java.util.Random;

import ch.ethz.idsc.sophus.hs.s1.S1RandomSample;
import ch.ethz.idsc.sophus.math.sample.BallRandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Norm;

/** random sample on n-dimensional sphere
 * random samples are vectors of length n + 1
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/Sphere.html">Sphere</a> */
public class SnRandomSample implements RandomSampleInterface, Serializable {
  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Norm._2);

  /** Example: dimension == 1 corresponds to the unit circle in the plane
   * 
   * @param dimension of sphere as manifold
   * @return */
  public static RandomSampleInterface of(int dimension) {
    switch (dimension) {
    // TODO treat case 0 separately -> should return {-1} or {+1} with equal probability
    case 1:
      return S1RandomSample.INSTANCE; // points on the unit circle
    default:
      return new SnRandomSample(dimension);
    }
  }

  // ---
  private final RandomSampleInterface randomSampleInterface;

  private SnRandomSample(int dimension) {
    this.randomSampleInterface = BallRandomSample.of(Array.zeros(dimension + 1), RealScalar.ONE);
  }

  @Override
  public Tensor randomSample(Random random) {
    return NORMALIZE.apply(randomSampleInterface.randomSample(random));
  }
}
