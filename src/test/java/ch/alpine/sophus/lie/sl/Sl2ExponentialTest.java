// code by jph
package ch.alpine.sophus.lie.sl;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.VectorQ;
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
import junit.framework.TestCase;

public class Sl2ExponentialTest extends TestCase {
  public void testSimple() {
    Tensor x = Tensors.fromString("{{2, 3}, {4, -2}}");
    Tensor exp = Sl2Exponential.INSTANCE.exp(x);
    Chop._10.requireClose(exp, MatrixExp.of(x));
    Chop._10.requireClose(Det.of(exp), RealScalar.ONE);
    Tensor log = Sl2Exponential.INSTANCE.log(exp);
    Chop._10.requireClose(x, log);
  }

  public void testNegativeDelta() {
    Tensor x = Tensors.fromString("{{2, -3}, {4, -2}}");
    Tensor exp = Sl2Exponential.INSTANCE.exp(x);
    Chop._10.requireClose(exp, MatrixExp.of(x));
    Chop._10.requireClose(Det.of(exp), RealScalar.ONE);
    Tensor log = Sl2Exponential.INSTANCE.log(exp);
    Chop._10.requireClose(x, log);
  }

  public void testId() {
    Tensor g = IdentityMatrix.of(2).negate();
    Tensor log = Sl2Exponential.INSTANCE.log(g);
    Tolerance.CHOP.requireClose(log, HodgeDual.of(Pi.VALUE, 2));
  }

  public void testRandom() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor x = RandomVariate.of(distribution, 2, 2);
      x.set(x.get(0, 0).negate(), 1, 1);
      Tensor e1 = Sl2Exponential.INSTANCE.exp(x);
      Tensor e2 = MatrixExp.of(x);
      Chop._10.requireClose(e1, e2);
      Chop._10.requireClose(Det.of(e1), RealScalar.ONE);
      Tensor log = Sl2Exponential.INSTANCE.log(e2);
      Chop._10.requireClose(log, x);
      Tensor vlg = Sl2Exponential.INSTANCE.vectorLog(e2);
      VectorQ.requireLength(vlg, 3);
    }
  }
}
