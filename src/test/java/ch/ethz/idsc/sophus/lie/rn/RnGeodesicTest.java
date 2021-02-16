// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.hs.Biinvariant;
import ch.ethz.idsc.sophus.hs.Biinvariants;
import ch.ethz.idsc.sophus.hs.MetricBiinvariant;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.itp.DeBoor;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
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
