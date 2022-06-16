// code by jph
package ch.alpine.sophus.lie.td;

import java.io.Serializable;
import java.util.Random;

import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;

public record TDtRandomSample(Distribution t, int n) implements RandomSampleInterface, Serializable {
  @Override
  public Tensor randomSample(Random random) {
    return RandomVariate.of(t, random, n).append(RandomVariate.of(t, random));
  }
}
