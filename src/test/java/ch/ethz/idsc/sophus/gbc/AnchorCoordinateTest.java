// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.lang.reflect.Modifier;

import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Clips;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import junit.framework.TestCase;

public class AnchorCoordinateTest extends TestCase {
  public void testSimple() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    ScalarUnaryOperator variogram = s -> s;
    BarycentricCoordinate anchorCoordinate = LeverageCoordinate.slow(Se2CoveringManifold.INSTANCE, variogram);
    BarycentricCoordinate targetCoordinate = LeverageCoordinate.of(Se2CoveringManifold.INSTANCE, variogram);
    for (int length = 4; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      Tensor point = RandomVariate.of(distribution, 3);
      Chop._08.requireClose( //
          anchorCoordinate.weights(sequence, point), //
          targetCoordinate.weights(sequence, point));
    }
  }

  public void testPackage() {
    assertFalse(Modifier.isPublic(AnchorCoordinate.class.getModifiers()));
  }
}