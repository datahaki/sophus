// code by jph
package ch.alpine.sophus.hs.st;

import ch.alpine.sophus.hs.SpecificHomogeneousSpace;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.pdf.RandomSampleInterface;

/** uniform distribution on St(n, k) according to Haar measure
 * 
 * References:
 * geomstats - stiefel.py
 * 
 * @return matrix that satisfies {@link OrthogonalMatrixQ} typically up to precision of 1e-10
 * 
 * @see StManifold */
public class StiefelManifold extends StManifold implements SpecificHomogeneousSpace {
  private final int n;
  private final int k;

  /** @param n positive
   * @param k positive, no greater than n */
  public StiefelManifold(int n, int k) {
    this.n = Integers.requirePositive(n);
    this.k = Integers.requirePositive(k);
    Integers.requireLessEquals(k, n);
  }

  @Override
  public RandomSampleInterface randomSampleInterface() {
    return new StRandom(n, k);
  }

  @Override
  public int dimensions() {
    return n * k - Math.divideExact((k + 1) * k, 2);
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise(super.toString(), n, k);
  }
}
