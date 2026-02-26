// code by jph
package ch.alpine.sophus.lie.td;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixExp;

class TdExponentialTest {
  @Test
  void test() {
    Tensor v = Tensors.vector(1, 2, 3);
    TdExponential tdExponential = TdExponential.INSTANCE;
    Tensor exp = tdExponential.exp(v);
    Tensor tensor = tdExponential.gl_representation(v);
    Tensor of = MatrixExp.of(tensor);
    Tolerance.CHOP.requireClose(exp.extract(0, 2), of.get(2).extract(0, 2));
  }
}
