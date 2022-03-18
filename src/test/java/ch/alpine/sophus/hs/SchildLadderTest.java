// code by jph
package ch.alpine.sophus.hs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.sn.SnGeodesic;
import ch.alpine.sophus.hs.sn.SnManifold;
import ch.alpine.sophus.hs.sn.SnTransport;
import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.sca.Chop;

public class SchildLadderTest {
  @Test
  public void testRn() {
    HsTransport hsTransport = SchildLadder.of(RnManifold.INSTANCE);
    TensorUnaryOperator shift = //
        hsTransport.shift(Tensors.vector(1, 2, 3), Tensors.vector(4, -1, 7));
    Tensor v = Tensors.vector(2, 3, 1);
    Tensor u = shift.apply(v);
    assertEquals(v, u);
    ExactTensorQ.require(u);
  }

  @Test
  public void testSn() throws ClassNotFoundException, IOException {
    Tensor orig = UnitVector.of(3, 0);
    Tensor dest = UnitVector.of(3, 1);
    TensorUnaryOperator shift1 = SchildLadder.of(SnManifold.INSTANCE).shift(orig, dest);
    TensorUnaryOperator shift2 = Serialization.copy(SchildLadder.of(SnManifold.INSTANCE)).shift(orig, dest);
    TensorUnaryOperator shift3 = Serialization.copy(new SchildLadder(SnManifold.INSTANCE, SnGeodesic.INSTANCE)).shift(orig, dest);
    TensorUnaryOperator shift4 = SnTransport.INSTANCE.shift(orig, dest);
    {
      Tensor v1 = UnitVector.of(3, 1);
      Tensor t1 = shift1.apply(v1);
      Tensor t2 = shift2.apply(v1);
      Tensor t3 = shift3.apply(v1);
      Tensor t4 = shift4.apply(v1);
      Chop._12.requireClose(t1, t4);
      Chop._12.requireClose(t2, t4);
      Chop._12.requireClose(t3, t4);
      ExactTensorQ.require(t4);
    }
    {
      Tensor v2 = UnitVector.of(3, 2).multiply(RealScalar.of(0.001));
      Tensor t1 = shift1.apply(v2);
      Tensor t2 = shift2.apply(v2);
      Tensor t3 = shift3.apply(v2);
      Chop._05.requireClose(t1, t3);
      Chop._05.requireClose(t2, t3);
    }
    {
      Tensor v2 = UnitVector.of(3, 2).multiply(RealScalar.of(0.1));
      Tensor t1 = shift1.apply(v2);
      Tensor t2 = shift2.apply(v2);
      Tensor t3 = shift3.apply(v2);
      Scalar d13 = Vector2Norm.between(t1, t3);
      Scalar d23 = Vector2Norm.between(t2, t3);
      Chop._12.requireClose(d13, d23);
    }
  }

  @Test
  public void testNullFail() {
    AssertFail.of(() -> SchildLadder.of(null));
  }
}
