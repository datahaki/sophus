// code by jph
package ch.alpine.sophus.hs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.s.SnManifold;
import ch.alpine.sophus.hs.s.SnTransport;
import ch.alpine.sophus.lie.gl.GlGroup;
import ch.alpine.sophus.lie.rn.RGroup;
import ch.alpine.sophus.lie.se2.Se2CoveringGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class PoleLadderTest {
  @Test
  void testRn() {
    HsTransport hsTransport = new PoleLadder(RGroup.INSTANCE);
    TensorUnaryOperator shift = //
        hsTransport.shift(Tensors.vector(1, 2, 3), Tensors.vector(4, -1, 7));
    Tensor v = Tensors.vector(2, 3, 1);
    Tensor u = shift.apply(v);
    assertEquals(v, u);
    ExactTensorQ.require(u);
  }

  @Test
  void testSn() throws ClassNotFoundException, IOException {
    Tensor orig = UnitVector.of(3, 0);
    Tensor dest = UnitVector.of(3, 1);
    HsTransport poleLadder = new PoleLadder(SnManifold.INSTANCE);
    TensorUnaryOperator shift1 = //
        Serialization.copy(poleLadder).shift(orig, dest);
    TensorUnaryOperator shift2 = SubdivideTransport.of(poleLadder, SnManifold.INSTANCE, 7).shift(orig, dest);
    TensorUnaryOperator shift3 = SnTransport.INSTANCE.shift(orig, dest);
    {
      Tensor v1 = UnitVector.of(3, 1);
      Tensor t1 = shift1.apply(v1);
      Tensor t2 = shift2.apply(v1);
      Tensor t3 = shift3.apply(v1);
      Chop._12.requireClose(t1, t3);
      Chop._12.requireClose(t2, t3);
      ExactTensorQ.require(t3);
    }
    {
      Tensor v2 = UnitVector.of(3, 2).multiply(RealScalar.of(0.001));
      Tensor t1 = shift1.apply(v2);
      Tensor t2 = shift2.apply(v2);
      Tensor t3 = shift3.apply(v2);
      Chop._10.requireClose(t1, t3);
      Chop._10.requireClose(t2, t3);
    }
    {
      Tensor v2 = UnitVector.of(3, 2).multiply(RealScalar.of(0.1));
      Tensor t1 = shift1.apply(v2);
      Tensor t2 = shift1.apply(v2);
      Tensor t3 = shift3.apply(v2);
      Chop._12.requireClose(t1, t3);
      Chop._12.requireClose(t2, t3);
    }
  }

  private static final Distribution DISTRIBUTION = UniformDistribution.of(-10, 10);

  @Test
  void testSimple() {
    Tensor p = RandomVariate.of(DISTRIBUTION, 3);
    Tensor q = RandomVariate.of(DISTRIBUTION, 3);
    Tensor v = RandomVariate.of(DISTRIBUTION, 3);
    Tensor r1 = GlGroup.INSTANCE.hsTransport().shift(p, q).apply(v);
    HsTransport hsTransport = new PoleLadder(Se2CoveringGroup.INSTANCE);
    Tensor r2 = hsTransport.shift(p, q).apply(v);
    Chop._10.requireClose(v.Get(2), r1.Get(2));
    Chop._10.requireClose(v.Get(2), r2.Get(2));
    HsTransport transport = new SymmetrizeTransport(hsTransport);
    Tensor r3 = transport.shift(p, q).apply(v);
    Chop._10.requireClose(r2, r3);
  }

  @Test
  void testSubdivide() {
    Tensor p = RandomVariate.of(DISTRIBUTION, 3);
    Tensor q = RandomVariate.of(DISTRIBUTION, 3);
    Tensor v = RandomVariate.of(DISTRIBUTION, 3);
    Tensor r1 = GlGroup.INSTANCE.hsTransport().shift(p, q).apply(v);
    HsTransport hsTransport = SubdivideTransport.of( //
        new PoleLadder(Se2CoveringGroup.INSTANCE), //
        Se2CoveringGroup.INSTANCE, 100);
    Tensor r2 = hsTransport.shift(p, q).apply(v);
    Chop._08.requireClose(v.Get(2), r1.Get(2));
    Chop._08.requireClose(v.Get(2), r2.Get(2));
  }
}
