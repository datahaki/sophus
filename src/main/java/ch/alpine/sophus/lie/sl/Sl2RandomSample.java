// code by jph
package ch.alpine.sophus.lie.sl;

import java.io.Serializable;
import java.util.Random;

import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;

public class Sl2RandomSample implements RandomSampleInterface, Serializable {
  private final Distribution xy;
  private final Distribution z;

  public Sl2RandomSample(Distribution xy, Distribution z) {
    this.xy = xy;
    this.z = z;
  }

  @Override
  public Tensor randomSample(Random random) {
    return RandomVariate.of(xy, random, 2).append(RandomVariate.of(z, random));
  }
}
