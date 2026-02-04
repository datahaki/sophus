// code by jph
package ch.alpine.sophus.hs.spd;

import java.io.Serializable;
import java.util.random.RandomGenerator;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.mat.UpperEvaluation;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;

/** @param n strictly positive
 * @param distribution */
public record TSpdRandomSample(int n, Distribution distribution) implements RandomSampleInterface, Serializable {
  public TSpdRandomSample {
    Integers.requirePositive(n);
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(RandomGenerator randomGenerator) {
    // clumsy way to generate a symmetric matrix
    Tensor vector = ConstantArray.of(RealScalar.ZERO, n);
    return UpperEvaluation.of(vector, vector, //
        (_, _) -> RandomVariate.of(distribution, randomGenerator), s -> s);
  }
}
