// code by jph
package ch.ethz.idsc.sophus.flt.bm;

import ch.ethz.idsc.sophus.crv.spline.MonomialExtrapolationMask;
import ch.ethz.idsc.sophus.lie.rn.RnBiinvariantMean;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Range;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.num.Series;
import ch.ethz.idsc.tensor.pdf.DiscreteUniformDistribution;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
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
