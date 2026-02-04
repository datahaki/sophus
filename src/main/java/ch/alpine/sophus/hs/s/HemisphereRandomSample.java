package ch.alpine.sophus.hs.s;

import java.util.random.RandomGenerator;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.sca.Abs;

/** random sample on n-dimensional sphere
 * random samples are vectors of length n + 1
 * 
 * <p>Reference:
 * "Spheres and Rotations" in NR, 2007
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/Sphere.html">Sphere</a> */
public class HemisphereRandomSample extends SphereRandomSample {
  public static RandomSampleInterface of(int dimension) {
    return new HemisphereRandomSample(dimension);
  }

  /** Example:
   * dimension == 0 consist of the points {1}, and {-1}
   * dimension == 1 corresponds to the unit circle in the plane
   * dimension == 2 corresponds to the "standard", surface sphere embedded in 3-dimensional Euclidean space
   * 
   * @param dimension of sphere as manifold */
  private HemisphereRandomSample(int dimension) {
    super(dimension);
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(RandomGenerator randomGenerator) {
    Tensor tensor = super.randomSample(randomGenerator);
    tensor.set(Abs.FUNCTION, tensor.length() - 1);
    return tensor;
  }
}
