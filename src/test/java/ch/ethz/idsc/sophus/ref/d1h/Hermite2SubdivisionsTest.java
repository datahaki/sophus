// code by jph
package ch.ethz.idsc.sophus.ref.d1h;

import java.util.Arrays;
import java.util.List;

import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.lie.rn.RnTransport;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
import ch.ethz.idsc.sophus.math.TensorIteration;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Reverse;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Hermite2SubdivisionsTest extends TestCase {
  public void testSimple() {
    TestHelper.check(RnHermite2Subdivisions.standard(), Hermite2Subdivisions.standard(RnManifold.INSTANCE, RnTransport.INSTANCE));
    TestHelper.check(RnHermite2Subdivisions.manifold(), Hermite2Subdivisions.manifold(RnManifold.INSTANCE, RnTransport.INSTANCE));
  }

  static final List<HermiteSubdivision> LIST = Arrays.asList( //
      Hermite2Subdivisions.standard(RnManifold.INSTANCE, RnTransport.INSTANCE), //
      Hermite2Subdivisions.manifold(RnManifold.INSTANCE, RnTransport.INSTANCE));

  public void testStringReverseRn() {
    Tensor cp1 = RandomVariate.of(NormalDistribution.standard(), 7, 2, 3);
    Tensor cp2 = cp1.copy();
    cp2.set(Tensor::negate, Tensor.ALL, 1);
    for (HermiteSubdivision hermiteSubdivision : LIST) {
      TensorIteration tensorIteration1 = hermiteSubdivision.string(RealScalar.ONE, cp1);
      TensorIteration tensorIteration2 = hermiteSubdivision.string(RealScalar.ONE, Reverse.of(cp2));
      for (int count = 0; count < 3; ++count) {
        Tensor result1 = tensorIteration1.iterate();
        Tensor result2 = Reverse.of(tensorIteration2.iterate());
        result2.set(Tensor::negate, Tensor.ALL, 1);
        Chop._12.requireClose(result1, result2);
      }
    }
  }

  public void testNullA1Fail() {
    AssertFail.of(() -> Hermite2Subdivisions.standard(Se2CoveringManifold.INSTANCE, null));
    AssertFail.of(() -> Hermite2Subdivisions.standard(null, RnTransport.INSTANCE));
  }

  public void testNullA2Fail() {
    AssertFail.of(() -> Hermite2Subdivisions.manifold(Se2CoveringManifold.INSTANCE, null));
    AssertFail.of(() -> Hermite2Subdivisions.manifold(null, RnTransport.INSTANCE));
  }
}
