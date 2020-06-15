// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Clips;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import junit.framework.TestCase;

public class DiagonalDistancesTest extends TestCase {
  public void testSimple() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    for (int length = 4; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      Tensor point = RandomVariate.of(distribution, 3);
      ScalarUnaryOperator variogram = s -> s;
      DiagonalDistances diagonalDistances = new DiagonalDistances(Se2CoveringManifold.INSTANCE, variogram);
      BiinvariantVector biinvariantVector = diagonalDistances.biinvariantVector(sequence, point);
      TensorUnaryOperator tensorUnaryOperator = Mahalanobis1Distances.of(Se2CoveringManifold.INSTANCE, variogram, sequence);
      Tensor dmah = tensorUnaryOperator.apply(point);
      Chop._10.requireClose(biinvariantVector.vector(), dmah);
    }
  }
}
