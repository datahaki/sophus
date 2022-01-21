// code by jph
package ch.alpine.sophus.lie.so3;

import java.util.Random;

import ch.alpine.sophus.hs.sn.SnExponential;
import ch.alpine.sophus.lie.HsAlgebra;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class So3AlgebraTest extends TestCase {
  public void testSimple() {
    Tensor x = Tensors.vector(0.1, 0.2, 0.05);
    Tensor y = Tensors.vector(0.02, -0.1, -0.04);
    Tensor mX = Rodrigues.vectorExp(x);
    Tensor mY = Rodrigues.vectorExp(y);
    Tensor res = Rodrigues.INSTANCE.vectorLog(mX.dot(mY));
    Tensor z = So3Algebra.INSTANCE.bch(6).apply(x, y);
    Chop._08.requireClose(z, res);
  }

  public void testAlg() {
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    HsAlgebra hsAlgebra = new HsAlgebra(So3Algebra.INSTANCE.ad(), 2, 6);
    Random random = new Random();
    for (int count = 0; count < 10; ++count) {
      // Tensor ga = Tensors.vector(0.0, 0.0, 0.1);
      Tensor ga = RandomVariate.of(distribution, random, 3);
      Tensor m1 = RandomVariate.of(distribution, random, 2);
      // System.out.println("m1=" + m1.map(Round._4));
      Tensor m2 = hsAlgebra.action(ga, m1);
      // System.out.println("m2=" + m2.map(Round._4));
      Tensor p = UnitVector.of(3, 2);
      // TODO investigate where this "change of coordinates" comes from
      Tensor g = Tensors.of(ga.Get(1).negate(), ga.Get(0), ga.Get(2));
      Tensor rotation = So3Exponential.INSTANCE.exp(g);
      SnExponential snExponential = new SnExponential(p);
      Tensor v1 = hsAlgebra.lift(m1);
      Tensor p1 = snExponential.exp(v1);
      // System.out.println("p1=" + p1.map(Round._4));
      Tensor p2 = rotation.dot(p1);
      // System.out.println("p2=" + p2.map(Round._4));
      Tensor v2 = snExponential.log(p2);
      // System.out.println("v2="+v2.map(Round._4));
      Chop._06.requireClose(hsAlgebra.lift(m2), v2);
    }
  }

  public void testActionH() {
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    HsAlgebra hsAlgebra = new HsAlgebra(So3Algebra.INSTANCE.ad(), 2, 6);
    Tensor h = Join.of(Array.zeros(2), RandomVariate.of(distribution, 1));
    Tensor m1 = RandomVariate.of(distribution, 2);
    Tensor m2 = hsAlgebra.action(h, m1);
    Tensor p = UnitVector.of(3, 2);
    Tensor rotation = So3Exponential.INSTANCE.exp(h);
    SnExponential snExponential = new SnExponential(p);
    Tensor v1 = hsAlgebra.lift(m1);
    Tensor snm = snExponential.exp(v1);
    Tensor dot = rotation.dot(snm);
    // System.out.println(dot);
    Tensor v2 = snExponential.log(dot);
    Tolerance.CHOP.requireClose(hsAlgebra.lift(m2), v2);
  }
}
