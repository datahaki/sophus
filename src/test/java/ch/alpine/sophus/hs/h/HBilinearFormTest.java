package ch.alpine.sophus.hs.h;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.Tolerance;

class HBilinearFormTest {
  @Test
  void test() {
    Tensor p = Tensors.vector(1, 0);
    HBilinearForm hBilinearForm = HBilinearForm.of(p);
    Tensor form = Tensors.matrix((i, j) -> hBilinearForm.formEval(UnitVector.of(2, i), UnitVector.of(2, j)), 2, 2);
    Tolerance.CHOP.requireClose(form, Tensors.fromString("{{1/2,0},{0,1}}"));
  }
}
