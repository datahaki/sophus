// code by jph
package ch.alpine.sophus.hs.hn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.dv.Biinvariant;
import ch.alpine.sophus.dv.Biinvariants;
import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.sophus.lie.sopq.TSopqProject;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.sophus.ref.d1.BSpline2CurveSubdivision;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.chq.FiniteTensorQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.TrapezoidalDistribution;
import ch.alpine.tensor.red.Nest;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Imag;
import ch.alpine.tensor.sca.Sign;
import ch.alpine.tensor.sca.pow.Sqrt;

class HnManifoldTest {
  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    GeodesicSpace geodesicSpace = Serialization.copy(HnManifold.INSTANCE);
    Distribution distribution = NormalDistribution.of(0, 10);
    for (int d = 1; d < 4; ++d) {
      Tensor p = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor midpoint = geodesicSpace.midpoint(p, q);
      HnMemberQ.INSTANCE.require(midpoint);
    }
  }

  @Test
  void testSubdiv() {
    BSpline2CurveSubdivision bSpline2CurveSubdivision = new BSpline2CurveSubdivision(HnManifold.INSTANCE);
    Distribution distribution = NormalDistribution.of(0, 10);
    for (int d = 1; d < 4; ++d) {
      Tensor p = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor r = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor result = Nest.of(bSpline2CurveSubdivision::cyclic, Tensors.of(p, q, r), 3);
      assertTrue(FiniteTensorQ.of(result));
    }
  }

  @Test
  void testFlip() {
    Distribution distribution = NormalDistribution.of(0, 1);
    for (int d = 1; d < 4; ++d) {
      Tensor p = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      HnExponential exp_p = new HnExponential(p);
      Tensor f1 = exp_p.exp(exp_p.log(q).negate());
      Tensor f2 = exp_p.flip(q);
      Chop._05.requireClose(f1, f2);
      Tensor f3 = HnManifold.INSTANCE.flip(p, q);
      Chop._05.requireClose(f1, f3);
    }
  }

  @Test
  void testZero() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 5; ++d) {
      Tensor x = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Scalar dxy = HnManifold.INSTANCE.distance(x, x);
      Chop._06.requireZero(dxy);
      assertTrue(Scalars.isZero(Imag.FUNCTION.apply(dxy)));
    }
  }

  @Test
  void testPositive() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 5; ++d) {
      Tensor x = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor y = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Scalar dxy = HnManifold.INSTANCE.distance(x, y);
      Sign.requirePositiveOrZero(dxy);
    }
  }

  @Test
  void testLBLinForm() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 5; ++d) {
      Tensor xn = RandomVariate.of(distribution, d);
      Tensor v = HnWeierstrassCoordinate.toTangent(xn, RandomVariate.of(distribution, d));
      assertEquals(v.length(), d + 1);
      Scalar vn1 = LBilinearForm.normSquared(v);
      Sign.requirePositiveOrZero(vn1);
    }
  }

  @Test
  void testConsistent() {
    Distribution distribution = NormalDistribution.standard();
    for (int d = 1; d < 5; ++d) {
      Tensor p = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
      Scalar distance = HnManifold.INSTANCE.distance(p, q);
      Tensor v = HnManifold.INSTANCE.exponential(p).log(q);
      new THnMemberQ(p).require(v);
      Scalar vn1 = Sqrt.FUNCTION.apply(LBilinearForm.normSquared(v));
      Chop._10.requireClose(distance, vn1);
      Scalar vn2 = HnVectorNorm.of(v);
      Chop._10.requireClose(vn2, vn1);
    }
  }

  @Test
  void testCoordinateBiinvariant() throws ClassNotFoundException, IOException {
    Random random = new Random(40);
    Distribution distribution = TrapezoidalDistribution.of(-2, -1, 1, 2);
    HomogeneousSpace manifold = HnManifold.INSTANCE;
    Biinvariant biinvariant = Serialization.copy(Biinvariants.METRIC.of(manifold));
    for (int d = 1; d < 5; ++d) {
      int n = d + 1;
      ScalarUnaryOperator variogram = InversePowerVariogram.of(2);
      Tensor sequence = //
          Tensors.vector(i -> HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, random, n - 1)), d + 3);
      Tensor point = HnWeierstrassCoordinate.toPoint(Array.zeros(d));
      Sedarim tensorUnaryOperator = biinvariant.coordinate(variogram, sequence);
      Tensor w1 = tensorUnaryOperator.sunder(point);
      Tensor mean = manifold.biinvariantMean(Chop._08).mean(sequence, w1);
      Chop._06.requireClose(mean, point);
      Tensor x = RandomVariate.of(NormalDistribution.standard(), random, n, n);
      x = new TSopqProject(d, 1).apply(x);
      Tensor sopq = MatrixExp.of(x);
      Tensor seq_l = Tensor.of(sequence.stream().map(sopq::dot));
      Tensor pnt_l = sopq.dot(point);
      Tensor w2 = biinvariant.coordinate(variogram, seq_l).sunder(pnt_l);
      Tensor m2 = manifold.biinvariantMean(Chop._08).mean(seq_l, w2);
      Chop._06.requireClose(m2, pnt_l);
      Chop._06.requireClose(w1, w2);
    }
  }

  @Test
  void testLagrangeBiinvariant() {
    Random random = new Random(40);
    Distribution distribution = TrapezoidalDistribution.of(-2, -1, 1, 2);
    HomogeneousSpace manifold = HnManifold.INSTANCE;
    Biinvariant biinvariant = Biinvariants.METRIC.of(manifold);
    for (int d = 1; d < 5; ++d) {
      int n = d + 1;
      ScalarUnaryOperator variogram = InversePowerVariogram.of(2);
      Tensor sequence = //
          Tensors.vector(i -> HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, random, n - 1)), d + 3);
      Tensor point = HnWeierstrassCoordinate.toPoint(Array.zeros(d));
      Sedarim tensorUnaryOperator = biinvariant.lagrainate(variogram, sequence);
      Tensor w1 = tensorUnaryOperator.sunder(point);
      Tensor mean = HnManifold.INSTANCE.biinvariantMean(Chop._08).mean(sequence, w1);
      Chop._06.requireClose(mean, point);
      Tensor x = RandomVariate.of(NormalDistribution.standard(), random, n, n);
      x = new TSopqProject(d, 1).apply(x);
      Tensor sopq = MatrixExp.of(x);
      Tensor seq_l = Tensor.of(sequence.stream().map(sopq::dot));
      Tensor pnt_l = sopq.dot(point);
      Tensor w2 = biinvariant.lagrainate(variogram, seq_l).sunder(pnt_l);
      Tensor m2 = HnManifold.INSTANCE.biinvariantMean(Chop._08).mean(seq_l, w2);
      Chop._06.requireClose(m2, pnt_l);
      Chop._06.requireClose(w1, w2);
    }
  }
}
