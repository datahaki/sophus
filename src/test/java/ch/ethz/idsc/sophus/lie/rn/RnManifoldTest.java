// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.gbc.AffineWrap;
import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.GbcHelper;
import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.Biinvariants;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.math.TensorMapping;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class RnManifoldTest extends TestCase {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(RnGroup.INSTANCE);

  public void testSimple() {
    Distribution distribution = NormalDistribution.standard();
    for (int n = 2; n < 5; ++n)
      for (int length = n + 1; length < 10; ++length) {
        Tensor points = RandomVariate.of(distribution, length, n);
        Tensor mean = RandomVariate.of(distribution, n);
        for (BarycentricCoordinate barycentricCoordinate : GbcHelper.barycentricsfull(RnManifold.INSTANCE)) {
          Tensor weights = barycentricCoordinate.weights(points, mean);
          Tensor result = RnBiinvariantMean.INSTANCE.mean(points, weights);
          Chop._08.requireClose(mean, result);
        }
      }
  }

  public void testRandom() {
    Distribution distribution = UniformDistribution.unit();
    BiinvariantMean biinvariantMean = RnBiinvariantMean.INSTANCE;
    for (int n = 2; n < 5; ++n)
      for (int length = n + 1; length < 10; ++length) {
        Tensor points = RandomVariate.of(distribution, length, n);
        Tensor xya = RandomVariate.of(distribution, n);
        for (BarycentricCoordinate barycentricCoordinate : GbcHelper.barycentricsfull(RnManifold.INSTANCE)) {
          Tensor weights = barycentricCoordinate.weights(points, xya);
          Chop._10.requireClose(Total.ofVector(weights), RealScalar.ONE);
          Tensor x_recreated = biinvariantMean.mean(points, weights);
          Chop._06.requireClose(xya, x_recreated);
          Tensor shift = RandomVariate.of(distribution, n);
          for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift))
            Chop._04.requireClose(weights, //
                barycentricCoordinate.weights(tensorMapping.slash(points), tensorMapping.apply(xya)));
        }
      }
  }

  public void testLinearReproduction() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 2; n < 6; ++n)
      for (int length = n + 1; length < 10; ++length) {
        Tensor points = RandomVariate.of(distribution, length, n);
        Tensor x = RandomVariate.of(distribution, n);
        TensorUnaryOperator tensorUnaryOperator = //
            Biinvariants.METRIC.coordinate(RnManifold.INSTANCE, InversePowerVariogram.of(1), points);
        Tensor weights = tensorUnaryOperator.apply(x);
        Tensor y = RnBiinvariantMean.INSTANCE.mean(points, weights);
        Chop._06.requireClose(x, y);
      }
  }

  public void testLagrangeProperty() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 2; n < 6; ++n)
      for (int length = n + 1; length < 10; ++length) {
        Tensor points = RandomVariate.of(distribution, length, n);
        TensorUnaryOperator tensorUnaryOperator = //
            Biinvariants.METRIC.coordinate(RnManifold.INSTANCE, InversePowerVariogram.of(1), points);
        Chop._10.requireClose(Tensor.of(points.stream().map(tensorUnaryOperator)), IdentityMatrix.of(length));
      }
  }

  public void testQuantity() {
    Distribution distribution = UniformDistribution.of(Quantity.of(-1, "m"), Quantity.of(+1, "m"));
    for (int n = 2; n < 6; ++n)
      for (int length = n + 1; length < 10; ++length) {
        Tensor points = RandomVariate.of(distribution, length, n);
        Tensor x = RandomVariate.of(distribution, n);
        TensorUnaryOperator tensorUnaryOperator = //
            Biinvariants.METRIC.coordinate(RnManifold.INSTANCE, InversePowerVariogram.of(1), points);
        Tensor weights = tensorUnaryOperator.apply(x);
        Tensor y = RnBiinvariantMean.INSTANCE.mean(points, weights);
        Chop._10.requireClose(x, y);
      }
  }

  public void testAffineSimple() {
    BarycentricCoordinate barycentricCoordinate = AffineWrap.of(RnManifold.INSTANCE);
    for (int dim = 2; dim < 4; ++dim)
      for (int length = dim + 1; length < 10; ++length) {
        Distribution distribution = NormalDistribution.standard();
        Tensor sequence = RandomVariate.of(distribution, length, dim);
        Tensor mean = RandomVariate.of(distribution, dim);
        Tensor lhs = barycentricCoordinate.weights(sequence, mean);
        Tensor rhs = RnAffineCoordinate.INSTANCE.weights(sequence, mean);
        Chop._06.requireClose(lhs, rhs);
      }
  }

  public void testNullFail() {
    for (BarycentricCoordinate barycentricCoordinate : GbcHelper.barycentricsfull(RnManifold.INSTANCE))
      try {
        barycentricCoordinate.weights(null, null);
        fail();
      } catch (Exception exception) {
        // ---
      }
  }

  public void testColinear() {
    int d = 2;
    int n = 5;
    for (BarycentricCoordinate barycentricCoordinate : GbcHelper.barycentricsfull(RnManifold.INSTANCE)) {
      Tensor sequence = RandomVariate.of(NormalDistribution.standard(), n, d);
      sequence.append(sequence.get(n - 1).multiply(RealScalar.of(5)));
      Tensor weights = barycentricCoordinate.weights(sequence, Array.zeros(d));
      assertEquals(sequence.length(), n + 1);
      AffineQ.require(weights);
    }
  }
}
