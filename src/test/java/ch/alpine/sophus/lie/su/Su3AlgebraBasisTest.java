package ch.alpine.sophus.lie.su;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.HermitianMatrixQ;
import ch.alpine.tensor.red.Trace;
import ch.alpine.tensor.sca.Chop;

class Su3AlgebraBasisTest {
  @Test
  void testHermitian() {
    Su3AlgebraBasis.basis().stream().forEach(HermitianMatrixQ::require);
    Tensor tensor = Tensor.of(Su3AlgebraBasis.basis().stream().map(Trace::of));
    Chop.NONE.requireAllZero(tensor);
  }
}
