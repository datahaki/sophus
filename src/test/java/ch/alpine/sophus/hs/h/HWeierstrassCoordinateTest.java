// code by jph
package ch.alpine.sophus.hs.h;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;
import java.util.random.RandomGenerator;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;

class HWeierstrassCoordinateTest {
  @Test
  void test() {
    HWeierstrassCoordinate hWeierstrassCoordinate = new HWeierstrassCoordinate(Tensors.vector(0, 0));
    Tensor p = hWeierstrassCoordinate.toPoint();
    assertEquals(p, Tensors.vector(0, 0, 1));
    ExactTensorQ.require(p);
  }

  @Test
  void testSimple() {
    RandomGenerator randomGenerator = new Random(3);
    Distribution distribution = NormalDistribution.standard();
    BiinvariantMean biinvariantMean = HManifold.INSTANCE.biinvariantMean();
    for (int d = 1; d < 5; ++d) {
      final int fd = d;
      Tensor sequence = Array.of(_ -> new HWeierstrassCoordinate(RandomVariate.of(distribution, randomGenerator, fd)).toPoint(), 10);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), randomGenerator, 10));
      biinvariantMean.mean(sequence, weights);
    }
  }
}
