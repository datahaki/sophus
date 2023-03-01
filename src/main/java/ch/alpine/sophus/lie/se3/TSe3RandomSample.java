// code by jph
package ch.alpine.sophus.lie.se3;

import java.io.Serializable;
import java.util.random.RandomGenerator;

import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;

public record TSe3RandomSample(Distribution p, Distribution v) implements RandomSampleInterface, Serializable {
  @Override
  public Tensor randomSample(RandomGenerator random) {
    return Join.of( //
        RandomVariate.of(p, random, 3), //
        RandomVariate.of(v, random, 3));
  }
}
