// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class RadialBasisFunctionInterpolationTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    int n = 10;
    Tensor sequence = RandomVariate.of(distribution, n, 3);
    Tensor values = RandomVariate.of(distribution, n, 2);
    WeightingInterface weightingInterface = PseudoDistances.RELATIVE.create(RnManifold.INSTANCE, PowerVariogram.of(1, 1.5));
    TensorUnaryOperator tensorUnaryOperator = Serialization.copy( //
        RadialBasisFunctionInterpolation.normalized(weightingInterface, sequence, values));
    for (int index = 0; index < sequence.length(); ++index) {
      Tensor tensor = tensorUnaryOperator.apply(sequence.get(index));
      Tolerance.CHOP.requireClose(tensor, values.get(index));
    }
  }

  public void testNormalized() {
    Distribution distribution = NormalDistribution.standard();
    int n = 10;
    Tensor sequence = RandomVariate.of(distribution, n, 3);
    Tensor values = RandomVariate.of(distribution, n, 2);
    WeightingInterface weightingInterface = PseudoDistances.RELATIVE.create(RnManifold.INSTANCE, PowerVariogram.of(1, 1.5));
    TensorUnaryOperator tensorUnaryOperator = RadialBasisFunctionInterpolation.normalized(weightingInterface, sequence, values);
    for (int index = 0; index < sequence.length(); ++index) {
      Tensor tensor = tensorUnaryOperator.apply(sequence.get(index));
      Tolerance.CHOP.requireClose(tensor, values.get(index));
    }
  }

  public void testBarycentric() {
    Distribution distribution = NormalDistribution.standard();
    int n = 10;
    Tensor sequence = RandomVariate.of(distribution, n, 3);
    WeightingInterface weightingInterface = PseudoDistances.RELATIVE.create(RnManifold.INSTANCE, PowerVariogram.of(1, 1.5));
    TensorUnaryOperator tensorUnaryOperator = RadialBasisFunctionInterpolation.partitions(weightingInterface, sequence);
    for (int index = 0; index < sequence.length(); ++index) {
      Tensor tensor = tensorUnaryOperator.apply(sequence.get(index));
      Tolerance.CHOP.requireClose(tensor, UnitVector.of(n, index));
      // ---
      Tensor point = RandomVariate.of(distribution, 3);
      Tensor weights = tensorUnaryOperator.apply(point);
      AffineQ.require(weights);
    }
  }
}
