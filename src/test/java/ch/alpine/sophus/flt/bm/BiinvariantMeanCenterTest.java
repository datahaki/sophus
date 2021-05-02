// code by jph
package ch.alpine.sophus.flt.bm;

import java.util.Arrays;

import ch.alpine.sophus.flt.ga.BinomialWeights;
import ch.alpine.sophus.lie.se2.Se2BiinvariantMeans;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import ch.alpine.tensor.sca.win.WindowFunctions;
import junit.framework.TestCase;

public class BiinvariantMeanCenterTest extends TestCase {
  public void testSe2() {
    for (WindowFunctions smoothingKernel : WindowFunctions.values()) {
      TensorUnaryOperator tensorUnaryOperator = //
          BiinvariantMeanCenter.of(Se2BiinvariantMeans.GLOBAL, smoothingKernel.get());
      Distribution distribution = UniformDistribution.unit();
      for (int count = 1; count < 10; ++count) {
        Tensor sequence = RandomVariate.of(distribution, count, 3);
        Tensor tensor = tensorUnaryOperator.apply(sequence);
        assertEquals(Dimensions.of(tensor), Arrays.asList(3));
      }
    }
  }

  public void testOfFunction() {
    TensorUnaryOperator tensorUnaryOperator = BiinvariantMeanCenter.of(Se2BiinvariantMeans.GLOBAL, BinomialWeights.INSTANCE);
    tensorUnaryOperator.apply(RandomVariate.of(UniformDistribution.unit(), 5, 3));
  }

  public void testFailNull() {
    AssertFail.of(() -> BiinvariantMeanCenter.of(Se2BiinvariantMeans.GLOBAL, (ScalarUnaryOperator) null));
  }
}
