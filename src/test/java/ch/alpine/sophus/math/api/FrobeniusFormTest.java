// code by jph
package ch.alpine.sophus.math.api;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.ComplexDiskUniformDistribution;
import ch.alpine.tensor.red.Times;
import ch.alpine.tensor.sca.pow.Sqrt;

class FrobeniusFormTest {
  @Test
  void testSquare() {
    Tensor v = RandomVariate.of(ComplexDiskUniformDistribution.of(0.3), 4, 4);
    Scalar form = Sqrt.FUNCTION.apply(FrobeniusForm.INSTANCE.formEval(v, v));
    Scalar norm = FrobeniusForm.INSTANCE.norm(v);
    Tolerance.CHOP.requireClose(form, norm);
  }

  @Test
  void testRect() {
    Tensor v = RandomVariate.of(ComplexDiskUniformDistribution.of(0.3), 4, 7);
    Scalar form = Sqrt.FUNCTION.apply(FrobeniusForm.INSTANCE.formEval(v, v));
    Scalar norm = FrobeniusForm.INSTANCE.norm(v);
    Tolerance.CHOP.requireClose(form, norm);
  }

  @Test
  void testFormFail() {
    Tensor u = RandomVariate.of(ComplexDiskUniformDistribution.of(0.3), 4);
    Tensor v = RandomVariate.of(ComplexDiskUniformDistribution.of(0.3), 4, 4);
    Times.of(u, v);
    assertThrows(Exception.class, () -> FrobeniusForm.INSTANCE.formEval(u, v));
  }
}
