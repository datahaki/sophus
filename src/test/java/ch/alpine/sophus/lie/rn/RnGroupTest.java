// code by jph
package ch.alpine.sophus.lie.rn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.SecureRandom;
import java.util.Random;
import java.util.random.RandomGenerator;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.dv.AffineWrap;
import ch.alpine.sophus.dv.BarycentricCoordinate;
import ch.alpine.sophus.dv.Biinvariant;
import ch.alpine.sophus.dv.Biinvariants;
import ch.alpine.sophus.dv.GbcHelper;
import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.api.TensorMapping;
import ch.alpine.sophus.math.api.TensorMetric;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.chq.ExactScalarQ;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.itp.DeBoor;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Chop;

class RnGroupTest {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(RnGroup.INSTANCE);

  @Test
  void testSimple() {
    RandomGenerator random = new SecureRandom();
    Distribution distribution = NormalDistribution.standard();
    for (int n = 2; n < 5; ++n) {
      int length = n + 1 + random.nextInt(3);
      Tensor points = RandomVariate.of(distribution, length, n);
      Tensor mean = RandomVariate.of(distribution, n);
      for (BarycentricCoordinate barycentricCoordinate : GbcHelper.barycentrics(RnGroup.INSTANCE)) {
        Tensor weights = barycentricCoordinate.weights(points, mean);
        Tensor result = RnBiinvariantMean.INSTANCE.mean(points, weights);
        Chop._08.requireClose(mean, result);
      }
    }
  }

  @Test
  void testRandom() {
    Random random = new Random();
    Distribution distribution = UniformDistribution.unit();
    BiinvariantMean biinvariantMean = RnBiinvariantMean.INSTANCE;
    for (int n = 2; n < 4; ++n) {
      int length = n + 1 + random.nextInt(3);
      Tensor points = RandomVariate.of(distribution, length, n);
      Tensor xya = RandomVariate.of(distribution, n);
      for (BarycentricCoordinate barycentricCoordinate : GbcHelper.barycentrics(RnGroup.INSTANCE)) {
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

  @Test
  void testLinearReproduction() {
    Random random = new Random();
    Distribution distribution = UniformDistribution.unit();
    Biinvariant biinvariant = Biinvariants.METRIC.ofSafe(RnGroup.INSTANCE);
    for (int n = 2; n < 6; ++n) {
      int length = n + 1 + random.nextInt(3);
      Tensor points = RandomVariate.of(distribution, length, n);
      Tensor x = RandomVariate.of(distribution, n);
      Sedarim tensorUnaryOperator = biinvariant.coordinate(InversePowerVariogram.of(1), points);
      Tensor weights = tensorUnaryOperator.sunder(x);
      Tensor y = RnBiinvariantMean.INSTANCE.mean(points, weights);
      Chop._06.requireClose(x, y);
    }
  }

  @Test
  void testLagrangeProperty() {
    Random random = new Random();
    Distribution distribution = UniformDistribution.unit();
    Biinvariant biinvariant = Biinvariants.METRIC.ofSafe(RnGroup.INSTANCE);
    for (int n = 2; n < 6; ++n) {
      int length = n + 1 + random.nextInt(3);
      Tensor points = RandomVariate.of(distribution, length, n);
      Sedarim tensorUnaryOperator = biinvariant.coordinate(InversePowerVariogram.of(1), points);
      Chop._10.requireClose(Tensor.of(points.stream().map(tensorUnaryOperator::sunder)), IdentityMatrix.of(length));
    }
  }

  @Test
  void testQuantity() {
    Random random = new Random();
    Distribution distribution = UniformDistribution.of(Quantity.of(-1, "m"), Quantity.of(+1, "m"));
    Biinvariant biinvariant = Biinvariants.METRIC.ofSafe(RnGroup.INSTANCE);
    for (int n = 2; n < 6; ++n) {
      int length = n + 1 + random.nextInt(3);
      Tensor points = RandomVariate.of(distribution, length, n);
      Tensor x = RandomVariate.of(distribution, n);
      Sedarim tensorUnaryOperator = biinvariant.coordinate(InversePowerVariogram.of(1), points);
      Tensor weights = tensorUnaryOperator.sunder(x);
      Tensor y = RnBiinvariantMean.INSTANCE.mean(points, weights);
      Chop._06.requireClose(x, y);
    }
  }

  @Test
  void testAffineSimple() {
    Random random = new Random(1);
    BarycentricCoordinate barycentricCoordinate = AffineWrap.of(RnGroup.INSTANCE);
    for (int dim = 2; dim < 4; ++dim)
      for (int length = dim + 1; length < 8; ++length) {
        Distribution distribution = NormalDistribution.standard();
        Tensor sequence = RandomVariate.of(distribution, random, length, dim);
        Tensor mean = RandomVariate.of(distribution, random, dim);
        Tensor lhs = barycentricCoordinate.weights(sequence, mean);
        Tensor rhs = RnAffineCoordinate.INSTANCE.weights(sequence, mean);
        Chop._06.requireClose(lhs, rhs);
      }
  }

  @Test
  void testNullFail() {
    for (BarycentricCoordinate barycentricCoordinate : GbcHelper.barycentrics(RnGroup.INSTANCE))
      assertThrows(Exception.class, () -> barycentricCoordinate.weights(null, null));
  }

  @Test
  void testColinear() {
    int d = 2;
    int n = 5;
    for (BarycentricCoordinate barycentricCoordinate : GbcHelper.barycentrics(RnGroup.INSTANCE)) {
      Tensor sequence = RandomVariate.of(NormalDistribution.standard(), n, d);
      sequence.append(sequence.get(n - 1).multiply(RealScalar.of(5)));
      Tensor weights = barycentricCoordinate.weights(sequence, Array.zeros(d));
      assertEquals(sequence.length(), n + 1);
      AffineQ.require(weights, Chop._08);
    }
  }

  @Test
  void testSimple1() {
    Tensor matrix = HilbertMatrix.of(2, 3);
    assertEquals(RnGroup.INSTANCE.exp(matrix), matrix);
    assertEquals(RnGroup.INSTANCE.log(matrix), matrix);
  }

  @Test
  void testLinearPrecision() {
    Tensor sequence = RandomVariate.of(NormalDistribution.standard(), 10, 3);
    Tensor point = RandomVariate.of(NormalDistribution.standard(), 3);
    for (BarycentricCoordinate barycentricCoordinates : GbcHelper.barycentrics(RnGroup.INSTANCE)) {
      Tensor weights = barycentricCoordinates.weights(sequence, point);
      Chop._10.requireClose(weights.dot(sequence), point);
    }
  }

  @Test
  void testSimple2() {
    Tensor actual = RnGroup.INSTANCE.split(Tensors.vector(10, 1), Tensors.vector(11, 0), RealScalar.of(-1));
    ExactTensorQ.require(actual);
    assertEquals(Tensors.vector(9, 2), actual);
  }

  @Test
  void testSimple3() {
    GeodesicSpace geodesicSpace = RnGroup.INSTANCE;
    ScalarTensorFunction scalarTensorFunction = geodesicSpace.curve(UnitVector.of(3, 0), UnitVector.of(3, 1));
    assertEquals(scalarTensorFunction.apply(RealScalar.ZERO), UnitVector.of(3, 0));
    assertEquals(scalarTensorFunction.apply(RationalScalar.HALF), Tensors.vector(0.5, 0.5, 0));
  }

  @Test
  void testEndPoints() {
    Distribution distribution = NormalDistribution.standard();
    for (int index = 0; index < 10; ++index) {
      Tensor p = RandomVariate.of(distribution, 7);
      Tensor q = RandomVariate.of(distribution, 7);
      Chop._14.requireClose(p, RnGroup.INSTANCE.split(p, q, RealScalar.ZERO));
      Chop._14.requireClose(q, RnGroup.INSTANCE.split(p, q, RealScalar.ONE));
    }
  }

  @Test
  void testDeBoor() {
    Tensor knots = Tensors.vector(1, 2, 3, 4);
    Tensor control = Tensors.vector(9, 3, 4);
    DeBoor.of(RnGroup.INSTANCE, knots, control);
    assertThrows(Exception.class, () -> DeBoor.of(null, knots, control));
  }

  @Test
  void testSymmetric() {
    int d = 2;
    int n = 5;
    Tensor sequence = RandomVariate.of(NormalDistribution.standard(), n, d);
    Manifold manifold = RnGroup.INSTANCE;
    Biinvariant metricBiinvariant = Biinvariants.METRIC.ofSafe(manifold);
    Biinvariant harborBiinvariant = Biinvariants.HARBOR.ofSafe(manifold);
    for (Biinvariant biinvariant : new Biinvariant[] { metricBiinvariant, harborBiinvariant }) {
      Sedarim tensorUnaryOperator = biinvariant.distances(sequence);
      Tensor vardst = Tensor.of(sequence.stream().map(tensorUnaryOperator::sunder));
      SymmetricMatrixQ.require(vardst);
    }
    Biinvariant leveragesBiinvariant = Biinvariants.LEVERAGES.ofSafe(manifold);
    {
      Sedarim tensorUnaryOperator = leveragesBiinvariant.distances(sequence);
      Tensor vardst = Tensor.of(sequence.stream().map(tensorUnaryOperator::sunder));
      assertFalse(SymmetricMatrixQ.of(vardst));
    }
  }

  @Test
  void testFailNull() {
    assertThrows(Exception.class, () -> RnGroup.INSTANCE.element(null));
  }

  @Test
  void testMetric() {
    TensorMetric tensorMetric = RnGroup.INSTANCE;
    Scalar scalar = tensorMetric.distance(Tensors.vector(1, 2, 3), Tensors.vector(1 + 3, 2 + 4, 3));
    assertEquals(scalar, RealScalar.of(5));
    ExactScalarQ.require(scalar);
  }
  // public void testFailMatrix() {
  // try {
  // RnGroup.INSTANCE.element(IdentityMatrix.of(3));
  // fail();
  // } catch (Exception exception) {
  // // ---
  // }
  // }
}
