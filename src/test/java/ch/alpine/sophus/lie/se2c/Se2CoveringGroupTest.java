// code by jph
package ch.alpine.sophus.lie.se2c;

import java.util.Random;

import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.gbc.GbcHelper;
import ch.alpine.sophus.gbc.HarborCoordinate;
import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.math.TensorMapping;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Power;
import junit.framework.TestCase;

public class Se2CoveringGroupTest extends TestCase {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se2CoveringGroup.INSTANCE);

  public void testSimple() {
    Se2CoveringGroupElement se2CoveringGroupElement = Se2CoveringGroup.INSTANCE.element(Tensors.vector(1, 2, 3));
    Tensor tensor = se2CoveringGroupElement.combine(Tensors.vector(0, 0, -3));
    assertEquals(tensor, Tensors.vector(1, 2, 0));
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
