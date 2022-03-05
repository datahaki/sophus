// code by jph
package ch.alpine.sophus.lie.rn;

import java.util.Random;

import ch.alpine.sophus.api.TensorMapping;
import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.gbc.AffineWrap;
import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.gbc.GbcHelper;
import ch.alpine.sophus.hs.MetricBiinvariant;
import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class RnManifoldTest extends TestCase {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(RnGroup.INSTANCE);

  public void testSimple() {
    Random random = new Random();
    Distribution distribution = NormalDistribution.standard();
    for (int n = 2; n < 5; ++n) {
      int length = n + 1 + random.nextInt(3);
      Tensor points = RandomVariate.of(distribution, length, n);
      Tensor mean = RandomVariate.of(distribution, n);
      for (BarycentricCoordinate barycentricCoordinate : GbcHelper.barycentrics(RnManifold.INSTANCE)) {
        Tensor weights = barycentricCoordinate.weights(points, mean);
        Tensor result = RnBiinvariantMean.INSTANCE.mean(points, weights);
        Chop._08.requireClose(mean, result);
      }
    }
  }

  public void testRandom() {
    Random random = new Random();
    Distribution distribution = UniformDistribution.unit();
    BiinvariantMean biinvariantMean = RnBiinvariantMean.INSTANCE;
    for (int n = 2; n < 4; ++n) {
      int length = n + 1 + random.nextInt(3);
      Tensor points = RandomVariate.of(distribution, length, n);
      Tensor xya = RandomVariate.of(distribution, n);
      for (BarycentricCoordinate barycentricCoordinate : GbcHelper.barycentrics(RnManifold.INSTANCE)) {
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
    Random random = new Random();
    Distribution distribution = UniformDistribution.unit();
    for (int n = 2; n < 6; ++n) {
      int length = n + 1 + random.nextInt(3);
      Tensor points = RandomVariate.of(distribution, length, n);
      Tensor x = RandomVariate.of(distribution, n);
      TensorUnaryOperator tensorUnaryOperator = //
          MetricBiinvariant.EUCLIDEAN.coordinate(RnManifold.INSTANCE, InversePowerVariogram.of(1), points);
      Tensor weights = tensorUnaryOperator.apply(x);
      Tensor y = RnBiinvariantMean.INSTANCE.mean(points, weights);
      Chop._06.requireClose(x, y);
    }
  }

  public void testLagrangeProperty() {
    Random random = new Random();
    Distribution distribution = UniformDistribution.unit();
    for (int n = 2; n < 6; ++n) {
      int length = n + 1 + random.nextInt(3);
      Tensor points = RandomVariate.of(distribution, length, n);
      TensorUnaryOperator tensorUnaryOperator = //
          MetricBiinvariant.EUCLIDEAN.coordinate(RnManifold.INSTANCE, InversePowerVariogram.of(1), points);
      Chop._10.requireClose(Tensor.of(points.stream().map(tensorUnaryOperator)), IdentityMatrix.of(length));
    }
  }

  public void testQuantity() {
    Random random = new Random();
    Distribution distribution = UniformDistribution.of(Quantity.of(-1, "m"), Quantity.of(+1, "m"));
    for (int n = 2; n < 6; ++n) {
      int length = n + 1 + random.nextInt(3);
      Tensor points = RandomVariate.of(distribution, length, n);
      Tensor x = RandomVariate.of(distribution, n);
      TensorUnaryOperator tensorUnaryOperator = //
          MetricBiinvariant.EUCLIDEAN.coordinate(RnManifold.INSTANCE, InversePowerVariogram.of(1), points);
      Tensor weights = tensorUnaryOperator.apply(x);
      Tensor y = RnBiinvariantMean.INSTANCE.mean(points, weights);
      Chop._06.requireClose(x, y);
    }
  }

  public void testAffineSimple() {
    BarycentricCoordinate barycentricCoordinate = AffineWrap.of(RnManifold.INSTANCE);
    for (int dim = 2; dim < 4; ++dim)
      for (int length = dim + 1; length < 8; ++length) {
        Distribution distribution = NormalDistribution.standard();
        Tensor sequence = RandomVariate.of(distribution, length, dim);
        Tensor mean = RandomVariate.of(distribution, dim);
        Tensor lhs = barycentricCoordinate.weights(sequence, mean);
        Tensor rhs = RnAffineCoordinate.INSTANCE.weights(sequence, mean);
        Chop._06.requireClose(lhs, rhs);
      }
  }

  public void testNullFail() {
    for (BarycentricCoordinate barycentricCoordinate : GbcHelper.barycentrics(RnManifold.INSTANCE))
      AssertFail.of(() -> barycentricCoordinate.weights(null, null));
  }

  public void testColinear() {
    int d = 2;
    int n = 5;
    for (BarycentricCoordinate barycentricCoordinate : GbcHelper.barycentrics(RnManifold.INSTANCE)) {
      Tensor sequence = RandomVariate.of(NormalDistribution.standard(), n, d);
      sequence.append(sequence.get(n - 1).multiply(RealScalar.of(5)));
      Tensor weights = barycentricCoordinate.weights(sequence, Array.zeros(d));
      assertEquals(sequence.length(), n + 1);
      AffineQ.require(weights, Chop._08);
    }
  }
}
