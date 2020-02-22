// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.lie.BiinvariantMean;
import ch.ethz.idsc.sophus.lie.LieGroupTests;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
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
          // invariant under inversion
          Tensor seqinv = LieGroupTests.invert(RnGroup.INSTANCE, points);
          Tensor xyainv = RnGroup.INSTANCE.element(xya).inverse().toCoordinate();
          Tensor weights2 = barycentricCoordinate.weights(seqinv, xyainv);
          Chop._10.requireClose(weights1, weights2);
          // invariant under left action
          Tensor lft = RandomVariate.of(distribution, n);
          RnGroupElement rnGroupElement = RnGroup.INSTANCE.element(lft);
          Tensor misc = Tensor.of(points.stream().map(rnGroupElement::combine));
          Tensor weights3 = barycentricCoordinate.weights(misc, rnGroupElement.combine(xya));
          Chop._10.requireClose(weights1, weights3);
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
