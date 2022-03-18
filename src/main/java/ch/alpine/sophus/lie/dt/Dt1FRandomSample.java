// code by jph
package ch.alpine.sophus.lie.dt;

import java.io.Serializable;
import java.util.Random;

import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;

public class Dt1FRandomSample implements RandomSampleInterface, Serializable {
  private final Distribution l;
  private final Distribution t;

  public Dt1FRandomSample(Distribution l, Distribution t) {
    this.l = l;
    this.t = t;
  }

  @Override
  public Tensor randomSample(Random random) {
    return Tensors.of(RandomVariate.of(l, random), RandomVariate.of(t, random));
  }
}
