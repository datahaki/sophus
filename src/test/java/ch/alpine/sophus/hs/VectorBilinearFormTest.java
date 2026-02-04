package ch.alpine.sophus.hs;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.api.FrobeniusForm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.ComplexDiskUniformDistribution;

class VectorBilinearFormTest {
  @Test
  void testSquare() {
    Tensor v = RandomVariate.of(ComplexDiskUniformDistribution.of(0.3), 4);
    Scalar form = FrobeniusForm.INSTANCE.formEval(v, v);
    Scalar norm = FrobeniusForm.INSTANCE.norm(v);
    Tolerance.CHOP.requireClose(form, norm.multiply(norm));
    Scalar f1 = VectorBilinearForm.INSTANCE.formEval(v, v);
    Scalar n1 = VectorBilinearForm.INSTANCE.norm(v);
    Tolerance.CHOP.requireClose(f1, n1.multiply(n1));
    Tolerance.CHOP.requireClose(form, f1);
  }
}
