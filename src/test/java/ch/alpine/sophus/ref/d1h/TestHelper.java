// code by jph
package ch.alpine.sophus.ref.d1h;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import ch.alpine.sophus.api.TensorIteration;
import ch.alpine.sophus.math.Do;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.num.Polynomial;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.d.DiscreteUniformDistribution;
import ch.alpine.tensor.qty.Quantity;

/* package */ enum TestHelper {
  ;
  private static void checkString(HermiteSubdivision hs1, HermiteSubdivision hs2) {
    Distribution distribution = DiscreteUniformDistribution.of(-5, 6);
    Tensor control = RandomVariate.of(distribution, 4, 2);
    TensorIteration tensorIteration1 = hs1.string(RealScalar.ONE, control);
    TensorIteration tensorIteration2 = hs2.string(RealScalar.ONE, control);
    for (int count = 0; count < 6; ++count) {
      Tensor it1 = tensorIteration1.iterate();
      Tensor it2 = tensorIteration2.iterate();
      assertEquals(it1, it2);
      ExactTensorQ.require(it1);
      ExactTensorQ.require(it2);
    }
  }

  private static void checkCyclic(HermiteSubdivision hs1, HermiteSubdivision hs2) {
    Distribution distribution = DiscreteUniformDistribution.of(-5, 6);
    Tensor control = RandomVariate.of(distribution, 4, 2);
    TensorIteration tensorIteration1 = hs1.cyclic(RealScalar.ONE, control);
    TensorIteration tensorIteration2 = hs2.cyclic(RealScalar.ONE, control);
    for (int count = 0; count < 6; ++count) {
      Tensor it1 = tensorIteration1.iterate();
      Tensor it2 = tensorIteration2.iterate();
      assertEquals(it1, it2);
      ExactTensorQ.require(it1);
      ExactTensorQ.require(it2);
    }
  }

  public static void check(HermiteSubdivision hs1, HermiteSubdivision hs2) {
    checkString(hs1, hs2);
    checkCyclic(hs1, hs2);
  }

  // ---
  public static void checkP(int n, HermiteSubdivision hermiteSubdivision) {
    Distribution distribution = DiscreteUniformDistribution.of(-5, 6);
    Tensor coeffs = RandomVariate.of(distribution, n + 1);
    Polynomial f0 = Polynomial.of(coeffs);
    Polynomial f1 = f0.derivative();
    Tensor domain = Range.of(0, 10);
    Tensor control = Transpose.of(Tensors.of(domain.map(f0), domain.map(f1)));
    TensorIteration tensorIteration = hermiteSubdivision.string(RealScalar.ONE, control);
    Tensor iterate = tensorIteration.iterate();
    ExactTensorQ.require(iterate);
    Tensor idm = Range.of(0, 19).multiply(RationalScalar.HALF);
    Tensor if0 = iterate.get(Tensor.ALL, 0);
    assertEquals(if0, idm.map(f0));
    Tensor if1 = iterate.get(Tensor.ALL, 1);
    assertEquals(if1, idm.map(f1));
  }

  public static void checkQuantity(HermiteSubdivision hermiteSubdivision) throws ClassNotFoundException, IOException {
    Tensor control = Tensors.fromString("{{0[m], 0[m*s^-1]}, {1[m], 0[m*s^-1]}, {0[m], -1[m*s^-1]}, {0[m], 0[m*s^-1]}}");
    HermiteSubdivision copy = Serialization.copy(hermiteSubdivision);
    {
      TensorIteration tensorIteration = copy.string(Quantity.of(3, "s"), control);
      ExactTensorQ.require(Do.of(tensorIteration::iterate, 2));
    }
    {
      TensorIteration tensorIteration = copy.cyclic(Quantity.of(3, "s"), control);
      ExactTensorQ.require(Do.of(tensorIteration::iterate, 2));
    }
  }
}
