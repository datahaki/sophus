// code by jph
package ch.alpine.sophus.lie.dt;

import java.io.Serializable;
import java.util.Random;

import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;

public class TDtRandomSample implements RandomSampleInterface, Serializable {
  private final int n;
  private final Distribution t;

  public TDtRandomSample(int n, Distribution t) {
    this.n = n;
    this.t = t;
  }

  @Override
  public Tensor randomSample(Random random) {
    return Tensors.of(RandomVariate.of(t, random), RandomVariate.of(t, random, n));
  }
}
