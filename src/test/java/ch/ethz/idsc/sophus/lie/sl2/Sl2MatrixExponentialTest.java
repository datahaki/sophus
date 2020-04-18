// code by jph
package ch.ethz.idsc.sophus.lie.sl2;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.lie.HodgeDual;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.mat.Det;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.opt.Pi;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Sl2MatrixExponentialTest extends TestCase {
  public void testSimple() {
    Tensor x = Tensors.fromString("{{2, 3}, {4, -2}}");
    Tensor exp = Sl2MatrixExponential.INSTANCE.exp(x);
    Chop._10.requireClose(exp, MatrixExp.of(x));
    Chop._10.requireClose(Det.of(exp), RealScalar.ONE);
    Tensor log = Sl2MatrixExponential.INSTANCE.log(exp);
    Chop._10.requireClose(x, log);
  }

  public void testNegativeDelta() {
    Tensor x = Tensors.fromString("{{2, -3}, {4, -2}}");
    Tensor exp = Sl2MatrixExponential.INSTANCE.exp(x);
    Chop._10.requireClose(exp, MatrixExp.of(x));
    Chop._10.requireClose(Det.of(exp), RealScalar.ONE);
    Tensor log = Sl2MatrixExponential.INSTANCE.log(exp);
    Chop._10.requireClose(x, log);
  }

  public void testId() {
    Tensor g = IdentityMatrix.of(2).negate();
    Tensor log = Sl2MatrixExponential.INSTANCE.log(g);
    Tolerance.CHOP.requireClose(log, HodgeDual.of(Pi.VALUE, 2));
  }

  public void testRandom() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor x = RandomVariate.of(distribution, 2, 2);
      x.set(x.get(0, 0).negate(), 1, 1);
      Tensor e1 = Sl2MatrixExponential.INSTANCE.exp(x);
      Tensor e2 = MatrixExp.of(x);
      Chop._10.requireClose(e1, e2);
      Chop._10.requireClose(Det.of(e1), RealScalar.ONE);
      Tensor log = Sl2MatrixExponential.INSTANCE.log(e2);
      Chop._10.requireClose(log, x);
    }
  }
}
