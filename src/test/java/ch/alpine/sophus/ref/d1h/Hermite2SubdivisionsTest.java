// code by jph
package ch.alpine.sophus.ref.d1h;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.TensorIteration;
import ch.alpine.sophus.lie.LieTransport;
import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.sophus.lie.se2c.Se2CoveringManifold;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Reverse;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

public class Hermite2SubdivisionsTest {
  @Test
  public void testSimple() {
    TestHelper.check(RnHermite2Subdivisions.standard(), Hermite2Subdivisions.standard(RnManifold.INSTANCE, LieTransport.INSTANCE));
    TestHelper.check(RnHermite2Subdivisions.manifold(), Hermite2Subdivisions.manifold(RnManifold.INSTANCE, LieTransport.INSTANCE));
  }

  static final List<HermiteSubdivision> LIST = Arrays.asList( //
      Hermite2Subdivisions.standard(RnManifold.INSTANCE, LieTransport.INSTANCE), //
      Hermite2Subdivisions.manifold(RnManifold.INSTANCE, LieTransport.INSTANCE));

  @Test
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

  @Test
  public void testNullA1Fail() {
    AssertFail.of(() -> Hermite2Subdivisions.standard(Se2CoveringManifold.INSTANCE, null));
    AssertFail.of(() -> Hermite2Subdivisions.standard(null, LieTransport.INSTANCE));
  }

  @Test
  public void testNullA2Fail() {
    AssertFail.of(() -> Hermite2Subdivisions.manifold(Se2CoveringManifold.INSTANCE, null));
    AssertFail.of(() -> Hermite2Subdivisions.manifold(null, LieTransport.INSTANCE));
  }
}
