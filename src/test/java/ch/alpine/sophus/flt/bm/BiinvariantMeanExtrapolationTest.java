// code by jph
package ch.alpine.sophus.flt.bm;

import ch.alpine.sophus.crv.spline.MonomialExtrapolationMask;
import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.num.Series;
import ch.alpine.tensor.pdf.DiscreteUniformDistribution;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class BiinvariantMeanExtrapolationTest extends TestCase {
  public void testSimple() {
    TensorUnaryOperator tensorUnaryOperator = BiinvariantMeanExtrapolation.of( //
        RnBiinvariantMean.INSTANCE, MonomialExtrapolationMask.INSTANCE);
    Tensor tensor = tensorUnaryOperator.apply(Tensors.vector(1, 2));
    assertEquals(tensor, RealScalar.of(3));
  }

  public void testSeries() {
    TensorUnaryOperator tensorUnaryOperator = BiinvariantMeanExtrapolation.of( //
        RnBiinvariantMean.INSTANCE, MonomialExtrapolationMask.INSTANCE);
    Distribution distribution = DiscreteUniformDistribution.of(3, 12);
    for (int deg = 1; deg < 6; ++deg) {
      ScalarUnaryOperator scalarUnaryOperator = Series.of(RandomVariate.of(distribution, deg + 1));
      Tensor sequence = Range.of(0, deg + 1).map(scalarUnaryOperator);
      Tensor predict = tensorUnaryOperator.apply(sequence);
      Scalar actual = scalarUnaryOperator.apply(RealScalar.of(deg + 1));
      assertEquals(predict, actual);
      ExactTensorQ.require(predict);
    }
  }
}
