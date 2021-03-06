// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import java.util.Random;

import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.GbcHelper;
import ch.ethz.idsc.sophus.gbc.HarborCoordinate;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.math.TensorMapping;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Power;
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
