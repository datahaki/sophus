// code by jph
package ch.alpine.sophus.ref.d1h;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.TensorIteration;
import ch.alpine.sophus.math.Do;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Differences;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.Reverse;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.num.Polynomial;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

public class RnHermite2SubdivisionTest {
  static final List<HermiteSubdivision> LIST = Arrays.asList( //
      RnHermite2Subdivisions.standard(), //
      RnHermite2Subdivisions.manifold());

  @Test
  public void testStringReverse() {
    Tensor cp1 = RandomVariate.of(NormalDistribution.standard(), 7, 2, 3);
    Tensor cp2 = cp1.copy();
    cp2.set(Tensor::negate, Tensor.ALL, 1);
    for (HermiteSubdivision hermiteSubdivision : LIST) {
      TensorIteration hs1 = hermiteSubdivision.string(RealScalar.ONE, cp1);
      TensorIteration hs2 = hermiteSubdivision.string(RealScalar.ONE, Reverse.of(cp2));
      for (int count = 0; count < 3; ++count) {
        Tensor result1 = hs1.iterate();
        Tensor result2 = Reverse.of(hs2.iterate());
        result2.set(Tensor::negate, Tensor.ALL, 1);
        Chop._12.requireClose(result1, result2);
      }
    }
  }

  @Test
  public void testLinearReproduction() {
    Polynomial f0 = Polynomial.of(Tensors.vector(5, -3));
    Polynomial f1 = f0.derivative();
    Tensor domain = Range.of(0, 10);
    Tensor control = Transpose.of(Tensors.of(domain.map(f0), domain.map(f1)));
    for (HermiteSubdivision hermiteSubdivision : LIST) {
      TensorIteration tensorIteration = hermiteSubdivision.string(RealScalar.ONE, control);
      Tensor iterate = Do.of(tensorIteration::iterate, 3);
      ExactTensorQ.require(iterate);
      assertEquals(iterate.length(), 33 * 2);
      Tensor id1 = Differences.of(iterate);
      Tensor id2 = Differences.of(id1);
      Chop.NONE.requireAllZero(id2);
    }
  }
}
