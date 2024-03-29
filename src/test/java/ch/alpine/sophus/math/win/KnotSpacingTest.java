// code by jph
package ch.alpine.sophus.math.win;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.r2.Se2Parametric;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.sca.Chop;

class KnotSpacingTest {
  @Test
  void testSimple() {
    TensorUnaryOperator centripetalKnotSpacing = KnotSpacing.centripetal(Se2Parametric.INSTANCE, 0.5);
    Tensor knots = centripetalKnotSpacing.apply(Tensors.fromString("{{1, 2, 3}, {4, 5, 6}, {8, 9, 11}}"));
    Chop._12.requireClose(knots, Tensors.vector(0, 2.525854879647931, 4.988462479155103));
  }

  @Test
  void testUniform() {
    TensorUnaryOperator uniform = KnotSpacing.uniform();
    TensorUnaryOperator power_0 = KnotSpacing.centripetal(Se2Parametric.INSTANCE, 0);
    Tensor control = Tensors.fromString("{{1, 2, 3}, {4, 5, 6}, {8, 9, 11}}");
    Tensor knots = uniform.apply(control);
    assertEquals(knots, Range.of(0, 3));
    assertEquals(knots, power_0.apply(control));
  }

  @Test
  void testChordal() {
    TensorUnaryOperator chordal = KnotSpacing.chordal(Se2Parametric.INSTANCE);
    TensorUnaryOperator power_1 = KnotSpacing.centripetal(Se2Parametric.INSTANCE, 1);
    Tensor control = Tensors.fromString("{{1, 2, 3}, {4, 5, 6}, {8, 9, 11}}");
    Chop._12.requireClose(chordal.apply(control), power_1.apply(control));
  }

  @Test
  void testSerializable() throws ClassNotFoundException, IOException {
    TensorUnaryOperator centripetalKnotSpacing = //
        Serialization.copy(KnotSpacing.centripetal(Se2Parametric.INSTANCE, 0.5));
    Tensor knots = centripetalKnotSpacing.apply(Tensors.fromString("{{1, 2, 3}, {4, 5, 6}, {8, 9, 11}}"));
    Chop._12.requireClose(knots, Tensors.vector(0, 2.525854879647931, 4.988462479155103));
  }

  @Test
  void testEmpty() {
    TensorUnaryOperator centripetalKnotSpacing = KnotSpacing.centripetal(Se2Parametric.INSTANCE, 0.75);
    assertEquals(centripetalKnotSpacing.apply(Tensors.empty()), Tensors.empty());
    assertEquals(centripetalKnotSpacing.apply(Tensors.fromString("{{2, 3, 4}}")), Tensors.vector(0));
  }

  @Test
  void testScalarFail() {
    TensorUnaryOperator centripetalKnotSpacing = KnotSpacing.centripetal(Se2Parametric.INSTANCE, 0.25);
    assertThrows(Exception.class, () -> centripetalKnotSpacing.apply(RealScalar.ONE));
  }

  @Test
  void testChordalFail() {
    assertThrows(Exception.class, () -> KnotSpacing.chordal(null));
  }
}
