// code by ob, jph
package ch.alpine.sophus.lie.dt;

import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import junit.framework.TestCase;

public class DtExponentialTest extends TestCase {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(DtGroup.INSTANCE);

  public void testSt1ExpLog() {
    Scalar u = RealScalar.of(7);
    Scalar v = RealScalar.of(3);
    Tensor inp = Tensors.of(u, v);
    Tensor xy = DtExponential.INSTANCE.exp(inp);
    Tensor uv = DtExponential.INSTANCE.log(xy);
    Tolerance.CHOP.requireClose(inp, uv);
  }

  public void testSt1LogExp() {
    Scalar u = RealScalar.of(7);
    Scalar v = RealScalar.of(3);
    Tensor inp = Tensors.of(u, v);
    Tensor uv = DtExponential.INSTANCE.log(inp);
    Tensor xy = DtExponential.INSTANCE.exp(uv);
    Tolerance.CHOP.requireClose(inp, xy);
  }

  public void testSt1ExpLogRandom() {
    for (int count = 0; count < 10; ++count) {
      Distribution distribution = NormalDistribution.standard();
      Tensor inp = Tensors.of( //
          RandomVariate.of(distribution), //
          RandomVariate.of(distribution, 2, 3));
      Tensor xy = DtExponential.INSTANCE.exp(inp);
      Tensor uv = DtExponential.INSTANCE.log(xy);
      Tolerance.CHOP.requireClose(inp, uv);
    }
  }

  public void testSt1ExpLogSingular() {
    for (int count = 0; count < 10; ++count) {
      Distribution distribution = NormalDistribution.standard();
      Tensor inp = Tensors.of( //
          RealScalar.ZERO, //
          RandomVariate.of(distribution, 2, 3));
      Tensor xy = DtExponential.INSTANCE.exp(inp);
      Tensor uv = DtExponential.INSTANCE.log(xy);
      Tolerance.CHOP.requireClose(inp, uv);
    }
  }

  public void testSt1Singular() {
    for (int count = 0; count < 10; ++count) {
      Tensor inp = Tensors.vector(0, Math.random());
      Tensor xy = DtExponential.INSTANCE.exp(inp);
      Tensor uv = DtExponential.INSTANCE.log(xy);
      Tolerance.CHOP.requireClose(inp, uv);
    }
  }

  public void testExpLog() {
    for (int count = 0; count < 10; ++count) {
      Scalar u = RealScalar.of(Math.random());
      Tensor v = Tensors.vector(Math.random(), 3 * Math.random(), -Math.random(), -4 * Math.random());
      Tensor inp = Tensors.of(u, v);
      Tensor xy = DtExponential.INSTANCE.exp(inp);
      Tensor uv = DtExponential.INSTANCE.log(xy);
      Tolerance.CHOP.requireClose(inp, uv);
    }
  }

  public void testLogExp() {
    for (int count = 0; count < 10; ++count) {
      Scalar u = RealScalar.of(Math.random());
      Tensor v = Tensors.vector(Math.random(), 3 * Math.random(), -Math.random(), -4 * Math.random());
      Tensor inp = Tensors.of(u, v);
      Tensor uv = DtExponential.INSTANCE.log(inp);
      Tensor xy = DtExponential.INSTANCE.exp(uv);
      Tolerance.CHOP.requireClose(inp, xy);
    }
  }

  public void testSingular() {
    Tensor v = Tensors.vector(Math.random(), 3 * Math.random(), -Math.random(), -4 * Math.random());
    Tensor inp = Tensors.of(RealScalar.ZERO, v);
    Tensor xy = DtExponential.INSTANCE.exp(inp);
    Tensor uv = DtExponential.INSTANCE.log(xy);
    Tolerance.CHOP.requireClose(inp, uv);
  }

  public void testLogInv() {
    Tensor lambda_t = TestHelper.spawn_St(2);
    DtGroupElement stGroupElement = DtGroup.INSTANCE.element(lambda_t);
    Tensor inv = stGroupElement.inverse().toCoordinate();
    Tensor neutral = stGroupElement.combine(inv);
    Tolerance.CHOP.requireClose(neutral, Tensors.fromString("{1, {0, 0}}"));
    Tensor log1 = DtExponential.INSTANCE.log(lambda_t);
    Tensor log2 = DtExponential.INSTANCE.log(inv);
    Tolerance.CHOP.requireClose(log1, log2.negate());
  }

  public void testAdLog() {
    for (int count = 0; count < 10; ++count) {
      Tensor g = TestHelper.spawn_St(2);
      Tensor m = TestHelper.spawn_St(2);
      Tensor lhs = DtExponential.INSTANCE.log(LIE_GROUP_OPS.conjugation(g).apply(m));
      Tensor rhs = DtGroup.INSTANCE.element(g).adjoint(DtExponential.INSTANCE.log(m));
      Tolerance.CHOP.requireClose(lhs, rhs);
    }
  }
}
