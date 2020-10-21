// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.BiinvariantVector;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.LeverageDistances;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Clips;
import junit.framework.TestCase;

public class AnchorDistancesTest extends TestCase {
  public void testDistances() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    WeightingInterface w1 = HsCoordinates.wrap(vectorLogManifold, LeverageDistances.INSTANCE);
    WeightingInterface w2 = new AnchorDistances(vectorLogManifold);
    for (int length = 4; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      Tensor point = RandomVariate.of(distribution, 3);
      Chop._07.requireClose( //
          w1.weights(sequence, point), //
          w2.weights(sequence, point));
    }
  }

  public void testSimple() {
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    ScalarUnaryOperator variogram = s -> s;
    AnchorDistances anchorDistances = new AnchorDistances(vectorLogManifold);
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    for (int length = 4; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      Tensor point = RandomVariate.of(distribution, 3);
      BiinvariantVector biinvariantVector = anchorDistances.biinvariantVector(sequence, point);
      WeightingInterface weightingInterface = HsCoordinates.wrap(vectorLogManifold, LeverageDistances.INSTANCE);
      Tensor dmah = weightingInterface.weights(sequence, point);
      Chop._10.requireClose(biinvariantVector.distances(), dmah);
      Chop._10.requireClose(biinvariantVector.weighting(variogram), NormalizeTotal.FUNCTION.apply(dmah));
    }
  }
}
