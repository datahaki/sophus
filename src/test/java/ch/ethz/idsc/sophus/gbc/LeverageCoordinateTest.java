// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.Biinvariants;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.Pi;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Clips;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import junit.framework.TestCase;

public class LeverageCoordinateTest extends TestCase {
  public void testR1equiv() {
    // in R1 we have W^ID = w^IL
    // but not in R2 etc.
    VectorLogManifold vectorLogManifold = RnManifold.INSTANCE;
    ScalarUnaryOperator variogram = s -> s;
    Distribution distribution = UniformDistribution.of(Clips.absolute(Pi.TWO));
    for (int length = 3; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 1);
      Tensor origin = RandomVariate.of(distribution, 1);
      Chop._08.requireClose( //
          Biinvariants.METRIC.weighting(vectorLogManifold, variogram, sequence).apply(origin), //
          Biinvariants.TARGET.weighting(vectorLogManifold, variogram, sequence).apply(origin));
    }
  }
}
