// code by jph
package ch.alpine.sophus.lie.se2c;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Random;
import java.util.function.BinaryOperator;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.TensorMapping;
import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.gbc.GbcHelper;
import ch.alpine.sophus.gbc.HarborCoordinate;
import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.lie.se2.Se2Algebra;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.pow.Power;

public class Se2CoveringGroupTest {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se2CoveringGroup.INSTANCE);
  private static final RandomSampleInterface RANDOM_SAMPLE_INTERFACE = //
      Se2CoveringRandomSample.uniform(UniformDistribution.of(Clips.absolute(10)));

  @Test
  public void testSimple() {
    Se2CoveringGroupElement se2CoveringGroupElement = Se2CoveringGroup.INSTANCE.element(Tensors.vector(1, 2, 3));
    Tensor tensor = se2CoveringGroupElement.combine(Tensors.vector(0, 0, -3));
    assertEquals(tensor, Tensors.vector(1, 2, 0));
  }

  @Test
  public void testConvergenceSe2() {
    Tensor x = Tensors.vector(0.1, 0.2, 0.05);
    Tensor y = Tensors.vector(0.02, -0.1, -0.04);
    Tensor mX = Se2CoveringExponential.INSTANCE.exp(x);
    Tensor mY = Se2CoveringExponential.INSTANCE.exp(y);
    Tensor res = Se2CoveringExponential.INSTANCE.log(Se2CoveringGroup.INSTANCE.element(mX).combine(mY));
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
  public void testAdInv() {
    Random random = new Random();
    int n = 5 + random.nextInt(3);
    Tensor sequence = RandomSample.of(RANDOM_SAMPLE_INTERFACE, n);
    Tensor point = RandomSample.of(RANDOM_SAMPLE_INTERFACE);
    Tensor shift = RandomSample.of(RANDOM_SAMPLE_INTERFACE);
    for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift)) {
      Tensor all = tensorMapping.slash(sequence);
      Tensor one = tensorMapping.apply(point);
      for (BarycentricCoordinate barycentricCoordinate : GbcHelper.biinvariant(Se2CoveringManifold.INSTANCE)) {
        Tensor w1 = barycentricCoordinate.weights(sequence, point);
        Tensor w2 = barycentricCoordinate.weights(all, one);
        if (!Chop._03.isClose(w1, w2)) {
          System.out.println("---");
          System.out.println(w1);
          System.out.println(w2);
          fail();
        }
      }
      for (int exp = 0; exp < 3; ++exp) {
        TensorUnaryOperator gr1 = HarborCoordinate.of(Se2CoveringManifold.INSTANCE, Power.function(exp), sequence);
        TensorUnaryOperator gr2 = HarborCoordinate.of(Se2CoveringManifold.INSTANCE, Power.function(exp), all);
        Tensor w1 = gr1.apply(point);
        Tensor w2 = gr2.apply(one);
        Chop._10.requireClose(w1, w2);
      }
    }
  }

  @Test
  public void testLinearReproduction() {
    Random random = new Random();
    int n = 5 + random.nextInt(5);
    Tensor sequence = RandomSample.of(RANDOM_SAMPLE_INTERFACE, n);
    TensorUnaryOperator grCoordinate = HarborCoordinate.of(Se2CoveringManifold.INSTANCE, InversePowerVariogram.of(2), sequence);
    Tensor point = RandomSample.of(RANDOM_SAMPLE_INTERFACE);
    Tensor weights = grCoordinate.apply(point);
    Tensor mean = Se2CoveringBiinvariantMean.INSTANCE.mean(sequence, weights);
    Chop._05.requireClose(point, mean);
  }
}
