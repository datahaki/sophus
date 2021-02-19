// code by jph
package ch.ethz.idsc.sophus.hs.s2;

import java.io.IOException;

import ch.ethz.idsc.sophus.hs.sn.S2Loxodrome;
import ch.ethz.idsc.sophus.hs.sn.SnMemberQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Subdivide;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.api.ScalarTensorFunction;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.ArcTan;
import ch.ethz.idsc.tensor.sca.Cos;
import ch.ethz.idsc.tensor.sca.Sin;
import junit.framework.TestCase;

public class S2LoxodromeTest extends TestCase {
  private static Tensor prev(Scalar angle, Scalar scalar) {
    Scalar f = ArcTan.FUNCTION.apply(scalar.multiply(angle));
    Scalar cf = Cos.FUNCTION.apply(f);
    return Tensors.of( //
        Cos.FUNCTION.apply(scalar).multiply(cf), //
        Sin.FUNCTION.apply(scalar).multiply(cf), //
        Sin.FUNCTION.apply(f));
  }

  public void testSimple() throws ClassNotFoundException, IOException {
    ScalarTensorFunction scalarTensorFunction = Serialization.copy(S2Loxodrome.of(RealScalar.of(0.1)));
    Tensor tensor = Subdivide.of(-1, 100, 60).map(scalarTensorFunction);
    assertTrue(tensor.stream().allMatch(SnMemberQ.INSTANCE::test));
  }

  public void testParamZeo() {
    Tensor first = S2Loxodrome.of(0.3).apply(RealScalar.ZERO);
    assertEquals(first, UnitVector.of(3, 0));
  }

  public void testPrevious() {
    Scalar angle = RandomVariate.of(NormalDistribution.standard());
    Scalar scalar = RandomVariate.of(NormalDistribution.standard());
    Tolerance.CHOP.requireClose( //
        S2Loxodrome.of(angle).apply(scalar), //
        prev(angle, scalar));
  }
}
