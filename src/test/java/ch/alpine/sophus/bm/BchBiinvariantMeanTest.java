// code by jph
package ch.alpine.sophus.bm;

import java.util.function.BinaryOperator;

import ch.alpine.sophus.lie.se2c.Se2CoveringBiinvariantMean;
import ch.alpine.sophus.lie.se2c.Se2CoveringExponential;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.sophus.math.Exponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.ad.BakerCampbellHausdorff;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.N;
import junit.framework.TestCase;

public class BchBiinvariantMeanTest extends TestCase {
  private static final BinaryOperator<Tensor> BCH_SE2 = BakerCampbellHausdorff.of(N.DOUBLE.of(Tensors.fromString( //
      "{{{0, 0, 0}, {0, 0, -1}, {0, 1, 0}}, {{0, 0, 1}, {0, 0, 0}, {-1, 0, 0}}, {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}}")), 6);

  public void testSe2ExpExpLog() {
    Exponential exponential = Se2CoveringExponential.INSTANCE;
    Tensor x = Tensors.vector(0.1, 0.2, 0.05);
    Tensor y = Tensors.vector(0.02, -0.1, -0.04);
    Tensor mX = exponential.exp(x);
    Tensor mY = exponential.exp(y);
    Tensor res = exponential.log(Se2CoveringGroup.INSTANCE.element(mX).combine(mY));
    Tensor z = BCH_SE2.apply(x, y);
    Chop._06.requireClose(z, res);
  }

  public void testSe2Mean() {
    Exponential exponential = Se2CoveringExponential.INSTANCE;
    Tensor p0 = Tensors.vector(0.1, 0.2, 0.05);
    Tensor p1 = Tensors.vector(0.02, -0.1, -0.04);
    Tensor p2 = Tensors.vector(-0.05, 0.03, 0.1);
    Tensor p3 = Tensors.vector(0.07, -0.08, -0.06);
    Tensor sequence = Tensors.of(p0, p1, p2, p3);
    Tensor seqG = Tensor.of(sequence.stream().map(exponential::exp));
    Tensor weights = NormalizeTotal.FUNCTION.apply(Tensors.vector(0.3, 0.4, 0.5, 0.6));
    Tensor meanG = Se2CoveringBiinvariantMean.INSTANCE.mean(seqG, weights);
    Tensor mean = exponential.log(meanG);
    BiinvariantMean biinvariantMean = BchBiinvariantMean.of(BCH_SE2);
    Tensor meanb = biinvariantMean.mean(sequence, weights);
    Chop._06.requireClose(mean, meanb);
  }
}
