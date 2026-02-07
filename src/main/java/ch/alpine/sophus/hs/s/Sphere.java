// code by jph
package ch.alpine.sophus.hs.s;

import java.util.random.RandomGenerator;

import ch.alpine.sophus.hs.SpecificManifold;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.pdf.RandomSampleInterface;

/** random sample on n-dimensional sphere
 * random samples are vectors of length n + 1
 * 
 * <p>Reference:
 * "Spheres and Rotations" in NR, 2007
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/Sphere.html">Sphere</a> */
public class Sphere extends SnManifold implements SpecificManifold {
  private final int dimensions;
  private final int length;
  private final RandomSampleInterface randomSampleInterface;

  /** Example:
   * dimension == 0 consist of the points {1}, and {-1}
   * dimension == 1 corresponds to the unit circle in the plane
   * dimension == 2 corresponds to the "standard", surface sphere embedded in 3-dimensional Euclidean space
   * 
   * @param dimensions of sphere as manifold */
  public Sphere(int dimensions) {
    this.dimensions = Integers.requirePositiveOrZero(dimensions);
    this.length = dimensions + 1;
    randomSampleInterface = SphereRandomSample.of(dimensions);
  }

  @Override // from MemberQ
  public boolean isMember(Tensor p) {
    return p.length() == length //
        && super.isMember(p);
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return randomSampleInterface.randomSample(randomGenerator);
  }

  @Override
  public int dimensions() {
    return dimensions;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("S", dimensions);
  }
}
