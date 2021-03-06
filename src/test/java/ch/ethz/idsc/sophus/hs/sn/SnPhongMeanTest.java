// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.nrm.NormalizeTotal;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SnPhongMeanTest extends TestCase {
  public void testSnNormalized() {
    Distribution distribution = NormalDistribution.of(1, 0.2);
    for (int d = 2; d < 6; ++d)
      for (int n = d + 1; n < 10; ++n) {
        Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, d).stream().map(Vector2Norm.NORMALIZE));
        Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, n));
        Tensor mean = SnPhongMean.INSTANCE.mean(sequence, weights);
        Tolerance.CHOP.requireClose(mean, Vector2Norm.NORMALIZE.apply(mean));
      }
  }

  public void testMidpoint() {
    Distribution distribution = NormalDistribution.of(0, 10);
    for (int d = 2; d < 4; ++d)
      for (int count = 0; count < 10; ++count) {
        Tensor x = Vector2Norm.NORMALIZE.apply(RandomVariate.of(distribution, d));
        Tensor y = Vector2Norm.NORMALIZE.apply(RandomVariate.of(distribution, d));
        Tensor m1 = SnGeodesic.INSTANCE.midpoint(x, y);
        SnMemberQ.INSTANCE.require(m1);
        Tensor m2 = SnGeodesic.INSTANCE.curve(x, y).apply(RationalScalar.HALF);
        SnMemberQ.INSTANCE.require(m2);
        Chop._08.requireClose(m1, m2);
        Tensor mp = SnPhongMean.INSTANCE.mean(Tensors.of(x, y), Tensors.vector(0.5, 0.5));
        Chop._08.requireClose(m1, mp);
      }
  }

  public void testAffineFail() {
    Tensor x = UnitVector.of(3, 0);
    Tensor y = UnitVector.of(3, 1);
    SnPhongMean.INSTANCE.mean(Tensors.of(x, y), Tensors.vector(0.5, 0.5));
    // AssertFail.of(() -> SnPhongMean.INSTANCE.mean(Tensors.of(x, y), Tensors.vector(0.5, 0.6)));
  }

  public void testAntipodalFail() {
    Tensor x = UnitVector.of(3, 0);
    Tensor y = x.negate();
    AssertFail.of(() -> SnPhongMean.INSTANCE.mean(Tensors.of(x, y), Tensors.vector(0.5, 0.5)));
    // AssertFail.of(() -> SnPhongMean.INSTANCE.mean(Tensors.of(x, y), Tensors.vector(0.5, 0.6)));
  }
}
