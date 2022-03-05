// code by jph
package ch.alpine.sophus.ref.d1h;

import java.util.Arrays;
import java.util.List;

import ch.alpine.sophus.api.TensorIteration;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Reverse;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class RnHermite3SubdivisionsTest extends TestCase {
  static final List<HermiteSubdivision> LIST = Arrays.asList( //
      RnHermite3Subdivisions.a1(), //
      RnHermite3Subdivisions.a2());

  public void testString() {
    Tensor control = Tensors.fromString("{{0, 0}, {1, 0}, {0, -1}, {-1/2, 1}}");
    for (HermiteSubdivision hermiteSubdivision : LIST) {
      TensorIteration tensorIteration = hermiteSubdivision.string(RealScalar.ONE, control);
      Tensor tensor = tensorIteration.iterate();
      ExactTensorQ.require(tensor);
      assertEquals(tensor.length(), 7);
    }
  }

  public void testStringReverse() {
    Tensor cp1 = RandomVariate.of(NormalDistribution.standard(), 7, 2, 3);
    Tensor cp2 = cp1.copy();
    cp2.set(Tensor::negate, Tensor.ALL, 1);
    for (HermiteSubdivision hermiteSubdivision : LIST) {
      TensorIteration ti1 = hermiteSubdivision.string(RealScalar.ONE, cp1);
      TensorIteration ti2 = hermiteSubdivision.string(RealScalar.ONE, Reverse.of(cp2));
      for (int count = 0; count < 3; ++count) {
        Tensor result1 = ti1.iterate();
        Tensor result2 = Reverse.of(ti2.iterate());
        result2.set(Tensor::negate, Tensor.ALL, 1);
        Chop._12.requireClose(result1, result2);
      }
    }
  }

  public void testCyclic() {
    Tensor control = Tensors.fromString("{{0, 0}, {1, 0}, {0, -1}, {-1/2, 1}}");
    for (HermiteSubdivision hermiteSubdivision : LIST) {
      TensorIteration tensorIteration = hermiteSubdivision.cyclic(RealScalar.ONE, control);
      Tensor tensor = tensorIteration.iterate();
      ExactTensorQ.require(tensor);
      assertEquals(tensor.length(), 8);
    }
  }

  public void testPolynomialReproduction() {
    for (HermiteSubdivision hermiteSubdivision : LIST)
      TestHelper.checkP(1, hermiteSubdivision);
  }
}
