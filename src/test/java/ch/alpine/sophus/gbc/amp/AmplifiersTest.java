// code by jph
package ch.alpine.sophus.gbc.amp;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.sca.Chop;

class AmplifiersTest {
  @Test
  void testSimple() {
    List<TensorUnaryOperator> list = Arrays.asList( //
        Amplifiers.EXP.supply(3), Amplifiers.EXP.supply(5), //
        Amplifiers.RAMP.supply(3), Amplifiers.RAMP.supply(5), //
        Amplifiers.ARCTAN.supply(3), Amplifiers.ARCTAN.supply(5));
    for (TensorUnaryOperator suo : list) {
      Tensor weights = suo.apply(Tensors.vector(1));
      Chop._12.requireClose(weights, Tensors.vector(1));
    }
  }
}
