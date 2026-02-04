// code by jph
package ch.alpine.sophus.lie.se2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.BasicLieIntegrator;
import ch.alpine.sophus.lie.LieIntegrator;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class Se2ForwardActionTest {
  @Test
  void testSimple() {
    Tensor xya = Tensors.vector(1, 2, 3);
    TensorUnaryOperator tuo = new Se2ForwardAction(xya);
    Tensor p = Tensors.vector(6, -9, 1);
    Tensor q1 = tuo.apply(p);
    Tensor m = Se2Matrix.of(xya);
    Tensor q2 = m.dot(p).extract(0, 2);
    Chop._12.requireClose(q1, q2);
  }

  @Test
  void testSome() {
    LieIntegrator lieIntegrator = new BasicLieIntegrator(Se2CoveringGroup.INSTANCE);
    Tensor u = Tensors.vector(1.2, 0, 0.75);
    Tensor m = Se2Matrix.of(Se2CoveringGroup.INSTANCE.exponential0().exp(u));
    Tensor p = Tensors.vector(-2, 3);
    Tensor v = m.dot(p.copy().append(RealScalar.ONE));
    Tensor r = lieIntegrator.spin(Se2CoveringGroup.INSTANCE.exponential0().exp(u), p.append(RealScalar.ZERO));
    assertEquals(r.extract(0, 2), v.extract(0, 2));
    Se2ForwardAction se2ForwardAction = new Se2ForwardAction(Se2CoveringGroup.INSTANCE.exponential0().exp(u));
    assertEquals(se2ForwardAction.apply(p), v.extract(0, 2));
  }

  @Test
  void testPureSe2() {
    Distribution distribution = NormalDistribution.standard();
    Tensor p = RandomVariate.of(distribution, 3);
    Tensor q = RandomVariate.of(distribution, 3);
    Tolerance.CHOP.requireClose( //
        Se2Group.INSTANCE.combine(p, q).extract(0, 2), //
        new Se2ForwardAction(p).apply(q.extract(0, 2)));
  }

  @Test
  void testSerializable() throws ClassNotFoundException, IOException {
    Se2ForwardAction forward = new Se2ForwardAction(Tensors.vector(2, -3, 1.3));
    TensorUnaryOperator copy = Serialization.copy(forward);
    Tensor vector = Tensors.vector(0.32, -0.98);
    assertEquals(forward.apply(vector), copy.apply(vector));
  }
}
