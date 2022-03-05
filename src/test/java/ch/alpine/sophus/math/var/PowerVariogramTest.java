// code by jph
package ch.alpine.sophus.math.var;

import java.io.IOException;

import ch.alpine.sophus.fit.PowerVariogramFit;
import ch.alpine.sophus.hs.MetricBiinvariant;
import ch.alpine.sophus.itp.Kriging;
import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.sophus.lie.rn.RnMetric;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.QuantityMagnitude;
import ch.alpine.tensor.qty.Unit;
import junit.framework.TestCase;

public class PowerVariogramTest extends TestCase {
  public void testFitQuantity() throws ClassNotFoundException, IOException {
    Distribution distributionX = NormalDistribution.of(Quantity.of(0, "m"), Quantity.of(2, "m"));
    int n = 10;
    int d = 3;
    Tensor sequence = RandomVariate.of(distributionX, n, d);
    Distribution distributionY = NormalDistribution.of(Quantity.of(0, "s"), Quantity.of(2, "s"));
    Tensor values = RandomVariate.of(distributionY, n);
    {
      ScalarUnaryOperator variogram = Serialization.copy(new ExponentialVariogram(Quantity.of(3, "m"), RealScalar.of(2)));
      TensorUnaryOperator weightingInterface = //
          MetricBiinvariant.EUCLIDEAN.var_dist(RnManifold.INSTANCE, variogram, sequence);
      Kriging kriging = Kriging.interpolation(weightingInterface, sequence, values);
      Scalar value = (Scalar) kriging.estimate(RandomVariate.of(distributionX, d));
      QuantityMagnitude.singleton(Unit.of("s")).apply(value);
    }
    {
      PowerVariogram variogram = Serialization.copy(PowerVariogramFit.fit(RnMetric.INSTANCE, sequence, values, RealScalar.ONE));
      Tensor covariance = DiagonalMatrix.of(n, Quantity.of(1, "s^2"));
      TensorUnaryOperator weightingInterface = //
          MetricBiinvariant.EUCLIDEAN.var_dist(RnManifold.INSTANCE, variogram, sequence);
      Kriging kriging = Kriging.regression(weightingInterface, sequence, values, covariance);
      Scalar value = (Scalar) kriging.estimate(RandomVariate.of(distributionX, d));
      QuantityMagnitude.singleton(Unit.of("s")).apply(value);
    }
  }

  public void testSimple() {
    try {
      PowerVariogram.of(1, 2);
      // fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testEmpty() {
    AssertFail.of(() -> PowerVariogramFit.fit(RnMetric.INSTANCE, Tensors.empty(), Tensors.empty(), RealScalar.of(1.5)));
  }
}
