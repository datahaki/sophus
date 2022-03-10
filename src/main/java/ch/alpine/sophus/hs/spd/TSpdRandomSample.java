// code by jph
package ch.alpine.sophus.hs.spd;

import java.io.Serializable;
import java.util.Random;

import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.mat.UpperEvaluation;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;

public class TSpdRandomSample implements RandomSampleInterface, Serializable {
  private final Distribution distribution;
  private final Tensor vector;

  /** @param n strictly positive
   * @param distribution */
  public TSpdRandomSample(int n, Distribution distribution) {
    Integers.requirePositive(n);
    this.distribution = distribution;
    vector = ConstantArray.of(RealScalar.ZERO, n);
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(Random random) {
    return UpperEvaluation.of(vector, vector, (p, q) -> RandomVariate.of(distribution, random), s -> s);
  }
}
