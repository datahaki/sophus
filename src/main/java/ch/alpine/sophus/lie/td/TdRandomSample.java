// code by jph
package ch.alpine.sophus.lie.td;

import java.io.Serializable;
import java.util.random.RandomGenerator;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.sca.Sign;

public record TdRandomSample(Distribution t, int n, Distribution l) implements RandomSampleInterface, Serializable {
  @Override
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return RandomVariate.of(t, randomGenerator, n).append(Sign.requirePositive(RandomVariate.of(l, randomGenerator)));
  }
}
