// code by jph
package ch.alpine.sophus.ref.d1h;

import java.io.IOException;

import ch.alpine.sophus.lie.LieTransport;
import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.sophus.lie.se2c.Se2CoveringManifold;
import ch.alpine.sophus.math.TensorIteration;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Reverse;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class Hermite3SubdivisionTest extends TestCase {
  public void testStringLength2() {
    Tensor control = Tensors.fromString("{{3, 4}, {1, -3}}");
    TensorIteration tensorIteration1 = RnHermite3Subdivisions.standard().string(RealScalar.ONE, control);
    TensorIteration tensorIteration2 = //
        Hermite3Subdivisions.of(RnManifold.INSTANCE, LieTransport.INSTANCE, RnBiinvariantMean.INSTANCE) //
            .string(RealScalar.ONE, control);
    for (int count = 0; count < 6; ++count) {
      Tensor it1 = tensorIteration1.iterate();
      Tensor it2 = tensorIteration2.iterate();
      assertEquals(it1, it2);
      ExactTensorQ.require(it1);
      ExactTensorQ.require(it2);
    }
  }

  public void testStringReverseRn() {
    Tensor cp1 = RandomVariate.of(NormalDistribution.standard(), 7, 2, 3);
    Tensor cp2 = cp1.copy();
    cp2.set(Tensor::negate, Tensor.ALL, 1);
    TensorIteration tensorIteration1 = //
        Hermite3Subdivisions.of(RnManifold.INSTANCE, LieTransport.INSTANCE, RnBiinvariantMean.INSTANCE) //
            .string(RealScalar.ONE, cp1);
    TensorIteration tensorIteration2 = //
        Hermite3Subdivisions.of(RnManifold.INSTANCE, LieTransport.INSTANCE, RnBiinvariantMean.INSTANCE) //
            .string(RealScalar.ONE, Reverse.of(cp2));
    for (int count = 0; count < 3; ++count) {
      Tensor result1 = tensorIteration1.iterate();
      Tensor result2 = Reverse.of(tensorIteration2.iterate());
      result2.set(Tensor::negate, Tensor.ALL, 1);
      Chop._12.requireClose(result1, result2);
    }
  }

  public void testCyclic() {
    Tensor control = Tensors.fromString("{{0, 0}, {1, 0}, {0, -1}, {-1/2, 1}}");
    TensorIteration tensorIteration1 = RnHermite3Subdivisions.standard().cyclic(RealScalar.ONE, control);
    TensorIteration tensorIteration2 = //
        Hermite3Subdivisions.of(RnManifold.INSTANCE, LieTransport.INSTANCE, RnBiinvariantMean.INSTANCE) //
            .cyclic(RealScalar.ONE, control);
    for (int count = 0; count < 6; ++count) {
      Tensor it1 = tensorIteration1.iterate();
      Tensor it2 = tensorIteration2.iterate();
      assertEquals(it1, it2);
      ExactTensorQ.require(it1);
      ExactTensorQ.require(it2);
    }
  }

  public void testQuantity() throws ClassNotFoundException, IOException {
    TestHelper.checkQuantity(Hermite3Subdivisions.of(RnManifold.INSTANCE, LieTransport.INSTANCE, RnBiinvariantMean.INSTANCE));
  }

  public void testNullFail() {
    AssertFail.of(() -> Hermite3Subdivisions.of(Se2CoveringManifold.INSTANCE, null, RnBiinvariantMean.INSTANCE));
    AssertFail.of(() -> Hermite3Subdivisions.of(null, LieTransport.INSTANCE, RnBiinvariantMean.INSTANCE));
    AssertFail.of(() -> Hermite3Subdivisions.of(Se2CoveringManifold.INSTANCE, LieTransport.INSTANCE, null));
  }
}
