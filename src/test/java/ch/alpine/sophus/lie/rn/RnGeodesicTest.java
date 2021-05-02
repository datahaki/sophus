// code by jph
package ch.alpine.sophus.lie.rn;

import ch.alpine.sophus.hs.Biinvariant;
import ch.alpine.sophus.hs.Biinvariants;
import ch.alpine.sophus.hs.MetricBiinvariant;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.itp.DeBoor;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.NormalDistribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class RnGeodesicTest extends TestCase {
  public void testSimple() {
    Tensor actual = RnGeodesic.INSTANCE.split(Tensors.vector(10, 1), Tensors.vector(11, 0), RealScalar.of(-1));
    ExactTensorQ.require(actual);
    assertEquals(Tensors.vector(9, 2), actual);
  }

  public void testEndPoints() {
    Distribution distribution = NormalDistribution.standard();
    for (int index = 0; index < 10; ++index) {
      Tensor p = RandomVariate.of(distribution, 7);
      Tensor q = RandomVariate.of(distribution, 7);
      Chop._14.requireClose(p, RnGeodesic.INSTANCE.split(p, q, RealScalar.ZERO));
      Chop._14.requireClose(q, RnGeodesic.INSTANCE.split(p, q, RealScalar.ONE));
    }
  }

  public void testDeBoor() {
    Tensor knots = Tensors.vector(1, 2, 3, 4);
    Tensor control = Tensors.vector(9, 3, 4);
    DeBoor.of(RnGeodesic.INSTANCE, knots, control);
    AssertFail.of(() -> DeBoor.of(null, knots, control));
  }

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
