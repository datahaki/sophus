// code by jph
package ch.alpine.sophus.math.var;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.dv.Biinvariant;
import ch.alpine.sophus.dv.Biinvariants;
import ch.alpine.sophus.fit.PowerVariogramFit;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.sophus.itp.Kriging;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.QuantityMagnitude;
import ch.alpine.tensor.qty.Unit;

class PowerVariogramTest {
  @Test
  void testFitQuantity() throws ClassNotFoundException, IOException {
    Distribution distributionX = NormalDistribution.of(Quantity.of(0, "m"), Quantity.of(2, "m"));
    int n = 10;
    int d = 3;
    Tensor sequence = RandomVariate.of(distributionX, n, d);
    Distribution distributionY = NormalDistribution.of(Quantity.of(0, "s"), Quantity.of(2, "s"));
    Tensor values = RandomVariate.of(distributionY, n);
    Biinvariant biinvariant = Biinvariants.METRIC.ofSafe(RnGroup.INSTANCE);
    {
      ScalarUnaryOperator variogram = Serialization.copy(new ExponentialVariogram(Quantity.of(3, "m"), RealScalar.of(2)));
      Sedarim weightingInterface = biinvariant.var_dist(variogram, sequence);
      Kriging kriging = Kriging.interpolation(weightingInterface, sequence, values);
      Scalar value = (Scalar) kriging.estimate(RandomVariate.of(distributionX, d));
      QuantityMagnitude.singleton(Unit.of("s")).apply(value);
    }
    {
      PowerVariogram variogram = Serialization.copy(PowerVariogramFit.fit(RnGroup.INSTANCE, sequence, values, RealScalar.ONE));
      Tensor covariance = DiagonalMatrix.of(n, Quantity.of(1, "s^2"));
      Sedarim weightingInterface = biinvariant.var_dist(variogram, sequence);
      Kriging kriging = Kriging.regression(weightingInterface, sequence, values, covariance);
      Scalar value = (Scalar) kriging.estimate(RandomVariate.of(distributionX, d));
      QuantityMagnitude.singleton(Unit.of("s")).apply(value);
    }
  }

  @Test
  void testSimple() {
    try {
      PowerVariogram.of(1, 2);
      // fail();
    } catch (Exception exception) {
      // ---
    }
  }

  @Test
  void testEmpty() {
    assertThrows(Exception.class, () -> PowerVariogramFit.fit(RnGroup.INSTANCE, Tensors.empty(), Tensors.empty(), RealScalar.of(1.5)));
  }
}
