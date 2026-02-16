// code by jph
package ch.alpine.sophus.hs.rs;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import ch.alpine.tensor.Rational;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.Tolerance;

class Se2inR2STest {
  @ParameterizedTest
  @EnumSource
  void test(Se2inR2S se2inR2S) {
    Tensor p = Tensors.vector(0, 0, 0);
    Tensor q = Tensors.vector(2, 0, 0);
    Tensor r = se2inR2S.split(p, q, Rational.HALF);
    Tolerance.CHOP.requireClose(r, UnitVector.of(3, 0));
  }
}
