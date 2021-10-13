// code by jph
package ch.alpine.sophus.lie.se2c;

import java.util.Random;
import java.util.function.BinaryOperator;

import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.gbc.GbcHelper;
import ch.alpine.sophus.gbc.HarborCoordinate;
import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.math.TensorMapping;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.lie.ad.BakerCampbellHausdorff;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.N;
import ch.alpine.tensor.sca.Power;
import junit.framework.TestCase;

public class Se2CoveringGroupTest extends TestCase {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se2CoveringGroup.INSTANCE);

  public void testSimple() {
    Se2CoveringGroupElement se2CoveringGroupElement = Se2CoveringGroup.INSTANCE.element(Tensors.vector(1, 2, 3));
    Tensor tensor = se2CoveringGroupElement.combine(Tensors.vector(0, 0, -3));
    assertEquals(tensor, Tensors.vector(1, 2, 0));
  }

  private static final Scalar P1 = RealScalar.ONE;
  private static final Scalar N1 = RealScalar.ONE.negate();

  /** @return ad tensor of 3-dimensional se(2) */
  public static Tensor se2() {
    Tensor ad = Array.zeros(3, 3, 3);
    ad.set(N1, 1, 2, 0);
    ad.set(P1, 1, 0, 2);
    ad.set(N1, 0, 1, 2);
    ad.set(P1, 0, 2, 1);
    return ad;
  }

  public void testConvergenceSe2() {
    Tensor x = Tensors.vector(0.1, 0.2, 0.05);
    Tensor y = Tensors.vector(0.02, -0.1, -0.04);
    Tensor mX = Se2CoveringExponential.INSTANCE.exp(x);
    Tensor mY = Se2CoveringExponential.INSTANCE.exp(y);
    Tensor res = Se2CoveringExponential.INSTANCE.log(Se2CoveringGroup.INSTANCE.element(mX).combine(mY));
    Tensor ad = N.DOUBLE.of(se2());
    Scalar cmp = RealScalar.ONE;
    for (int degree = 1; degree < 6; ++degree) {
      BinaryOperator<Tensor> binaryOperator = BakerCampbellHausdorff.of(ad, degree);
      Tensor z = binaryOperator.apply(x, y);
      Scalar err = Vector2Norm.between(res, z);
      assertTrue(Scalars.lessThan(err, cmp));
      cmp = err;
    }
    Chop._08.requireZero(cmp);
  }

  public void testAdInv() {
    Random random = new Random();
    int n = 5 + random.nextInt(3);
    Tensor sequence = Tensors.vector(i -> TestHelper.spawn_Se2C(), n);
    Tensor point = TestHelper.spawn_Se2C();
    Tensor shift = TestHelper.spawn_Se2C();
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

  public void testLinearReproduction() {
    Random random = new Random();
    int n = 5 + random.nextInt(5);
    Tensor sequence = Tensors.vector(i -> TestHelper.spawn_Se2C(), n);
    TensorUnaryOperator grCoordinate = HarborCoordinate.of(Se2CoveringManifold.INSTANCE, InversePowerVariogram.of(2), sequence);
    Tensor point = TestHelper.spawn_Se2C();
    Tensor weights = grCoordinate.apply(point);
    Tensor mean = Se2CoveringBiinvariantMean.INSTANCE.mean(sequence, weights);
    Chop._05.requireClose(point, mean);
  }
}
