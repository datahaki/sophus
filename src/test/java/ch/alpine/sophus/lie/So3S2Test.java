// code by jph
package ch.alpine.sophus.lie;

import java.util.function.BinaryOperator;

import ch.alpine.sophus.hs.ad.HsAlgebra;
import ch.alpine.sophus.lie.so3.So3Algebra;
import ch.alpine.sophus.lie.so3.So3Exponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class So3S2Test extends TestCase {
  public void testSo3H() {
    Tensor so3 = So3Algebra.INSTANCE.ad();
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    Tensor g = RandomVariate.of(distribution, 3);
    Tensor h = UnitVector.of(3, 2).multiply(RandomVariate.of(distribution));
    BinaryOperator<Tensor> bch = So3Algebra.INSTANCE.bch(6);
    HsAlgebra hsAlgebra = new HsAlgebra(so3, 2, 8);
    Tensor prj_g = hsAlgebra.projection(g);
    Tensor res = bch.apply(g, h);
    Tensor prj_gh = hsAlgebra.projection(res);
    Chop._10.requireClose(prj_g, prj_gh);
  }

  public void testSo3S2() {
    Tensor so3 = So3Algebra.INSTANCE.ad();
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    Tensor g = RandomVariate.of(distribution, 3);
    Tensor m = RandomVariate.of(distribution, 2);
    HsAlgebra hsAlgebra = new HsAlgebra(so3, 2, 8);
    Tensor expect = hsAlgebra.action(g, m);
    Tensor rotG = So3Exponential.INSTANCE.exp(g);
    Tensor rotM = So3Exponential.INSTANCE.exp(hsAlgebra.lift(m));
    Tensor log = So3Exponential.INSTANCE.log(rotG.dot(rotM));
    Tensor prj = hsAlgebra.projection(log);
    Tolerance.CHOP.requireClose(expect, prj);
  }
}
