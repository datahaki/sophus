// code by jph
package ch.alpine.sophus.flt.bm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.crv.spline.MonomialExtrapolationMask;
import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.num.Polynomial;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.d.DiscreteUniformDistribution;

public class BiinvariantMeanExtrapolationTest {
  @Test
  public void testSimple() {
    TensorUnaryOperator tensorUnaryOperator = new BiinvariantMeanExtrapolation( //
        RnBiinvariantMean.INSTANCE, MonomialExtrapolationMask.INSTANCE);
    Tensor tensor = tensorUnaryOperator.apply(Tensors.vector(1, 2));
    assertEquals(tensor, RealScalar.of(3));
  }

  @Test
  public void testSeries() {
    TensorUnaryOperator tensorUnaryOperator = new BiinvariantMeanExtrapolation( //
        RnBiinvariantMean.INSTANCE, MonomialExtrapolationMask.INSTANCE);
    Distribution distribution = DiscreteUniformDistribution.of(3, 12);
    for (int deg = 1; deg < 6; ++deg) {
      ScalarUnaryOperator scalarUnaryOperator = Polynomial.of(RandomVariate.of(distribution, deg + 1));
      Tensor sequence = Range.of(0, deg + 1).map(scalarUnaryOperator);
      Tensor predict = tensorUnaryOperator.apply(sequence);
      Scalar actual = scalarUnaryOperator.apply(RealScalar.of(deg + 1));
      assertEquals(predict, actual);
      ExactTensorQ.require(predict);
    }
  }
}
