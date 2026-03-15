// code by jph
package ch.alpine.sophus.hs.gr;

import java.util.random.RandomGenerator;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.gr.InfluenceMatrix;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

record GrRandomSample(int n, int k) implements RandomSampleInterface {
  @Override
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return InfluenceMatrix.of(RandomVariate.of(NormalDistribution.standard(), randomGenerator, n, k)).matrix();
  }
}
