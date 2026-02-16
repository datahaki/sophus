// code by jph
package ch.alpine.sophus.hs.st;

import java.util.random.RandomGenerator;

import ch.alpine.sophus.math.api.SpecificManifold;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.pd.Orthogonalize;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

/** uniform distribution on St(n, k) according to Haar measure
 * 
 * References:
 * geomstats - stiefel.py
 * 
 * @return matrix that satisfies {@link OrthogonalMatrixQ} typically up to precision of 1e-10
 * 
 * @see StManifold */
public class StiefelManifold extends StManifold implements SpecificManifold {
  private final int n;
  private final int k;

  /** @param n positive
   * @param k positive, no greater than n */
  public StiefelManifold(int n, int k) {
    this.n = Integers.requirePositive(n);
    this.k = Integers.requirePositive(k);
    Integers.requireLessEquals(k, n);
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(RandomGenerator randomGenerator) {
    Tensor p = Orthogonalize.usingPD(RandomVariate.of(NormalDistribution.standard(), randomGenerator, k, n));
    p = StManifold.projection(p); // just for numeric correction
    return isPointQ().require(p);
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
