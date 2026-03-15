// code by jph
package ch.alpine.sophus.hs.st;

import java.util.random.RandomGenerator;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.pd.Orthogonalize;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

record StRandom(int n, int k) implements RandomSampleInterface {
  @Override
  public Tensor randomSample(RandomGenerator randomGenerator) {
    Tensor p = Orthogonalize.usingPD(RandomVariate.of(NormalDistribution.standard(), randomGenerator, k, n));
    return StManifold.projection(p); // just for numeric correction
  }
}
