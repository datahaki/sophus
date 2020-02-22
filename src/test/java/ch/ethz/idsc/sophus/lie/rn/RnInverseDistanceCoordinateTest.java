// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.lie.BiinvariantMean;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
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
    for (int dim = 2; dim < 5; ++dim) {
      Tensor points = RandomVariate.of(distribution, 10, dim);
      // TensorUnaryOperator tensorUnaryOperator = //
      // Serialization.copy(RnInverseDistanceCoordinates.INSTANCE.of(points));
      for (int count = 0; count < 10; ++count) {
        Tensor mean = RandomVariate.of(distribution, dim);
        for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
          {
            Tensor weights = barycentricCoordinate.weights(points, mean);
            Tensor result = RnBiinvariantMean.INSTANCE.mean(points, weights);
            Chop._10.requireClose(mean, result);
          }
          {
          }
        }
      }
    }
  }

  public void testRandom() {
    Tensor NEUTRAL = Tensors.vector(0, 0, 0);
    Distribution distribution = UniformDistribution.unit();
    BiinvariantMean biinvariantMean = RnBiinvariantMean.INSTANCE;
    for (int n = 4; n < 10; ++n) {
      Tensor points = RandomVariate.of(distribution, n, 3);
      Tensor xya = RandomVariate.of(distribution, 3);
      for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
        Tensor weights1 = barycentricCoordinate.weights(points, xya);
        Chop._10.requireClose(Total.ofVector(weights1), RealScalar.ONE);
        Tensor x_recreated = biinvariantMean.mean(points, weights1);
        Chop._06.requireClose(xya, x_recreated);
        Tensor seqinv = Tensor.of(points.stream() //
            .map(RnGroup.INSTANCE::element) //
            .map(RnGroupElement::inverse) //
            .map(ge -> ge.combine(NEUTRAL)));
        Tensor xyainv = RnGroup.INSTANCE.element(xya).inverse().combine(NEUTRAL);
        Tensor weights2 = barycentricCoordinate.weights(seqinv, xyainv);
        Chop._10.requireClose(weights1, weights2);
      }
    }
  }
  // public void testLinearReproduction() {
  // Distribution distribution = UniformDistribution.unit();
  // for (int d = 2; d < 6; ++d)
  // for (int n = d + 1; n < 10; ++n) {
  // Tensor points = RandomVariate.of(distribution, n, d);
  // Tensor x = RandomVariate.of(distribution, d);
  // TensorUnaryOperator idfCoordinates = RnInverseDistanceCoordinatesOLD.of(Norm._2::ofVector, points);
  // LieInverseDistanceCoordinates lidc = new LieInverseDistanceCoordinates(RnGroup.INSTANCE, p -> p, InverseNorm.of(Norm._2::ofVector));
  //// TensorUnaryOperator rn_Coordinates = .of(points);
  // Tensor w1 = idfCoordinates.apply(x);
  // Tensor w2 = lidc.idc(points, x);
  // Chop._06.requireClose(w1, w2);
  // }
  // }
  //
  // public void testLagrangeProperty() {
  // Distribution distribution = UniformDistribution.unit();
  // for (int d = 2; d < 6; ++d)
  // for (int n = d + 1; n < 10; ++n) {
  // Tensor points = RandomVariate.of(distribution, n, d);
  // TensorUnaryOperator idfCoordinates = RnInverseDistanceCoordinatesOLD.of(Norm._2::ofVector, points);
  //// TensorUnaryOperator rn_Coordinates = new LieInverseDistanceCoordinates(RnGroup.INSTANCE, p -> p, InverseNorm.of(Norm._2::ofVector)).of(points);
  // LieInverseDistanceCoordinates lidc = new LieInverseDistanceCoordinates(RnGroup.INSTANCE, p -> p, InverseNorm.of(Norm._2::ofVector));
  // Chop._06.requireClose(Tensor.of(points.stream().map(idfCoordinates)), IdentityMatrix.of(n));
  // Chop._06.requireClose(Tensor.of(points.stream().map(r->lidc.idc(points, r))), IdentityMatrix.of(n));
  // }
  // }

  public void testLinearReproduction() {
    Distribution distribution = UniformDistribution.unit();
    for (int d = 2; d < 6; ++d)
      for (int n = d + 1; n < 10; ++n) {
        Tensor points = RandomVariate.of(distribution, n, d);
        Tensor x = RandomVariate.of(distribution, d);
        // TensorUnaryOperator tensorUnaryOperator = //
        // Serialization.copy(of(RnNorm.INSTANCE, points));
        Tensor weights = OriginalInverseDistanceCoordinates.INSTANCE.weights(points, x);
        Tensor y = RnBiinvariantMean.INSTANCE.mean(points, weights);
        Chop._10.requireClose(x, y);
      }
  }

  public void testLagrangeProperty() {
    Distribution distribution = UniformDistribution.unit();
    for (int d = 2; d < 6; ++d)
      for (int n = d + 1; n < 10; ++n) {
        Tensor points = RandomVariate.of(distribution, n, d);
        TensorUnaryOperator tensorUnaryOperator = x -> OriginalInverseDistanceCoordinates.INSTANCE.weights(points, x);
        Chop._10.requireClose(Tensor.of(points.stream().map(tensorUnaryOperator)), IdentityMatrix.of(n));
      }
  }

  public void testQuantity() {
    Distribution distribution = UniformDistribution.of(Quantity.of(-1, "m"), Quantity.of(+1, "m"));
    for (int d = 2; d < 6; ++d)
      for (int n = d + 1; n < 10; ++n) {
        Tensor points = RandomVariate.of(distribution, n, d);
        Tensor x = RandomVariate.of(distribution, d);
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
