// code by jph
package ch.alpine.sophus.hs.gr;

import ch.alpine.sophus.hs.SpecificHomogeneousSpace;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.pdf.ConstantRandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;

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
public class Grassmannian extends GrManifold implements SpecificHomogeneousSpace {
  private final int n;
  private final int k;
  private final RandomSampleInterface randomSampleInterface;

  /** @param n as in n x n projection matrices
   * @param k each matrix with k ev equals 1 and (n-k) ev equals 0 */
  public Grassmannian(int n, int k) {
    this.n = Integers.requirePositive(n);
    this.k = Integers.requirePositiveOrZero(k);
    Integers.requireLessEquals(k, n);
    randomSampleInterface = 0 < k //
        ? new GrRandomSample(n, k)
        : new ConstantRandomSample(Array.zeros(n, n));
  }

  @Override
  public RandomSampleInterface randomSampleInterface() {
    return randomSampleInterface;
  }

  @Override
  public int dimensions() {
    return k * (n - k);
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise(super.toString(), n, k);
  }
}
