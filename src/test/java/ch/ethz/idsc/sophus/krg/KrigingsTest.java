// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.qty.QuantityMagnitude;
import ch.ethz.idsc.tensor.qty.Unit;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import junit.framework.TestCase;

public class KrigingsTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    int n = 10;
    Tensor sequence = RandomVariate.of(distribution, n, 3);
    Tensor values = RandomVariate.of(distribution, n, 2);
    ScalarUnaryOperator variogram = PowerVariogram.of(RealScalar.ONE, RealScalar.of(1.5));
    for (Krigings krigings : Krigings.values()) {
      Kriging kriging = Serialization.copy(krigings.interpolation(RnManifold.INSTANCE, variogram, sequence, values));
      for (int index = 0; index < sequence.length(); ++index) {
        Tensor tensor = kriging.estimate(sequence.get(index));
        Tolerance.CHOP.requireClose(tensor, values.get(index));
      }
    }
  }

  public void testScalarValued() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    int n = 10;
    Tensor sequence = RandomVariate.of(distribution, n, 3);
    Tensor values = RandomVariate.of(distribution, n);
    ScalarUnaryOperator variogram = PowerVariogram.of(RealScalar.ONE, RealScalar.of(1.5));
    for (Krigings krigings : Krigings.values()) {
      Kriging kriging = Serialization.copy(krigings.interpolation(RnManifold.INSTANCE, variogram, sequence, values));
      for (int index = 0; index < sequence.length(); ++index) {
        Tensor tensor = kriging.estimate(sequence.get(index));
        Tolerance.CHOP.requireClose(tensor, values.get(index));
      }
    }
  }

  public void testBarycentric() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    int n = 10;
    ScalarUnaryOperator variogram = PowerVariogram.of(RealScalar.ONE, RealScalar.of(1.5));
    for (int d = 1; d < 4; ++d) {
      Tensor sequence = RandomVariate.of(distribution, n, d);
      for (Krigings krigings : Krigings.values()) {
        Kriging kriging = Serialization.copy(krigings.barycentric(RnManifold.INSTANCE, variogram, sequence));
        for (int index = 0; index < sequence.length(); ++index) {
          Tensor tensor = kriging.estimate(sequence.get(index));
          Chop._10.requireClose(tensor, UnitVector.of(n, index));
          // ---
          Tensor point = RandomVariate.of(distribution, d);
          Tensor weights = kriging.estimate(point);
          AffineQ.require(weights, Chop._08);
        }
      }
    }
  }

  public void testWeighting() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    int n = 6;
    ScalarUnaryOperator variogram = PowerVariogram.of(RealScalar.ONE, RealScalar.of(1.5));
    for (Krigings krigings : Krigings.values()) {
      WeightingInterface weightingInterface = Serialization.copy(krigings.weighting(RnManifold.INSTANCE, variogram));
      for (int d = 1; d < 4; ++d) {
        Tensor sequence = RandomVariate.of(distribution, n, d);
        for (int index = 0; index < sequence.length(); ++index) {
          Tensor tensor = weightingInterface.weights(sequence, sequence.get(index));
          Chop._10.requireClose(tensor, UnitVector.of(n, index));
          // ---
          Tensor point = RandomVariate.of(distribution, d);
          Tensor weights = weightingInterface.weights(sequence, point);
          AffineQ.require(weights, Chop._08);
        }
      }
    }
  }

  public void testQuantityAbsolute() {
    Distribution distributionX = NormalDistribution.of(Quantity.of(0, "m"), Quantity.of(2, "m"));
    ScalarUnaryOperator variogram = ExponentialVariogram.of(Quantity.of(3, "m"), RealScalar.of(2));
    int n = 10;
    int d = 3;
    Tensor sequence = RandomVariate.of(distributionX, n, d);
    Distribution distributionY = NormalDistribution.of(Quantity.of(0, "s"), Quantity.of(2, "s"));
    Tensor values = RandomVariate.of(distributionY, n);
    Krigings krigings = Krigings.ABSOLUTE;
    Kriging kriging = krigings.interpolation(RnManifold.INSTANCE, variogram, sequence, values);
    Scalar apply = (Scalar) kriging.estimate(RandomVariate.of(distributionX, d));
    QuantityMagnitude.singleton(Unit.of("s")).apply(apply);
  }

  public void testQuantityRelative() {
    Distribution distributionX = NormalDistribution.of(Quantity.of(0, "m"), Quantity.of(2, "m"));
    ScalarUnaryOperator variogram = ExponentialVariogram.of(3, 2);
    int n = 10;
    int d = 3;
    Tensor sequence = RandomVariate.of(distributionX, n, d);
    Distribution distributionY = NormalDistribution.of(Quantity.of(0, "s"), Quantity.of(2, "s"));
    Tensor values = RandomVariate.of(distributionY, n);
    Krigings krigings = Krigings.RELATIVE;
    Kriging kriging = krigings.interpolation(RnManifold.INSTANCE, variogram, sequence, values);
    Scalar apply = (Scalar) kriging.estimate(RandomVariate.of(distributionX, d));
    QuantityMagnitude.singleton(Unit.of("s")).apply(apply);
  }
}