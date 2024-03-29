// code by jph
package ch.alpine.sophus.flt.bm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.flt.ga.BinomialWeights;
import ch.alpine.sophus.lie.se2.Se2BiinvariantMeans;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.win.WindowFunctions;

class BiinvariantMeanCenterTest {
  @Test
  void testSe2() {
    for (WindowFunctions smoothingKernel : WindowFunctions.values()) {
      TensorUnaryOperator tensorUnaryOperator = //
          BiinvariantMeanCenter.of(Se2BiinvariantMeans.GLOBAL, smoothingKernel.get());
      Distribution distribution = UniformDistribution.unit();
      for (int count = 1; count < 10; ++count) {
        Tensor sequence = RandomVariate.of(distribution, count, 3);
        Tensor tensor = tensorUnaryOperator.apply(sequence);
        assertEquals(Dimensions.of(tensor), List.of(3));
      }
    }
  }

  @Test
  void testOfFunction() {
    TensorUnaryOperator tensorUnaryOperator = BiinvariantMeanCenter.of(Se2BiinvariantMeans.GLOBAL, BinomialWeights.INSTANCE);
    tensorUnaryOperator.apply(RandomVariate.of(UniformDistribution.unit(), 5, 3));
  }

  @Test
  void testFailNull() {
    assertThrows(Exception.class, () -> BiinvariantMeanCenter.of(Se2BiinvariantMeans.GLOBAL, (ScalarUnaryOperator) null));
  }
}
