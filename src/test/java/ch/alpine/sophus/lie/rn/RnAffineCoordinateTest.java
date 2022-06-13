// code by jph
package ch.alpine.sophus.lie.rn;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.gbc.AffineWrap;
import ch.alpine.sophus.gbc.AveragingWeights;
import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.pdf.d.DiscreteUniformDistribution;
import ch.alpine.tensor.red.Mean;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Chop;

class RnAffineCoordinateTest {
  @Test
  void testMean() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 3; n < 10; ++n) {
      Tensor points = RandomVariate.of(distribution, n, 2);
      TensorUnaryOperator affineCoordinates = RnAffineCoordinate.of(points);
      Tensor weights = affineCoordinates.apply(Mean.of(points));
      Chop._10.requireClose(weights, AveragingWeights.of(n));
    }
  }

  @Test
  void testExact() {
    Distribution distribution = DiscreteUniformDistribution.of(-1000, 1000);
    for (int n = 3; n < 10; ++n) {
      Tensor points = RandomVariate.of(distribution, n, 2);
      TensorUnaryOperator affineCoordinates = RnAffineCoordinate.of(points);
      Tensor weights = affineCoordinates.apply(Mean.of(points));
      Chop._10.requireClose(weights, AveragingWeights.of(n));
      // ExactTensorQ.require(weights);
    }
  }

  @Test
  void testUnity() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 3; n < 10; ++n) {
      Tensor points = RandomVariate.of(distribution, n, n - 1);
      TensorUnaryOperator affineCoordinates = RnAffineCoordinate.of(points);
      Tensor weights = affineCoordinates.apply(Mean.of(points));
      Chop._10.requireClose(weights, AveragingWeights.of(n));
      Chop._10.requireClose(Tensor.of(points.stream().map(affineCoordinates)), IdentityMatrix.of(n));
    }
  }

  @Test
  void testPoints() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 5; n < 10; ++n) {
      Tensor p1 = RandomVariate.of(distribution, n, 2);
      TensorUnaryOperator affineCoordinates = RnAffineCoordinate.of(p1);
      Tensor p2 = Tensor.of(p1.stream().map(affineCoordinates)).dot(p1);
      Chop._08.requireClose(p1, p2);
    }
  }

  @Test
  void testWeights() {
    Distribution distribution = UniformDistribution.unit();
    BarycentricCoordinate barycentricCoordinate = AffineWrap.of(RnGroup.INSTANCE);
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

  @Test
  void testZeros() {
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

  @Test
  void testSmallN() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 1; n < 3; ++n) {
      Tensor points = RandomVariate.of(distribution, n, 2);
      TensorUnaryOperator affineCoordinates = RnAffineCoordinate.of(points);
      Tensor tensor = Tensor.of(points.stream().map(affineCoordinates));
      Chop._10.requireClose(tensor, IdentityMatrix.of(n));
    }
  }

  @Test
  void testSinglePoint() {
    Tensor sequence = Tensors.fromString("{{1, 2, 3}}");
    TensorUnaryOperator tensorUnaryOperator = RnAffineCoordinate.of(sequence);
    Tensor tensor = tensorUnaryOperator.apply(Tensors.vector(1, 2, 4));
    Chop._10.requireClose(Tensors.vector(1), tensor);
  }

  @Test
  void testVectorFail() {
    assertThrows(Exception.class, () -> RnAffineCoordinate.of(Tensors.vector(1, 2, 3, 4)));
  }

  @Test
  void testEmptyFail() {
    assertThrows(Exception.class, () -> RnAffineCoordinate.of(Tensors.empty()));
  }
}
