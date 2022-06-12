// code by jph
package ch.alpine.sophus.lie.se3;

import java.io.Serializable;
import java.util.Random;

import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;

public record TSe3RandomSample(Distribution p, Distribution v) implements RandomSampleInterface, Serializable {
  @Override
  public Tensor randomSample(Random random) {
    return Tensors.of( //
        RandomVariate.of(p, random, 3), //
        RandomVariate.of(v, random, 3));
  }
}
