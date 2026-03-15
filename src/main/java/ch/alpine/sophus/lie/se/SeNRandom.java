// code by jph
package ch.alpine.sophus.lie.se;

import java.util.random.RandomGenerator;

import ch.alpine.sophus.lie.so.SoNGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ArrayFlatten;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

record SeNRandom(int n) implements RandomSampleInterface {
  @Override
  public Tensor randomSample(RandomGenerator randomGenerator) {
    Tensor rot = new SoNGroup(n).randomSampleInterface().randomSample(randomGenerator);
    Tensor pnt = RandomVariate.of(NormalDistribution.standard(), randomGenerator, n, 1);
    return ArrayFlatten.of(new Tensor[][] { { rot, pnt } }).append(UnitVector.of(n + 1, n));
  }
}
