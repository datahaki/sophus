// code by jph
package ch.ethz.idsc.sophus.hs.r2;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.se2.Se2GroupElement;
import ch.ethz.idsc.sophus.lie.se2.Se2Matrix;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringExponential;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringIntegrator;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se2ForwardActionTest extends TestCase {
  public void testSimple() {
    Tensor xya = Tensors.vector(1, 2, 3);
    TensorUnaryOperator tuo = new Se2ForwardAction(xya);
    Tensor p = Tensors.vector(6, -9, 1);
    Tensor q1 = tuo.apply(p);
    Tensor m = Se2Matrix.of(xya);
    Tensor q2 = m.dot(p).extract(0, 2);
    Chop._12.requireClose(q1, q2);
  }

  public void testSome() {
    Tensor u = Tensors.vector(1.2, 0, 0.75);
    Tensor m = Se2Matrix.of(Se2CoveringExponential.INSTANCE.exp(u));
    Tensor p = Tensors.vector(-2, 3);
    Tensor v = m.dot(p.copy().append(RealScalar.ONE));
    Tensor r = Se2CoveringIntegrator.INSTANCE.spin(Se2CoveringExponential.INSTANCE.exp(u), p.append(RealScalar.ZERO));
    assertEquals(r.extract(0, 2), v.extract(0, 2));
    Se2ForwardAction se2ForwardAction = new Se2ForwardAction(Se2CoveringExponential.INSTANCE.exp(u));
    assertEquals(se2ForwardAction.apply(p), v.extract(0, 2));
  }

  public void testPureSe2() {
    Distribution distribution = NormalDistribution.standard();
    Tensor p = RandomVariate.of(distribution, 3);
    Tensor q = RandomVariate.of(distribution, 3);
    Tolerance.CHOP.requireClose( //
        new Se2GroupElement(p).combine(q).extract(0, 2), //
        new Se2ForwardAction(p).apply(q.extract(0, 2)));
  }

  public void testSerializable() throws ClassNotFoundException, IOException {
    Se2Bijection se2Bijection = new Se2Bijection(Tensors.vector(2, -3, 1.3));
    TensorUnaryOperator forward = se2Bijection.forward();
    TensorUnaryOperator copy = Serialization.copy(forward);
    Tensor vector = Tensors.vector(0.32, -0.98);
    assertEquals(forward.apply(vector), copy.apply(vector));
  }
}
