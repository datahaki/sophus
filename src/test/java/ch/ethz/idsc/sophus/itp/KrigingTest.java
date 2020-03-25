// code by jph
package ch.ethz.idsc.sophus.itp;

import java.io.IOException;

import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.qty.QuantityMagnitude;
import ch.ethz.idsc.tensor.qty.Unit;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import junit.framework.TestCase;

public class KrigingTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    int n = 10;
    Tensor sequence = RandomVariate.of(distribution, n, 3);
    Tensor values = RandomVariate.of(distribution, n, 2);
    ScalarUnaryOperator variogram = PowerVariogram.of(RealScalar.ONE, RealScalar.of(1.5));
    TensorUnaryOperator tensorUnaryOperator = //
        Serialization.copy(LinearKriging.interpolation(variogram, sequence, values));
    for (int index = 0; index < sequence.length(); ++index) {
      Tensor tensor = tensorUnaryOperator.apply(sequence.get(index));
      Tolerance.CHOP.requireClose(tensor, values.get(index));
    }
  }

  public void testScalarValued() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    int n = 10;
    Tensor sequence = RandomVariate.of(distribution, n, 3);
    Tensor values = RandomVariate.of(distribution, n);
    ScalarUnaryOperator variogram = PowerVariogram.of(RealScalar.ONE, RealScalar.of(1.5));
    TensorUnaryOperator tensorUnaryOperator = //
        Serialization.copy(LinearKriging.interpolation(variogram, sequence, values));
    for (int index = 0; index < sequence.length(); ++index) {
      Tensor tensor = tensorUnaryOperator.apply(sequence.get(index));
      Tolerance.CHOP.requireClose(tensor, values.get(index));
    }
  }

  public void testBarycentric() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    int n = 10;
    ScalarUnaryOperator variogram = PowerVariogram.of(RealScalar.ONE, RealScalar.of(1.5));
    for (int d = 1; d < 4; ++d) {
      Tensor sequence = RandomVariate.of(distribution, n, d);
      TensorUnaryOperator tensorUnaryOperator = //
          Serialization.copy(LinearKriging.barycentric(variogram, sequence));
      for (int index = 0; index < sequence.length(); ++index) {
        Tensor tensor = tensorUnaryOperator.apply(sequence.get(index));
        Tolerance.CHOP.requireClose(tensor, UnitVector.of(n, index));
        // ---
        Tensor point = RandomVariate.of(distribution, d);
        Tensor weights = tensorUnaryOperator.apply(point);
        AffineQ.require(weights);
      }
    }
  }

  public void testQuantity() {
    Distribution distributionX = NormalDistribution.of(Quantity.of(0, "m"), Quantity.of(2, "m"));
    ScalarUnaryOperator variogram = ExponentialVariogram.of(Quantity.of(3, "m"), RealScalar.of(2));
    int n = 10;
    int d = 3;
    Tensor sequence = RandomVariate.of(distributionX, n, d);
    Distribution distributionY = NormalDistribution.of(Quantity.of(0, "s"), Quantity.of(2, "s"));
    Tensor values = RandomVariate.of(distributionY, n);
    Kriging kriging = LinearKriging.interpolation(variogram, sequence, values);
    Scalar apply = (Scalar) kriging.apply(RandomVariate.of(distributionX, d));
    QuantityMagnitude.singleton(Unit.of("s")).apply(apply);
  }
}
