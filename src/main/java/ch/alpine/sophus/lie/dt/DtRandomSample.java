// code by jph
package ch.alpine.sophus.lie.dt;

import java.io.Serializable;
import java.util.Random;

import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;

public class DtRandomSample implements RandomSampleInterface, Serializable {
  private final int n;
  private final Distribution l;
  private final Distribution t;

  public DtRandomSample(int n, Distribution l, Distribution t) {
    this.n = n;
    this.l = l;
    this.t = t;
  }

  @Override
  public Tensor randomSample(Random random) {
    return Tensors.of(RandomVariate.of(l, random), RandomVariate.of(t, random, n));
  }
}
