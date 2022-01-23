// code by jph
package ch.alpine.sophus.hs.spd;

import java.io.IOException;

import ch.alpine.sophus.hs.PoleLadder;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

/** SpdTransport is also tested in {@link SpdRiemannTest} */
public class SpdTransportTest extends TestCase {
  public void testLadder() throws ClassNotFoundException, IOException {
    Tensor p = IdentityMatrix.of(3);
    Tensor q = TestHelper.generateSpd(3);
    Tensor e11 = Tensors.fromString("{{1, 0, 0}, {0, 0, 0}, {0, 0, 0}}");
    Tensor e12 = Tensors.fromString("{{0, 1, 0}, {1, 0, 0}, {0, 0, 0}}");
    Tensor e13 = Tensors.fromString("{{0, 0, 1}, {0, 0, 0}, {1, 0, 0}}");
    TensorUnaryOperator tu1 = Serialization.copy(SpdTransport.INSTANCE.shift(p, q));
    TensorUnaryOperator tu2 = Serialization.copy(new PoleLadder(SpdManifold.INSTANCE).shift(p, q));
    Chop._08.requireClose(tu1.apply(e11), tu2.apply(e11));
    Chop._08.requireClose(tu1.apply(e12), tu2.apply(e12));
    Chop._08.requireClose(tu1.apply(e13), tu2.apply(e13));
  }

  public void testMultiT() {
    Tensor e11 = Tensors.fromString("{{1, 0, 0}, {0, 0, 0}, {0, 0, 0}}");
    Tensor e12 = Tensors.fromString("{{0, 1, 0}, {1, 0, 0}, {0, 0, 0}}");
    Tensor e13 = Tensors.fromString("{{0, 0, 1}, {0, 0, 0}, {1, 0, 0}}");
    Tensor p = IdentityMatrix.of(3);
    for (int c = 0; c < 5; ++c) {
      Tensor q = TestHelper.generateSpd(3);
      TensorUnaryOperator tu1 = SpdTransport.INSTANCE.shift(p, q);
      TensorUnaryOperator tu2 = new PoleLadder(SpdManifold.INSTANCE).shift(p, q);
      Chop._08.requireClose(tu1.apply(e11), tu2.apply(e11));
      Chop._08.requireClose(tu1.apply(e12), tu2.apply(e12));
      Chop._08.requireClose(tu1.apply(e13), tu2.apply(e13));
      e11 = tu1.apply(e11);
      e12 = tu1.apply(e12);
      e13 = tu1.apply(e13);
      SymmetricMatrixQ.require(e11);
      SymmetricMatrixQ.require(e12);
      SymmetricMatrixQ.require(e13);
      p = q;
    }
  }
}
