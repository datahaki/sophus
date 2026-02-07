// code by jph
package ch.alpine.sophus.hs.s;

import java.io.Serializable;
import java.util.random.RandomGenerator;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.lie.rot.AngleVector;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Clips;

/** random sample on n-dimensional sphere
 * random samples are vectors of length n + 1
 * 
 * <p>Reference:
 * "Spheres and Rotations" in NR, 2007
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/Sphere.html">Sphere</a> */
public class SphereRandomSample implements RandomSampleInterface, Serializable {
  private static final Distribution DISTRIBUTION = UniformDistribution.of(Clips.absolute(Pi.VALUE));

  public static RandomSampleInterface of(int dimension) {
    return new SphereRandomSample(dimension);
  }

  // ---
  private final int length;

  /** Example:
   * dimension == 0 consist of the points {1}, and {-1}
   * dimension == 1 corresponds to the unit circle in the plane
   * dimension == 2 corresponds to the "standard", surface sphere embedded in 3-dimensional Euclidean space
   * 
   * @param dimension of sphere as manifold */
  protected SphereRandomSample(int dimension) {
    this.length = Integers.requirePositiveOrZero(dimension) + 1;
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(RandomGenerator randomGenerator) {
    // ignore the risk that random vector could be (0, 0, ..., 0)
    return length == 2 //
        ? AngleVector.of(RandomVariate.of(DISTRIBUTION, randomGenerator))
        : Vector2Norm.NORMALIZE.apply(RandomVariate.of(NormalDistribution.standard(), randomGenerator, length));
  }
}
