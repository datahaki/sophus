// code by jph
package ch.alpine.sophus.ref.d1h;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.TensorIteration;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Reverse;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class RnHermite1SubdivisionTest {
  @Test
  public void testString() {
    Tensor control = Tensors.fromString("{{0, 0}, {1, 0}, {0, -1}, {0, 0}}");
    TensorIteration hermiteSubdivision = RnHermite1Subdivisions.instance().string(RealScalar.ONE, control);
    Tensor iterate = hermiteSubdivision.iterate();
    Tensor expect = Tensors.fromString("{{0, 0}, {1/2, 3/2}, {1, 0}, {5/8, -5/4}, {0, -1}, {-1/8, 1/4}, {0, 0}}");
    assertEquals(iterate, expect);
    ExactTensorQ.require(iterate);
    iterate = hermiteSubdivision.iterate();
    // System.out.println(iterate);
    String string = "{{0, 0}, {5/32, 9/8}, {1/2, 3/2}, {27/32, 9/8}, {1, 0}, {57/64, -13/16}, {5/8, -5/4}, {19/64, -21/16}, {0, -1}, {-9/64, -3/16}, {-1/8, 1/4}, {-3/64, 5/16}, {0, 0}}";
    assertEquals(iterate, Tensors.fromString(string));
    ExactTensorQ.require(iterate);
  }

  @Test
  public void testStringReverse() {
    Tensor cp1 = RandomVariate.of(NormalDistribution.standard(), 7, 2, 3);
    Tensor cp2 = cp1.copy();
    cp2.set(Tensor::negate, Tensor.ALL, 1);
    TensorIteration hs1 = RnHermite1Subdivisions.instance().string(RealScalar.ONE, cp1);
    TensorIteration hs2 = RnHermite1Subdivisions.instance().string(RealScalar.ONE, Reverse.of(cp2));
    for (int count = 0; count < 3; ++count) {
      Tensor result1 = hs1.iterate();
      Tensor result2 = Reverse.of(hs2.iterate());
      result2.set(Tensor::negate, Tensor.ALL, 1);
      Chop._12.requireClose(result1, result2);
    }
  }

  @Test
  public void testCyclic() {
    Tensor control = Tensors.fromString("{{0, 0}, {1, 0}, {0, -1}, {-1/2, 1}}");
    TensorIteration tensorIteration = RnHermite1Subdivisions.instance().cyclic(RealScalar.ONE, control);
    Tensor iterate = tensorIteration.iterate();
    Tensor expect = Tensors.fromString("{{0, 0}, {1/2, 3/2}, {1, 0}, {5/8, -5/4}, {0, -1}, {-1/2, -3/4}, {-1/2, 1}, {-1/8, 1/2}}");
    assertEquals(iterate, expect);
    ExactTensorQ.require(iterate);
  }

  @Test
  public void testPolynomialReproduction() {
    TestHelper.checkP(3, RnHermite1Subdivisions.instance());
  }
}
