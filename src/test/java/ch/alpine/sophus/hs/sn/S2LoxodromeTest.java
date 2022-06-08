// code by jph
package ch.alpine.sophus.hs.sn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.tri.ArcTan;
import ch.alpine.tensor.sca.tri.Cos;
import ch.alpine.tensor.sca.tri.Sin;

class S2LoxodromeTest {
  private static Tensor prev(Scalar angle, Scalar scalar) {
    Scalar f = ArcTan.FUNCTION.apply(scalar.multiply(angle));
    Scalar cf = Cos.FUNCTION.apply(f);
    return Tensors.of( //
        Cos.FUNCTION.apply(scalar).multiply(cf), //
        Sin.FUNCTION.apply(scalar).multiply(cf), //
        Sin.FUNCTION.apply(f));
  }

  @Test
  public void testSimple() throws ClassNotFoundException, IOException {
    ScalarTensorFunction scalarTensorFunction = Serialization.copy(S2Loxodrome.of(RealScalar.of(0.1)));
    Tensor tensor = Subdivide.of(-1, 100, 60).map(scalarTensorFunction);
    assertTrue(tensor.stream().allMatch(SnMemberQ.INSTANCE::test));
  }

  @Test
  public void testParamZeo() {
    Tensor first = S2Loxodrome.of(0.3).apply(RealScalar.ZERO);
    assertEquals(first, UnitVector.of(3, 0));
  }

  @Test
  public void testPrevious() {
    Scalar angle = RandomVariate.of(NormalDistribution.standard());
    Scalar scalar = RandomVariate.of(NormalDistribution.standard());
    Tolerance.CHOP.requireClose( //
        S2Loxodrome.of(angle).apply(scalar), //
        prev(angle, scalar));
  }
}
