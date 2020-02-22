// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.lie.BiinvariantMean;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
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

public class RnInverseDistanceCoordinateTest extends TestCase {
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = { //
      RnInverseDistanceCoordinate.INSTANCE, //
      RnInverseDistanceCoordinate.SQUARED };

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
    for (int n = 2; n < 5; ++n)
      for (int length = n + 1; length < 10; ++length) {
        Tensor points = RandomVariate.of(distribution, length, n);
        Tensor xya = RandomVariate.of(distribution, n);
        for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
          Tensor weights1 = barycentricCoordinate.weights(points, xya);
          Chop._10.requireClose(Total.ofVector(weights1), RealScalar.ONE);
          Tensor x_recreated = biinvariantMean.mean(points, weights1);
          Chop._06.requireClose(xya, x_recreated);
          Tensor shift = RandomVariate.of(distribution, n);
          RnGroupElement rnGroupElement = RnGroup.INSTANCE.element(shift);
          // invariant under left action
          Tensor weightsL = barycentricCoordinate.weights( //
              LIE_GROUP_OPS.left(points, shift), rnGroupElement.combine(xya));
          Tolerance.CHOP.requireClose(weights1, weightsL);
          // invariant under left action
          Tensor weightsR = barycentricCoordinate.weights( //
              LIE_GROUP_OPS.right(points, shift), RnGroup.INSTANCE.element(xya).combine(shift));
          Tolerance.CHOP.requireClose(weights1, weightsR);
          // invariant under inversion
          Tensor xyainv = RnGroup.INSTANCE.element(xya).inverse().toCoordinate();
          Tensor weightsI = barycentricCoordinate.weights(LIE_GROUP_OPS.invertAll(points), xyainv);
          Tolerance.CHOP.requireClose(weights1, weightsI);
        }
      }
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

  public void testNullFail() {
    try {
      RnInverseDistanceCoordinate.INSTANCE.weights(null, null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
