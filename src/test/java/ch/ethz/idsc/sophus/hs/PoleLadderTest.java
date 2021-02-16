// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.IOException;

import ch.ethz.idsc.sophus.hs.sn.SnGeodesic;
import ch.ethz.idsc.sophus.hs.sn.SnManifold;
import ch.ethz.idsc.sophus.hs.sn.SnTransport;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class PoleLadderTest extends TestCase {
  public void testRn() {
    HsTransport hsTransport = PoleLadder.of(RnManifold.INSTANCE);
    TensorUnaryOperator shift = //
        hsTransport.shift(Tensors.vector(1, 2, 3), Tensors.vector(4, -1, 7));
    Tensor v = Tensors.vector(2, 3, 1);
    Tensor u = shift.apply(v);
    assertEquals(v, u);
    ExactTensorQ.require(u);
  }

  public void testSn() throws ClassNotFoundException, IOException {
    Tensor orig = UnitVector.of(3, 0);
    Tensor dest = UnitVector.of(3, 1);
    HsTransport poleLadder = PoleLadder.of(SnManifold.INSTANCE);
    TensorUnaryOperator shift1 = //
        Serialization.copy(poleLadder).shift(orig, dest);
    TensorUnaryOperator shift2 = SubdivideTransport.of(poleLadder, SnGeodesic.INSTANCE, 7).shift(orig, dest);
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

  public void testNullFail() {
    AssertFail.of(() -> PoleLadder.of(null));
  }
}
