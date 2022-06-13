// code by jph
package ch.alpine.sophus.hs.hn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class HnExponentialTest {
  @Test
  void testExp() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 4; ++d) {
      Tensor xn = RandomVariate.of(distribution, d);
      Tensor x = HnWeierstrassCoordinate.toPoint(xn);
      HnExponential hnExponential = new HnExponential(x);
      Tensor v = HnWeierstrassCoordinate.toTangent(xn, RandomVariate.of(distribution, d));
      new THnMemberQ(x).require(v);
      Tensor y = hnExponential.exp(v);
      HnMemberQ.INSTANCE.require(y);
      Scalar dxy = HnMetric.INSTANCE.distance(x, y);
      Tolerance.CHOP.requireClose(dxy, HnVectorNorm.of(v));
    }
  }

  @Test
  void testExpZero() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 4; ++d) {
      Tensor xn = RandomVariate.of(distribution, d);
      Tensor x = HnWeierstrassCoordinate.toPoint(xn);
      HnExponential hnExponential = new HnExponential(x);
      Tensor v = HnWeierstrassCoordinate.toTangent(xn, Array.zeros(d));
      new THnMemberQ(x).require(v);
      assertEquals(v, Array.zeros(d + 1));
      Tensor y = hnExponential.exp(v);
      HnMemberQ.INSTANCE.require(y);
      Tolerance.CHOP.requireClose(x, y);
      Scalar dxy = HnMetric.INSTANCE.distance(x, y);
      Chop._04.requireClose(dxy, HnVectorNorm.of(v));
    }
  }

  @Test
  void testLog() {
    Random random = new Random(1);
    Distribution distribution = NormalDistribution.of(0, 5);
    for (int d = 1; d < 4; ++d)
      for (int count = 0; count < 100; ++count) {
        Tensor x = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, random, d));
        Tensor y = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, random, d));
        Tensor z = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, random, d));
        HnExponential hnExponential = new HnExponential(x);
        Tensor vy = hnExponential.log(y);
        Tensor vz = hnExponential.log(z);
        THnMemberQ tHnMemberQ = new THnMemberQ(x);
        tHnMemberQ.require(vy);
        tHnMemberQ.require(vz);
        Tensor v = vy.add(vz);
        tHnMemberQ.require(v);
        Tensor a = hnExponential.exp(v);
        HnMemberQ.INSTANCE.require(a);
        Scalar dxy = HnMetric.INSTANCE.distance(x, y);
        Scalar vn1 = HnVectorNorm.of(vy);
        Chop._06.requireClose(dxy, vn1);
      }
  }

  @Test
  void testLogZero() {
    Random random = new Random(3);
    Distribution distribution = NormalDistribution.of(0, 10);
    for (int d = 1; d < 4; ++d) {
      Tensor x = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, random, d));
      HnExponential hnExponential = new HnExponential(x);
      Tensor v = hnExponential.log(x);
      new THnMemberQ(x).require(v);
      Scalar dxy = HnMetric.INSTANCE.distance(x, x);
      Scalar vn1 = HnVectorNorm.of(v);
      Chop._06.requireClose(dxy, vn1);
    }
  }

  @Test
  void testSpecific() {
    Tensor x = Tensors.vector(2, Math.sqrt(5));
    Tensor y = Tensors.vector(3, Math.sqrt(10));
    HnMemberQ.INSTANCE.require(x);
    HnMemberQ.INSTANCE.require(y);
    HnExponential hnExponential = new HnExponential(x);
    Tensor v = hnExponential.log(y);
    Tolerance.CHOP.requireClose(v, Tensors.vector(0.8381028390566728, 0.7496219681065144));
    Scalar vnorm = HnVectorNorm.of(v);
    Tolerance.CHOP.requireClose(vnorm, RealScalar.of(0.37481098405325747));
    Tensor yr = hnExponential.exp(v);
    Tolerance.CHOP.requireClose(yr, Tensors.vector(3, 3.1622776601683817));
    // Scalar lf = LBilinearForm.between(x, y);
    // System.out.println(lf);
    // System.out.println(v);
  }
}
