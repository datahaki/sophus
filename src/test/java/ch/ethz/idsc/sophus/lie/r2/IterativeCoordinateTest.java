// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import ch.ethz.idsc.sophus.gbc.LeversCoordinate;
import ch.ethz.idsc.sophus.hs.MeanDefect;
import ch.ethz.idsc.sophus.lie.rn.RnExponential;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Sign;
import junit.framework.TestCase;

public class IterativeCoordinateTest extends TestCase {
  public void testSimple() {
    Distribution distribution = UniformDistribution.of(-10, 10);
    for (int n = 3; n < 10; ++n) {
      Tensor sequence = RandomVariate.of(distribution, n, 2);
      {
        TensorUnaryOperator tensorUnaryOperator = ThreePointHomogeneous.of(Barycenter.MEAN_VALUE);
        Tensor weights = tensorUnaryOperator.apply(sequence);
        MeanDefect meanDefect = new MeanDefect(sequence, weights, RnExponential.INSTANCE);
        Tensor tangent = meanDefect.tangent();
        Chop._07.requireAllZero(tangent);
      }
      for (int k = 0; k < 3; ++k) {
        TensorUnaryOperator tensorUnaryOperator = IterativeCoordinate.usingMeanValue(k);
        Tensor weights = tensorUnaryOperator.apply(sequence);
        MeanDefect meanDefect = new MeanDefect(sequence, weights, RnExponential.INSTANCE);
        Tensor tangent = meanDefect.tangent();
        Chop._07.requireAllZero(tangent);
      }
    }
  }

  public void testKis0() {
    Distribution distribution = UniformDistribution.of(-10, 10);
    // for (int count = 0; count < 10; ++count)
    for (int n = 3; n < 10; ++n) {
      Tensor sequence = RandomVariate.of(distribution, n, 2);
      Tensor weights = ThreePointCoordinate.of(Barycenter.MEAN_VALUE).apply(sequence);
      Chop._07.requireClose( //
          weights, //
          new IterativeCoordinate(ThreePointHomogeneous.of(Barycenter.MEAN_VALUE), 0).apply(sequence));
      if (weights.stream().map(Scalar.class::cast).anyMatch(Sign::isNegative)) {
        boolean result = Chop._10.isClose( //
            weights, //
            new IterativeCoordinate(ThreePointHomogeneous.of(Barycenter.MEAN_VALUE), 2).apply(sequence));
        if (4 < n)
          assertFalse(result);
      }
    }
  }

  public void testBiinv() {
    Distribution distribution = UniformDistribution.of(-10, 10);
    TensorUnaryOperator ops = LeversCoordinate.of(InversePowerVariogram.of(2));
    for (int n = 3; n < 10; ++n) {
      Tensor sequence = RandomVariate.of(distribution, n, 2);
      for (int k = 0; k < 3; ++k) {
        TensorUnaryOperator tensorUnaryOperator = IterativeCoordinate.of(ops, k);
        // TensorUnaryOperator ivd = points->
        // Biinvariants.METRIC.coordinate(RnManifold.INSTANCE, InversePowerVariogram.of(-2), points);
        Tensor weights = tensorUnaryOperator.apply(sequence);
        MeanDefect meanDefect = new MeanDefect(sequence, weights, RnExponential.INSTANCE);
        Tensor tangent = meanDefect.tangent();
        if (!Chop._07.allZero(tangent)) {
          System.out.println("ICTEST n=" + n + " k=" + k);
        }
      }
    }
  }
}
