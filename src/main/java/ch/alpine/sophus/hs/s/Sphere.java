// code by jph
package ch.alpine.sophus.hs.s;

import ch.alpine.sophus.hs.SpecificHomogeneousSpace;
import ch.alpine.tensor.chq.MemberQ;
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
public class Sphere extends SnManifold implements SpecificHomogeneousSpace {
  private final int dimensions;
  private final int length;

  /** Example:
   * dimension == 0 consist of the points {1}, and {-1}
   * dimension == 1 corresponds to the unit circle in the plane
   * dimension == 2 corresponds to the "standard", surface sphere embedded in 3-dimensional Euclidean space
   * 
   * @param dimensions of sphere as manifold */
  public Sphere(int dimensions) {
    this.dimensions = Integers.requirePositiveOrZero(dimensions);
    this.length = dimensions + 1;
  }

  @Override // from MemberQ
  public MemberQ isPointQ() {
    return p -> p.length() == length //
        && super.isPointQ().test(p);
  }

  @Override
  public RandomSampleInterface randomSampleInterface() {
    return SphereRandomSample.of(dimensions);
  }

  @Override
  public int dimensions() {
    return dimensions;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise(super.toString(), dimensions);
  }
}
