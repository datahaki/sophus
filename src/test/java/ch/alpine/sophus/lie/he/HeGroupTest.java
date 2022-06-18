// code by jph
package ch.alpine.sophus.lie.he;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.TensorMapping;
import ch.alpine.sophus.dv.AffineWrap;
import ch.alpine.sophus.dv.AveragingWeights;
import ch.alpine.sophus.dv.BarycentricCoordinate;
import ch.alpine.sophus.dv.HsCoordinates;
import ch.alpine.sophus.dv.MetricCoordinate;
import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.math.NormWeighting;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;

class HeGroupTest {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(HeGroup.INSTANCE);
  private static final RandomSampleInterface RSI = new HeRandomSample(2, UniformDistribution.of(Clips.absolute(10)));

  @Test
  void testSimple1() {
    Tensor p = Tensors.fromString("{1, 2, 3, 4, 5, 6, 7}");
    Tensor q = Tensors.fromString("{-1, 6, 2, -3, -2, 1, -4}");
    Tensor actual = HeGroup.INSTANCE.split(p, q, RationalScalar.HALF);
    ExactTensorQ.require(actual);
    Tensor expect = Tensors.fromString("{0, 4, 5/2, 1/2, 3/2, 7/2, 21/8}");
    assertEquals(actual, expect);
  }

  @Test
  void testExpLog() {
    for (int count = 0; count < 10; ++count) {
      Tensor inp = RandomSample.of(RSI);
      Tensor xyz = HeGroup.INSTANCE.exp(inp);
      Tensor uvw = HeGroup.INSTANCE.log(xyz);
      Tolerance.CHOP.requireClose(inp, uvw);
    }
  }

  @Test
  void testLogExp() {
    for (int count = 0; count < 10; ++count) {
      Tensor inp = RandomSample.of(RSI);
      Tensor uvw = HeGroup.INSTANCE.log(inp);
      Tensor xyz = HeGroup.INSTANCE.exp(uvw);
      Tolerance.CHOP.requireClose(inp, xyz);
    }
  }

  @Test
  void testAdLog() {
    for (int count = 0; count < 10; ++count) {
      Tensor g = RandomSample.of(RSI);
      Tensor m = RandomSample.of(RSI);
      Tensor lhs = HeGroup.INSTANCE.log(LIE_GROUP_OPS.conjugation(g).apply(m));
      Tensor rhs = HeGroup.INSTANCE.element(g).adjoint(HeGroup.INSTANCE.log(m));
      Tolerance.CHOP.requireClose(lhs, rhs);
    }
  }

  private static final BarycentricCoordinate AFFINE = AffineWrap.of(HeGroup.INSTANCE);
  public static final BarycentricCoordinate INSTANCE = new HsCoordinates(new HsDesign(HeGroup.INSTANCE), //
      new MetricCoordinate( //
          NormWeighting.of(new HeTarget(Vector2Norm::of, RealScalar.ONE), //
              InversePowerVariogram.of(1))));
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = { //
      // LeveragesCoordinate.slow(HeManifold.INSTANCE, InversePowerVariogram.of(1)), //
      // LeveragesCoordinate.slow(HeManifold.INSTANCE, InversePowerVariogram.of(2)), //
      AFFINE, //
      INSTANCE //
  };

  @Test
  void testSimple() {
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int n = 1; n < 3; ++n)
        for (int length = 2 * n + 2; length < 2 * n + 10; ++length) {
          RandomSampleInterface rsi = new HeRandomSample(n, UniformDistribution.of(Clips.absolute(10)));
          Tensor sequence = RandomSample.of(rsi, length);
          Tensor mean1 = RandomSample.of(rsi);
          Tensor weights = barycentricCoordinate.weights(sequence, mean1);
          Tensor mean2 = HeBiinvariantMean.INSTANCE.mean(sequence, weights);
          Chop._05.requireClose(mean1, mean2);
          // ---
          Tensor shift = RandomSample.of(rsi);
          for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift))
            Chop._05.requireClose(weights, //
                barycentricCoordinate.weights(tensorMapping.slash(sequence), tensorMapping.apply(mean1)));
        }
  }

  @Test
  void testAffineBiinvariant() throws ClassNotFoundException, IOException {
    Random random = new Random(1);
    BarycentricCoordinate barycentricCoordinate = Serialization.copy(AFFINE);
    for (int n = 1; n < 3; ++n)
      for (int length = 2 * n + 2; length < 2 * n + 10; ++length) {
        RandomSampleInterface rsi = new HeRandomSample(n, UniformDistribution.of(Clips.absolute(10)));
        Tensor sequence = RandomSample.of(rsi, random, length);
        Tensor mean1 = RandomSample.of(rsi, random);
        Tensor weights = barycentricCoordinate.weights(sequence, mean1);
        Tensor mean2 = HeBiinvariantMean.INSTANCE.mean(sequence, weights);
        Chop._06.requireClose(mean1, mean2);
        // ---
        Tensor shift = RandomSample.of(rsi, random);
        for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift))
          Chop._04.requireClose(weights, //
              barycentricCoordinate.weights(tensorMapping.slash(sequence), tensorMapping.apply(mean1)));
      }
  }

  @Test
  void testAffineCenter() throws ClassNotFoundException, IOException {
    BarycentricCoordinate barycentricCoordinate = Serialization.copy(AFFINE);
    for (int n = 1; n < 3; ++n)
      for (int length = 2 * n + 2; length < 2 * n + 10; ++length) {
        RandomSampleInterface rsi = new HeRandomSample(n, UniformDistribution.of(Clips.absolute(10)));
        Tensor sequence = RandomSample.of(rsi, length);
        Tensor constant = AveragingWeights.of(length);
        Tensor center = HeBiinvariantMean.INSTANCE.mean(sequence, constant);
        Tensor weights = barycentricCoordinate.weights(sequence, center);
        Tolerance.CHOP.requireClose(weights, constant);
      }
  }

  @Test
  void testNullFail() {
    assertThrows(Exception.class, () -> HeGroup.INSTANCE.element(null));
  }
}
