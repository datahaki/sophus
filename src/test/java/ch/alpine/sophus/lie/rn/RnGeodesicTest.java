// code by jph
package ch.alpine.sophus.lie.rn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.Biinvariant;
import ch.alpine.sophus.hs.Biinvariants;
import ch.alpine.sophus.hs.MetricBiinvariant;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.itp.DeBoor;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

public class RnGeodesicTest {
  @Test
  public void testSimple() {
    Tensor actual = RnGeodesic.INSTANCE.split(Tensors.vector(10, 1), Tensors.vector(11, 0), RealScalar.of(-1));
    ExactTensorQ.require(actual);
    assertEquals(Tensors.vector(9, 2), actual);
  }

  @Test
  public void testEndPoints() {
    Distribution distribution = NormalDistribution.standard();
    for (int index = 0; index < 10; ++index) {
      Tensor p = RandomVariate.of(distribution, 7);
      Tensor q = RandomVariate.of(distribution, 7);
      Chop._14.requireClose(p, RnGeodesic.INSTANCE.split(p, q, RealScalar.ZERO));
      Chop._14.requireClose(q, RnGeodesic.INSTANCE.split(p, q, RealScalar.ONE));
    }
  }

  @Test
  public void testDeBoor() {
    Tensor knots = Tensors.vector(1, 2, 3, 4);
    Tensor control = Tensors.vector(9, 3, 4);
    DeBoor.of(RnGeodesic.INSTANCE, knots, control);
    assertThrows(Exception.class, () -> DeBoor.of(null, knots, control));
  }

  @Test
  public void testSymmetric() {
    int d = 2;
    int n = 5;
    Tensor sequence = RandomVariate.of(NormalDistribution.standard(), n, d);
    for (Biinvariant biinvariant : new Biinvariant[] { MetricBiinvariant.EUCLIDEAN, Biinvariants.HARBOR }) {
      TensorUnaryOperator tensorUnaryOperator = biinvariant.distances(RnManifold.INSTANCE, sequence);
      Tensor vardst = Tensor.of(sequence.stream().map(tensorUnaryOperator));
      SymmetricMatrixQ.require(vardst);
    }
    {
      TensorUnaryOperator tensorUnaryOperator = Biinvariants.LEVERAGES.distances(RnManifold.INSTANCE, sequence);
      Tensor vardst = Tensor.of(sequence.stream().map(tensorUnaryOperator));
      assertFalse(SymmetricMatrixQ.of(vardst));
    }
  }
}
