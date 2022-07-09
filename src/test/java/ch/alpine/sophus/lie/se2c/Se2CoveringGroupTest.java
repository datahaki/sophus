// code by jph
package ch.alpine.sophus.lie.se2c;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Random;
import java.util.function.BinaryOperator;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.dv.BarycentricCoordinate;
import ch.alpine.sophus.dv.Biinvariant;
import ch.alpine.sophus.dv.Biinvariants;
import ch.alpine.sophus.dv.GbcHelper;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.lie.se2.Se2Algebra;
import ch.alpine.sophus.math.api.TensorMapping;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.pow.Power;

class Se2CoveringGroupTest {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se2CoveringGroup.INSTANCE);
  private static final RandomSampleInterface RANDOM_SAMPLE_INTERFACE = //
      Se2CoveringRandomSample.uniform(UniformDistribution.of(Clips.absolute(10)));

  @Test
  void testSimple() {
    Se2CoveringGroupElement se2CoveringGroupElement = Se2CoveringGroup.INSTANCE.element(Tensors.vector(1, 2, 3));
    Tensor tensor = se2CoveringGroupElement.combine(Tensors.vector(0, 0, -3));
    assertEquals(tensor, Tensors.vector(1, 2, 0));
  }

  @Test
  void testConvergenceSe2() {
    Tensor x = Tensors.vector(0.1, 0.2, 0.05);
    Tensor y = Tensors.vector(0.02, -0.1, -0.04);
    Tensor mX = Se2CoveringGroup.INSTANCE.exp(x);
    Tensor mY = Se2CoveringGroup.INSTANCE.exp(y);
    Tensor res = Se2CoveringGroup.INSTANCE.log(Se2CoveringGroup.INSTANCE.element(mX).combine(mY));
    Scalar cmp = RealScalar.ONE;
    for (int degree = 1; degree < 6; ++degree) {
      BinaryOperator<Tensor> binaryOperator = Se2Algebra.INSTANCE.bch(degree);
      Tensor z = binaryOperator.apply(x, y);
      Scalar err = Vector2Norm.between(res, z);
      assertTrue(Scalars.lessThan(err, cmp));
      cmp = err;
    }
    Chop._08.requireZero(cmp);
  }

  @Test
  void testAdInv() {
    Random random = new Random();
    int n = 5 + random.nextInt(3);
    Tensor sequence = RandomSample.of(RANDOM_SAMPLE_INTERFACE, n);
    Tensor point = RandomSample.of(RANDOM_SAMPLE_INTERFACE);
    Tensor shift = RandomSample.of(RANDOM_SAMPLE_INTERFACE);
    for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift)) {
      Tensor all = tensorMapping.slash(sequence);
      Tensor one = tensorMapping.apply(point);
      for (BarycentricCoordinate barycentricCoordinate : GbcHelper.biinvariant(Se2CoveringGroup.INSTANCE)) {
        Tensor w1 = barycentricCoordinate.weights(sequence, point);
        Tensor w2 = barycentricCoordinate.weights(all, one);
        if (!Chop._03.isClose(w1, w2)) {
          System.out.println("---");
          System.out.println(w1);
          System.out.println(w2);
          fail();
        }
      }
      Biinvariant biinvariant = Biinvariants.HARBOR.ofSafe(Se2CoveringGroup.INSTANCE);
      for (int exp = 0; exp < 3; ++exp) {
        Sedarim gr1 = biinvariant.coordinate(Power.function(exp), sequence);
        Sedarim gr2 = biinvariant.coordinate(Power.function(exp), all);
        Tensor w1 = gr1.sunder(point);
        Tensor w2 = gr2.sunder(one);
        Chop._10.requireClose(w1, w2);
      }
    }
  }

  @Test
  void testLinearReproduction() {
    Random random = new Random();
    int n = 5 + random.nextInt(5);
    Tensor sequence = RandomSample.of(RANDOM_SAMPLE_INTERFACE, n);
    Biinvariant biinvariant = Biinvariants.HARBOR.ofSafe(Se2CoveringGroup.INSTANCE);
    Sedarim grCoordinate = biinvariant.coordinate(InversePowerVariogram.of(2), sequence);
    Tensor point = RandomSample.of(RANDOM_SAMPLE_INTERFACE);
    Tensor weights = grCoordinate.sunder(point);
    Tensor mean = Se2CoveringBiinvariantMean.INSTANCE.mean(sequence, weights);
    Chop._05.requireClose(point, mean);
  }

  @Test
  void testArticle() {
    Tensor tensor = Se2CoveringGroup.INSTANCE.split( //
        Tensors.vector(1, 2, 3), Tensors.vector(4, 5, 6), RealScalar.of(0.7));
    Chop._14.requireClose(tensor, Tensors.fromString("{4.483830852817113, 3.2143505344919467, 5.1}"));
  }

  @Test
  void testCenter() {
    // mathematica gives: {2.26033, -0.00147288, 0.981748}
    Tensor p = Tensors.vector(2.017191762967754, -0.08474511292102775, 0.9817477042468103);
    Tensor q = Tensors.vector(2.503476971090440, +0.08179934782700435, 0.9817477042468102);
    Scalar scalar = RealScalar.of(0.5);
    Tensor delta = new Se2CoveringGroupElement(p).inverse().combine(q);
    Tensor x = Se2CoveringGroup.INSTANCE.log(delta).multiply(scalar);
    x.get();
    // x= {0.20432112230000457, -0.1559021143001622, -5.551115123125783E-17}
    // mathematica gives
    // x= {0.204321, .......... -0.155902, ......... -5.55112*10^-17}
    // Tensor exp_x = Se2CoveringIntegrator.INSTANCE.spin(Tensors.vector(0, 0, 0), x);
    // exp_x == {0.20432112230000457, -0.1559021143001622, -5.551115123125783E-17}
    Tensor tensor = Se2CoveringGroup.INSTANCE.split(p, q, scalar);
    // {2.260334367029097, -0.0014728825470118057, 0.9817477042468103}
    Chop._14.requireClose(tensor, //
        Tensors.fromString("{2.260334367029097, -0.0014728825470118057, 0.9817477042468103}"));
  }

  @Test
  void testBiinvariantMean() {
    Distribution distribution = UniformDistribution.of(-3, 8);
    Distribution wd = UniformDistribution.unit();
    int success = 0;
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      Scalar w = RandomVariate.of(wd);
      Tensor mean = Se2CoveringBiinvariantMean.INSTANCE.mean(Tensors.of(p, q), Tensors.of(RealScalar.ONE.subtract(w), w));
      Tensor splt = Se2CoveringGroup.INSTANCE.split(p, q, w);
      if (Chop._12.isClose(mean, splt))
        ++success;
    }
    assertTrue(90 < success);
  }
}
