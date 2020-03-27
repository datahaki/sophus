// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.HsBiinvariantCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.BiinvariantMean;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.math.win.AffineCoordinate;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class RnInverseDistanceCoordinatesTest extends TestCase {
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = { //
      HsBarycentricCoordinate.linear(RnManifold.INSTANCE), //
      HsBarycentricCoordinate.smooth(RnManifold.INSTANCE), //
      HsBiinvariantCoordinate.linear(RnManifold.INSTANCE), //
      HsBiinvariantCoordinate.smooth(RnManifold.INSTANCE) };

  public void testSimple() {
    Distribution distribution = NormalDistribution.standard();
    for (int n = 2; n < 5; ++n)
      for (int length = n + 1; length < 10; ++length) {
        Tensor points = RandomVariate.of(distribution, length, n);
        Tensor mean = RandomVariate.of(distribution, n);
        for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
          Tensor weights = barycentricCoordinate.weights(points, mean);
          Tensor result = RnBiinvariantMean.INSTANCE.mean(points, weights);
          Chop._10.requireClose(mean, result);
        }
      }
  }

  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(RnGroup.INSTANCE);

  public void testRandom() {
    Distribution distribution = UniformDistribution.unit();
    BiinvariantMean biinvariantMean = RnBiinvariantMean.INSTANCE;
    int fails = 0;
    for (int n = 2; n < 5; ++n)
      for (int length = n + 1; length < 10; ++length)
        try {
          Tensor points = RandomVariate.of(distribution, length, n);
          Tensor xya = RandomVariate.of(distribution, n);
          for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
            Tensor weights1 = barycentricCoordinate.weights(points, xya);
            Chop._10.requireClose(Total.ofVector(weights1), RealScalar.ONE);
            Tensor x_recreated = biinvariantMean.mean(points, weights1);
            Chop._06.requireClose(xya, x_recreated);
            Tensor shift = RandomVariate.of(distribution, n);
            { // invariant under left action
              Tensor weightsL = barycentricCoordinate.weights( //
                  LIE_GROUP_OPS.allLeft(points, shift), LIE_GROUP_OPS.combine(shift, xya));
              Tolerance.CHOP.requireClose(weights1, weightsL);
            }
            { // invariant under left action
              Tensor weightsR = barycentricCoordinate.weights( //
                  LIE_GROUP_OPS.allRight(points, shift), LIE_GROUP_OPS.combine(xya, shift));
              Tolerance.CHOP.requireClose(weights1, weightsR);
            }
            { // invariant under inversion
              Tensor weightsI = barycentricCoordinate.weights( //
                  LIE_GROUP_OPS.allInvert(points), LIE_GROUP_OPS.invert(xya));
              Tolerance.CHOP.requireClose(weights1, weightsI);
            }
          }
        } catch (Exception exception) {
          ++fails;
        }
    assertTrue(fails < 3);
  }

  public void testLinearReproduction() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 2; n < 6; ++n)
      for (int length = n + 1; length < 10; ++length) {
        Tensor points = RandomVariate.of(distribution, length, n);
        Tensor x = RandomVariate.of(distribution, n);
        Tensor weights = OriginalInverseDistanceCoordinates.INSTANCE.weights(points, x);
        Tensor y = RnBiinvariantMean.INSTANCE.mean(points, weights);
        Chop._10.requireClose(x, y);
      }
  }

  public void testLagrangeProperty() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 2; n < 6; ++n)
      for (int length = n + 1; length < 10; ++length) {
        Tensor points = RandomVariate.of(distribution, length, n);
        TensorUnaryOperator tensorUnaryOperator = x -> OriginalInverseDistanceCoordinates.INSTANCE.weights(points, x);
        Chop._10.requireClose(Tensor.of(points.stream().map(tensorUnaryOperator)), IdentityMatrix.of(length));
      }
  }

  public void testQuantity() {
    Distribution distribution = UniformDistribution.of(Quantity.of(-1, "m"), Quantity.of(+1, "m"));
    for (int n = 2; n < 6; ++n)
      for (int length = n + 1; length < 10; ++length) {
        Tensor points = RandomVariate.of(distribution, length, n);
        Tensor x = RandomVariate.of(distribution, n);
        Tensor weights = OriginalInverseDistanceCoordinates.INSTANCE.weights(points, x);
        Tensor y = RnBiinvariantMean.INSTANCE.mean(points, weights);
        Chop._10.requireClose(x, y);
      }
  }

  public void testAffineSimple() {
    ProjectedCoordinate projectedCoordinate = HsBiinvariantCoordinate.affine(RnManifold.INSTANCE);
    for (int dim = 2; dim < 4; ++dim)
      for (int length = dim + 1; length < 10; ++length) {
        Distribution distribution = NormalDistribution.standard();
        Tensor sequence = RandomVariate.of(distribution, length, dim);
        Tensor mean = RandomVariate.of(distribution, dim);
        Tensor lhs = projectedCoordinate.weights(sequence, mean);
        Tensor rhs = AffineCoordinate.INSTANCE.weights(sequence, mean);
        Chop._10.requireClose(lhs, rhs);
      }
  }

  public void testNullFail() {
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      try {
        barycentricCoordinate.weights(null, null);
        fail();
      } catch (Exception exception) {
        // ---
      }
  }
}
