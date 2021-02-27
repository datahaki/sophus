// code by jph
package ch.ethz.idsc.sophus.hs.r2;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.se2.Se2GroupElement;
import ch.ethz.idsc.sophus.lie.se2.Se2Matrix;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.mat.LinearSolve;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se2InverseActionTest extends TestCase {
  public void testSimple() {
    Tensor xya = Tensors.vector(1, 2, 3);
    TensorUnaryOperator tensorUnaryOperator = new Se2InverseAction(xya);
    Tensor p = Tensors.vector(6, -9, 1);
    Tensor q1 = tensorUnaryOperator.apply(p);
    Tensor q2 = LinearSolve.of(Se2Matrix.of(xya), p).extract(0, 2);
    Chop._12.requireClose(q1, q2);
  }

  public void testPureSe2() {
    Distribution distribution = NormalDistribution.standard();
    Tensor p = RandomVariate.of(distribution, 3);
    Tensor q = RandomVariate.of(distribution, 3);
    Tolerance.CHOP.requireClose( //
        new Se2GroupElement(p).inverse().combine(q).extract(0, 2), //
        new Se2InverseAction(p).apply(q.extract(0, 2)));
  }

  public void testSerializable() throws ClassNotFoundException, IOException {
    Se2Bijection se2Bijection = new Se2Bijection(Tensors.vector(2, -3, 1.3));
    Serialization.copy(se2Bijection.inverse());
  }
}
