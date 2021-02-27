// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.gbc.AffineWrap;
import ch.ethz.idsc.sophus.gbc.AveragingWeights;
import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.pdf.DiscreteUniformDistribution;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Mean;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class RnAffineCoordinateTest extends TestCase {
  public void testMean() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 3; n < 10; ++n) {
      Tensor points = RandomVariate.of(distribution, n, 2);
      TensorUnaryOperator affineCoordinates = RnAffineCoordinate.of(points);
      Tensor weights = affineCoordinates.apply(Mean.of(points));
      Chop._10.requireClose(weights, AveragingWeights.of(n));
    }
  }

  public void testExact() {
    Distribution distribution = DiscreteUniformDistribution.of(-1000, 1000);
    for (int n = 3; n < 10; ++n) {
      Tensor points = RandomVariate.of(distribution, n, 2);
      TensorUnaryOperator affineCoordinates = RnAffineCoordinate.of(points);
      Tensor weights = affineCoordinates.apply(Mean.of(points));
      Chop._10.requireClose(weights, AveragingWeights.of(n));
      // ExactTensorQ.require(weights);
    }
  }

  public void testUnity() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 3; n < 10; ++n) {
      Tensor points = RandomVariate.of(distribution, n, n - 1);
      TensorUnaryOperator affineCoordinates = RnAffineCoordinate.of(points);
      Tensor weights = affineCoordinates.apply(Mean.of(points));
      Chop._10.requireClose(weights, AveragingWeights.of(n));
      Chop._10.requireClose(Tensor.of(points.stream().map(affineCoordinates)), IdentityMatrix.of(n));
    }
  }

  public void testPoints() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 5; n < 10; ++n) {
      Tensor p1 = RandomVariate.of(distribution, n, 2);
      TensorUnaryOperator affineCoordinates = RnAffineCoordinate.of(p1);
      Tensor p2 = Tensor.of(p1.stream().map(affineCoordinates)).dot(p1);
      Chop._08.requireClose(p1, p2);
    }
  }

  public void testWeights() {
    Distribution distribution = UniformDistribution.unit();
    BarycentricCoordinate barycentricCoordinate = AffineWrap.of(RnManifold.INSTANCE);
    for (int d = 2; d < 5; ++d)
      for (int n = 5; n < 10; ++n) {
        Tensor sequence = RandomVariate.of(distribution, n, d);
        TensorUnaryOperator affineCoordinates = RnAffineCoordinate.of(sequence);
        Tensor point = RandomVariate.of(distribution, d);
        Tensor w1 = affineCoordinates.apply(point);
        VectorQ.requireLength(w1, n);
        Chop._06.requireClose(Total.ofVector(w1), RealScalar.ONE);
        Tensor w2 = barycentricCoordinate.weights(sequence, point);
        Chop._06.requireClose(w1, w2);
      }
  }

  public void testZeros() {
    Distribution distribution = UniformDistribution.unit();
    for (int d = 2; d < 5; ++d)
      for (int n = 5; n < 10; ++n) {
        Tensor points = Array.zeros(n, d);
        TensorUnaryOperator affineCoordinates = RnAffineCoordinate.of(points);
        Tensor x = RandomVariate.of(distribution, d);
        Tensor weights = affineCoordinates.apply(x);
        VectorQ.requireLength(weights, n);
        Chop._06.requireClose(Total.ofVector(weights), RealScalar.ONE);
      }
  }

  public void testSmallN() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 1; n < 3; ++n) {
      Tensor points = RandomVariate.of(distribution, n, 2);
      TensorUnaryOperator affineCoordinates = RnAffineCoordinate.of(points);
      Tensor tensor = Tensor.of(points.stream().map(affineCoordinates));
      Chop._10.requireClose(tensor, IdentityMatrix.of(n));
    }
  }

  public void testSinglePoint() {
    Tensor sequence = Tensors.fromString("{{1, 2, 3}}");
    TensorUnaryOperator tensorUnaryOperator = RnAffineCoordinate.of(sequence);
    Tensor tensor = tensorUnaryOperator.apply(Tensors.vector(1, 2, 4));
    Chop._10.requireClose(Tensors.vector(1), tensor);
  }

  public void testVectorFail() {
    AssertFail.of(() -> RnAffineCoordinate.of(Tensors.vector(1, 2, 3, 4)));
  }

  public void testEmptyFail() {
    AssertFail.of(() -> RnAffineCoordinate.of(Tensors.empty()));
  }
}
