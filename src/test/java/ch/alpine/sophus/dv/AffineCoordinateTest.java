// code by jph
package ch.alpine.sophus.dv;

import java.util.Random;
import java.util.random.RandomGenerator;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.crv.d2.OriginEnclosureQ;
import ch.alpine.sophus.hs.Genesis;
import ch.alpine.sophus.hs.sn.SnRandomSample;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.red.Times;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.exp.Exp;

class AffineCoordinateTest {
  @Test
  void testS1() {
    Genesis genesis = MetricCoordinate.affine();
    RandomSampleInterface randomSampleInterface = SnRandomSample.of(1);
    RandomGenerator random = new Random(3);
    for (int n = 3; n < 10; ++n) {
      Tensor levers = RandomSample.of(randomSampleInterface, random, n);
      Tensor w1 = genesis.origin(levers.unmodifiable());
      Tensor w2 = AffineCoordinate.INSTANCE.origin(levers);
      Chop._07.requireClose(w1, w2);
    }
  }

  @Test
  void testRn() {
    Genesis genesis = MetricCoordinate.affine();
    Distribution distribution = NormalDistribution.standard();
    RandomGenerator random = new Random(3);
    for (int n = 3; n < 10; ++n)
      for (int k = 0; k < 2; ++k) {
        int d = 2 + k;
        Tensor levers = RandomVariate.of(distribution, random, n + k, d);
        Tensor w1 = genesis.origin(levers);
        Tensor w2 = AffineCoordinate.INSTANCE.origin(levers);
        Chop._04.requireClose(w1, w2);
      }
  }

  @Test
  void testIterateR2() {
    Distribution distribution = UniformDistribution.of(-1, 5);
    RandomGenerator random = new Random(3);
    int d = 2;
    for (int n = 4; n < 10; ++n)
      for (int count = 0; count < 10; ++count) {
        Tensor levers = RandomVariate.of(distribution, random, n, d);
        if (OriginEnclosureQ.INSTANCE.test(levers)) {
          for (int i = 0; i < 3; ++i) {
            Tensor weights = AffineCoordinate.INSTANCE.origin(levers);
            weights = NormalizeTotal.FUNCTION.apply(weights.map(Exp.FUNCTION));
            levers = Times.of(weights, levers);
          }
        }
      }
  }
}
