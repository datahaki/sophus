// code by jph
package ch.ethz.idsc.sophus.math.var;

import java.io.IOException;

import ch.ethz.idsc.sophus.hs.MetricBiinvariant;
import ch.ethz.idsc.sophus.itp.Kriging;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.lie.rn.RnMetric;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.mat.DiagonalMatrix;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.qty.QuantityMagnitude;
import ch.ethz.idsc.tensor.qty.Unit;
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
      ScalarUnaryOperator variogram = Serialization.copy(ExponentialVariogram.of(Quantity.of(3, "m"), RealScalar.of(2)));
      TensorUnaryOperator weightingInterface = //
          MetricBiinvariant.EUCLIDEAN.var_dist(RnManifold.INSTANCE, variogram, sequence);
      Kriging kriging = Kriging.interpolation(weightingInterface, sequence, values);
      Scalar value = (Scalar) kriging.estimate(RandomVariate.of(distributionX, d));
      QuantityMagnitude.singleton(Unit.of("s")).apply(value);
    }
    {
      PowerVariogram variogram = Serialization.copy(PowerVariogram.fit(RnMetric.INSTANCE, sequence, values, RealScalar.ONE));
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
    AssertFail.of(() -> PowerVariogram.fit(RnMetric.INSTANCE, Tensors.empty(), Tensors.empty(), RealScalar.of(1.5)));
  }
}
