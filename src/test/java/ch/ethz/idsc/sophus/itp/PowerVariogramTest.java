// code by jph
package ch.ethz.idsc.sophus.itp;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.qty.QuantityMagnitude;
import ch.ethz.idsc.tensor.qty.Unit;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import junit.framework.TestCase;

public class PowerVariogramTest extends TestCase {
  public void testFitQuantity() {
    Distribution distributionX = NormalDistribution.of(Quantity.of(0, "m"), Quantity.of(2, "m"));
    int n = 10;
    int d = 3;
    Tensor sequence = RandomVariate.of(distributionX, n, d);
    Distribution distributionY = NormalDistribution.of(Quantity.of(0, "s"), Quantity.of(2, "s"));
    Tensor values = RandomVariate.of(distributionY, n);
    {
      ScalarUnaryOperator variogram = ExponentialVariogram.of(Quantity.of(3, "m"), RealScalar.of(2));
      Kriging kriging = LinearKriging.interpolation(variogram, sequence, values);
      Scalar value = (Scalar) kriging.apply(RandomVariate.of(distributionX, d));
      QuantityMagnitude.singleton(Unit.of("s")).apply(value);
    }
    {
      PowerVariogram variogram = PowerVariogram.fit(sequence, values, RealScalar.ONE);
      Tensor covariance = IdentityMatrix.of(n, Quantity.of(1, "s^2"));
      Kriging kriging = LinearKriging.regression(variogram, sequence, values, covariance);
      Scalar value = (Scalar) kriging.apply(RandomVariate.of(distributionX, d));
      QuantityMagnitude.singleton(Unit.of("s")).apply(value);
    }
  }

  public void testSimple() {
    try {
      PowerVariogram.of(1, 2);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
