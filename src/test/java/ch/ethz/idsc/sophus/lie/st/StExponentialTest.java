// code by ob, jph
package ch.ethz.idsc.sophus.lie.st;

import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class StExponentialTest extends TestCase {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(StGroup.INSTANCE);

  public void testSt1ExpLog() {
    Scalar u = RealScalar.of(7);
    Scalar v = RealScalar.of(3);
    Tensor inp = Tensors.of(u, v);
    Tensor xy = StExponential.INSTANCE.exp(inp);
    Tensor uv = StExponential.INSTANCE.log(xy);
    Tolerance.CHOP.requireClose(inp, uv);
  }

  public void testSt1LogExp() {
    Scalar u = RealScalar.of(7);
    Scalar v = RealScalar.of(3);
    Tensor inp = Tensors.of(u, v);
    Tensor uv = StExponential.INSTANCE.log(inp);
    Tensor xy = StExponential.INSTANCE.exp(uv);
    Tolerance.CHOP.requireClose(inp, xy);
  }

  public void testSt1ExpLogRandom() {
    for (int count = 0; count < 10; ++count) {
      Distribution distribution = NormalDistribution.standard();
      Tensor inp = Tensors.of( //
          RandomVariate.of(distribution), //
          RandomVariate.of(distribution, 2, 3));
      Tensor xy = StExponential.INSTANCE.exp(inp);
      Tensor uv = StExponential.INSTANCE.log(xy);
      Tolerance.CHOP.requireClose(inp, uv);
    }
  }

  public void testSt1ExpLogSingular() {
    for (int count = 0; count < 10; ++count) {
      Distribution distribution = NormalDistribution.standard();
      Tensor inp = Tensors.of( //
          RealScalar.ZERO, //
          RandomVariate.of(distribution, 2, 3));
      Tensor xy = StExponential.INSTANCE.exp(inp);
      Tensor uv = StExponential.INSTANCE.log(xy);
      Tolerance.CHOP.requireClose(inp, uv);
    }
  }

  public void testSt1Singular() {
    for (int count = 0; count < 10; ++count) {
      Tensor inp = Tensors.vector(0, Math.random());
      Tensor xy = StExponential.INSTANCE.exp(inp);
      Tensor uv = StExponential.INSTANCE.log(xy);
      Tolerance.CHOP.requireClose(inp, uv);
    }
  }

  public void testExpLog() {
    for (int count = 0; count < 10; ++count) {
      Scalar u = RealScalar.of(Math.random());
      Tensor v = Tensors.vector(Math.random(), 3 * Math.random(), -Math.random(), -4 * Math.random());
      Tensor inp = Tensors.of(u, v);
      Tensor xy = StExponential.INSTANCE.exp(inp);
      Tensor uv = StExponential.INSTANCE.log(xy);
      Tolerance.CHOP.requireClose(inp, uv);
    }
  }

  public void testLogExp() {
    for (int count = 0; count < 10; ++count) {
      Scalar u = RealScalar.of(Math.random());
      Tensor v = Tensors.vector(Math.random(), 3 * Math.random(), -Math.random(), -4 * Math.random());
      Tensor inp = Tensors.of(u, v);
      Tensor uv = StExponential.INSTANCE.log(inp);
      Tensor xy = StExponential.INSTANCE.exp(uv);
      Tolerance.CHOP.requireClose(inp, xy);
    }
  }

  public void testSingular() {
    Tensor v = Tensors.vector(Math.random(), 3 * Math.random(), -Math.random(), -4 * Math.random());
    Tensor inp = Tensors.of(RealScalar.ZERO, v);
    Tensor xy = StExponential.INSTANCE.exp(inp);
    Tensor uv = StExponential.INSTANCE.log(xy);
    Tolerance.CHOP.requireClose(inp, uv);
  }

  public void testLogInv() {
    Tensor lambda_t = TestHelper.spawn_St(2);
    StGroupElement stGroupElement = StGroup.INSTANCE.element(lambda_t);
    Tensor inv = stGroupElement.inverse().toCoordinate();
    Tensor neutral = stGroupElement.combine(inv);
    Tolerance.CHOP.requireClose(neutral, Tensors.fromString("{1, {0, 0}}"));
    Tensor log1 = StExponential.INSTANCE.log(lambda_t);
    Tensor log2 = StExponential.INSTANCE.log(inv);
    Tolerance.CHOP.requireClose(log1, log2.negate());
  }

  public void testAdLog() {
    for (int count = 0; count < 10; ++count) {
      Tensor g = TestHelper.spawn_St(2);
      Tensor m = TestHelper.spawn_St(2);
      Tensor lhs = StExponential.INSTANCE.log(LIE_GROUP_OPS.conjugation(g).one(m));
      Tensor rhs = StGroup.INSTANCE.element(g).adjoint(StExponential.INSTANCE.log(m));
      Tolerance.CHOP.requireClose(lhs, rhs);
    }
  }
}
