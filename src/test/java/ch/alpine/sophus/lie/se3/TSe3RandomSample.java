// code by jph
package ch.alpine.sophus.lie.se3;

import java.io.Serializable;
import java.util.random.RandomGenerator;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;

@Deprecated
record TSe3RandomSample(Distribution p, Distribution v) implements RandomSampleInterface, Serializable {
  @Override
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return Join.of( //
        RandomVariate.of(p, randomGenerator, 3), //
        RandomVariate.of(v, randomGenerator, 3));
  }
}
