// code by jph
package ch.alpine.sophus.lie.se3;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Random;

import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

public class TSe3RandomSample implements RandomSampleInterface, Serializable {
  private final Distribution p;
  private final Distribution v;

  public TSe3RandomSample(Distribution p, Distribution v) {
    this.p = p;
    this.v = v;
  }

  @Override
  public Tensor randomSample(Random random) {
    return Tensors.of( //
        RandomVariate.of(p, random, 3), //
        RandomVariate.of(v, random, 3));
  }
}
