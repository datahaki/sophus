// code by jph
package ch.alpine.sophus.lie.sl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.lie.HodgeDual;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class Sl2GroupTest {
  @Test
  void testSimple() {
    Sl2GroupElement sl2GroupElement = Sl2Group.INSTANCE.element(Tensors.vector(8, 64, 4));
    Tensor inverse = sl2GroupElement.inverse().toCoordinate();
    ExactTensorQ.require(inverse);
    assertEquals(inverse, Tensors.vector(-2, -16, 0.25));
  }

  @Test
  void testSimple2() {
    Tensor x = Tensors.fromString("{{2, 3}, {4, -2}}");
    Tensor exp = Sl2Group.INSTANCE.exp(x);
    Chop._10.requireClose(exp, MatrixExp.of(x));
    Chop._10.requireClose(Det.of(exp), RealScalar.ONE);
    Tensor log = Sl2Group.INSTANCE.log(exp);
    Chop._10.requireClose(x, log);
  }

  @Test
  void testNegativeDelta() {
    Tensor x = Tensors.fromString("{{2, -3}, {4, -2}}");
    Tensor exp = Sl2Group.INSTANCE.exp(x);
    Chop._10.requireClose(exp, MatrixExp.of(x));
    Chop._10.requireClose(Det.of(exp), RealScalar.ONE);
    Tensor log = Sl2Group.INSTANCE.log(exp);
    Chop._10.requireClose(x, log);
  }

  @Test
  void testId() {
    Tensor g = IdentityMatrix.of(2).negate();
    Tensor log = Sl2Group.INSTANCE.log(g);
    Tolerance.CHOP.requireClose(log, HodgeDual.of(Pi.VALUE, 2));
  }

  @Test
  void testRandom() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor x = RandomVariate.of(distribution, 2, 2);
      x.set(x.get(0, 0).negate(), 1, 1);
      Tensor e1 = Sl2Group.INSTANCE.exp(x);
      Tensor e2 = MatrixExp.of(x);
      Chop._10.requireClose(e1, e2);
      Chop._10.requireClose(Det.of(e1), RealScalar.ONE);
      Tensor log = Sl2Group.INSTANCE.log(e2);
      Chop._10.requireClose(log, x);
      Tensor vlg = Sl2Group.INSTANCE.vectorLog(e2);
      VectorQ.requireLength(vlg, 3);
    }
  }
}
