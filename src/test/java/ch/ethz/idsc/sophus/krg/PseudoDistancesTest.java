// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.qty.QuantityMagnitude;
import junit.framework.TestCase;

public class PseudoDistancesTest extends TestCase {
  public void testAbsolute() {
    Distribution distribution = NormalDistribution.of(Quantity.of(1, "m"), Quantity.of(2, "m"));
    PseudoDistances pseudoDistances = PseudoDistances.ABSOLUTE;
    Tensor sequence = RandomVariate.of(distribution, 10, 3);
    TensorUnaryOperator weightingInterface = //
        pseudoDistances.create(RnManifold.INSTANCE, SphericalVariogram.of(Quantity.of(10, "m"), Quantity.of(2, "s")), sequence);
    Tensor point = RandomVariate.of(distribution, 3);
    Tensor weights = weightingInterface.apply(point);
    weights.map(QuantityMagnitude.singleton("s"));
  }

  public void testRelative() {
    Distribution distribution = NormalDistribution.of(Quantity.of(1, "m"), Quantity.of(2, "m"));
    PseudoDistances pseudoDistances = PseudoDistances.RELATIVE1;
    Tensor sequence = RandomVariate.of(distribution, 10, 3);
    TensorUnaryOperator weightingInterface = //
        pseudoDistances.create(RnManifold.INSTANCE, SphericalVariogram.of(RealScalar.of(10), Quantity.of(2, "s")), sequence);
    Tensor point = RandomVariate.of(distribution, 3);
    Tensor weights = weightingInterface.apply(point);
    weights.map(QuantityMagnitude.singleton("s"));
  }
}
