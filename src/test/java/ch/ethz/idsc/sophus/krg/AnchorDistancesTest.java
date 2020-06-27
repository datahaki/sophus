// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Clips;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import junit.framework.TestCase;

public class AnchorDistancesTest extends TestCase {
  public void testSimple() {
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    ScalarUnaryOperator variogram = s -> s;
    AnchorDistances anchorDistances = new AnchorDistances(vectorLogManifold, variogram);
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    for (int length = 4; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      Tensor point = RandomVariate.of(distribution, 3);
      BiinvariantVector biinvariantVector = anchorDistances.biinvariantVector(sequence, point);
      WeightingInterface weightingInterface = TargetDistances.of(vectorLogManifold, variogram);
      Tensor dmah = weightingInterface.weights(sequence, point);
      Chop._10.requireClose(biinvariantVector.vector(), dmah);
      Chop._10.requireClose(biinvariantVector.normalized(), NormalizeTotal.FUNCTION.apply(dmah));
    }
  }
}
