// code by jph
package ch.ethz.idsc.sophus.math.win;

import java.io.IOException;

import ch.ethz.idsc.sophus.hs.r2.Se2Parametric;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Range;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class KnotSpacingTest extends TestCase {
  public void testSimple() {
    TensorUnaryOperator centripetalKnotSpacing = KnotSpacing.centripetal(Se2Parametric.INSTANCE, 0.5);
    Tensor knots = centripetalKnotSpacing.apply(Tensors.fromString("{{1, 2, 3}, {4, 5, 6}, {8, 9, 11}}"));
    Chop._12.requireClose(knots, Tensors.vector(0, 2.525854879647931, 4.988462479155103));
  }

  public void testUniform() {
    TensorUnaryOperator uniform = KnotSpacing.uniform();
    TensorUnaryOperator power_0 = KnotSpacing.centripetal(Se2Parametric.INSTANCE, 0);
    Tensor control = Tensors.fromString("{{1, 2, 3}, {4, 5, 6}, {8, 9, 11}}");
    Tensor knots = uniform.apply(control);
    assertEquals(knots, Range.of(0, 3));
    assertEquals(knots, power_0.apply(control));
  }

  public void testChordal() {
    TensorUnaryOperator chordal = KnotSpacing.chordal(Se2Parametric.INSTANCE);
    TensorUnaryOperator power_1 = KnotSpacing.centripetal(Se2Parametric.INSTANCE, 1);
    Tensor control = Tensors.fromString("{{1, 2, 3}, {4, 5, 6}, {8, 9, 11}}");
    Chop._12.requireClose(chordal.apply(control), power_1.apply(control));
  }

  public void testSerializable() throws ClassNotFoundException, IOException {
    TensorUnaryOperator centripetalKnotSpacing = //
        Serialization.copy(KnotSpacing.centripetal(Se2Parametric.INSTANCE, 0.5));
    Tensor knots = centripetalKnotSpacing.apply(Tensors.fromString("{{1, 2, 3}, {4, 5, 6}, {8, 9, 11}}"));
    Chop._12.requireClose(knots, Tensors.vector(0, 2.525854879647931, 4.988462479155103));
  }

  public void testEmpty() {
    TensorUnaryOperator centripetalKnotSpacing = KnotSpacing.centripetal(Se2Parametric.INSTANCE, 0.75);
    assertEquals(centripetalKnotSpacing.apply(Tensors.empty()), Tensors.empty());
    assertEquals(centripetalKnotSpacing.apply(Tensors.fromString("{{2, 3, 4}}")), Tensors.vector(0));
  }

  public void testScalarFail() {
    TensorUnaryOperator centripetalKnotSpacing = KnotSpacing.centripetal(Se2Parametric.INSTANCE, 0.25);
    AssertFail.of(() -> centripetalKnotSpacing.apply(RealScalar.ONE));
  }

  public void testChordalFail() {
    AssertFail.of(() -> KnotSpacing.chordal(null));
  }
}
