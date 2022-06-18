// code by jph
package ch.alpine.sophus.hs.sn;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.sophus.gbc.AveragingWeights;
import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.gbc.GbcHelper;
import ch.alpine.sophus.hs.r2.ArcTan2D;
import ch.alpine.sophus.lie.so.SoRandomSample;
import ch.alpine.sophus.lie.so3.Rodrigues;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.lie.r2.AngleVector;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.red.Mean;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.tri.ArcCos;

class SnManifoldTest {
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = //
      GbcHelper.barycentrics(SnManifold.INSTANCE);

  private static Tensor randomCloud(Tensor mean, int n, Random random) {
    Distribution distribution = NormalDistribution.of(0, 0.2);
    return Tensor.of(RandomVariate.of(distribution, random, n, mean.length()).stream().map(mean::add).map(Vector2Norm.NORMALIZE));
  }

  @Test
  void testLinearReproduction() {
    Random random = new Random(3);
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int d = 2; d < 5; ++d) {
        Tensor mean = UnitVector.of(d, 0);
        int n = d + 1 + random.nextInt(3);
        Tensor sequence = randomCloud(mean, n, random);
        Tensor weights = barycentricCoordinate.weights(sequence, mean);
        VectorQ.requireLength(weights, n);
        AffineQ.require(weights, Chop._08);
        Tensor evaluate = new MeanDefect(sequence, weights, SnManifold.INSTANCE.exponential(mean)).tangent();
        Chop._06.requireAllZero(evaluate);
        Chop._06.requireClose(mean, SnManifold.INSTANCE.biinvariantMean(Chop._14).mean(sequence, weights));
      }
  }

  @Test
  void testLagrangeProperty() {
    Random random = new Random(3);
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int d = 2; d < 4; ++d) {
        int n = d + 1 + random.nextInt(3);
        Tensor sequence = randomCloud(UnitVector.of(d, 0), n, random);
        int count = random.nextInt(sequence.length());
        Tensor mean = sequence.get(count);
        Tensor weights = barycentricCoordinate.weights(sequence, mean);
        VectorQ.requireLength(weights, n);
        AffineQ.require(weights, Chop._08);
        Chop._06.requireClose(weights, UnitVector.of(n, count));
        Tensor evaluate = new MeanDefect(sequence, weights, SnManifold.INSTANCE.exponential(mean)).tangent();
        Chop._06.requireAllZero(evaluate);
        Chop._03.requireClose(mean, SnManifold.INSTANCE.biinvariantMean(Chop._06).mean(sequence, weights));
      }
  }

  @Test
  void testBiinvariance() {
    Random random = new Random(3);
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int d = 2; d < 5; ++d) {
        Tensor mean = UnitVector.of(d, 0);
        RandomSampleInterface randomSampleInterface = SoRandomSample.of(d);
        int n = d + 1 + random.nextInt(3);
        Tensor sequence = randomCloud(mean, n, random);
        Tensor weights = barycentricCoordinate.weights(sequence, mean);
        VectorQ.requireLength(weights, n);
        AffineQ.require(weights, Chop._08);
        {
          Tensor evaluate = new MeanDefect(sequence, weights, SnManifold.INSTANCE.exponential(mean)).tangent();
          Chop._08.requireAllZero(evaluate);
        }
        // ---
        {
          Tensor matrix = RandomSample.of(randomSampleInterface);
          Tensor mean2 = matrix.dot(mean);
          Tensor shifted = Tensor.of(sequence.stream().map(matrix::dot));
          Tensor evaluate = new MeanDefect(shifted, weights, SnManifold.INSTANCE.exponential(mean2)).tangent();
          Chop._10.requireAllZero(evaluate);
          Tensor weights2 = barycentricCoordinate.weights(shifted, mean2);
          Chop._04.requireClose(weights, weights2); // 1e-6 does not always work
        }
      }
  }

  private static Scalar _check(Tensor p, Tensor q) {
    return ArcCos.FUNCTION.apply((Scalar) p.dot(q)); // complex number if |p.q| > 1
  }

  @Test
  void testSimple() {
    Chop._12.requireClose(SnManifold.INSTANCE.distance(UnitVector.of(3, 0), UnitVector.of(3, 1)), Pi.HALF);
    Chop._12.requireClose(SnManifold.INSTANCE.distance(UnitVector.of(3, 0), UnitVector.of(3, 2)), Pi.HALF);
    Chop._12.requireClose(SnManifold.INSTANCE.distance(UnitVector.of(3, 1), UnitVector.of(3, 2)), Pi.HALF);
  }

  @Test
  void testDirect() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 100; ++count) {
      Tensor p = Vector2Norm.NORMALIZE.apply(RandomVariate.of(distribution, 3));
      Tensor q = Vector2Norm.NORMALIZE.apply(RandomVariate.of(distribution, 3));
      Scalar a1 = SnManifold.INSTANCE.distance(p, q);
      Scalar a2 = _check(p, q);
      Chop._12.requireClose(a1, a2);
      Scalar norm = Vector2Norm.of(new SnExponential(p).log(q));
      Tolerance.CHOP.requireClose(norm, a1);
    }
  }

  @Test
  void testSpecific() {
    Distribution distribution = NormalDistribution.of(0, 0.2);
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
      Tensor rotation = Rodrigues.vectorExp(RandomVariate.of(distribution, 3));
      Tensor mean = rotation.dot(Vector2Norm.NORMALIZE.apply(Tensors.vector(1, 1, 1)));
      Tensor sequence = Tensor.of(IdentityMatrix.of(3).stream().map(rotation::dot));
      Chop._08.requireClose(sequence, Transpose.of(rotation));
      Tensor weights = barycentricCoordinate.weights(sequence, mean);
      Chop._12.requireClose(weights, NormalizeTotal.FUNCTION.apply(Tensors.vector(1, 1, 1)));
      Tensor evaluate = new MeanDefect(sequence, weights, SnManifold.INSTANCE.exponential(mean)).tangent();
      Chop._12.requireAllZero(evaluate);
      Chop._05.requireClose(mean, SnManifold.INSTANCE.biinvariantMean(Chop._06).mean(sequence, weights));
    }
  }

  @Test
  void testS1Linear() {
    Distribution distribution = UniformDistribution.of(0, Math.PI);
    for (int n = 2; n < 10; ++n) {
      Tensor angles = RandomVariate.of(distribution, n);
      Tensor sequence = angles.map(AngleVector::of);
      Tensor weights = AveragingWeights.of(n);
      Tensor point = SnManifold.INSTANCE.biinvariantMean(Chop._06).mean(sequence, weights);
      Chop._05.requireClose(ArcTan2D.of(point), Mean.of(angles));
    }
  }

  @Test
  void testMemberQFail() {
    assertThrows(Exception.class, () -> SnManifold.INSTANCE.distance(Tensors.vector(1, 0), Tensors.vector(1, 1)));
    assertThrows(Exception.class, () -> SnManifold.INSTANCE.distance(Tensors.vector(1, 1), Tensors.vector(1, 0)));
  }
}
