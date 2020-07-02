// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.GbcHelper;
import ch.ethz.idsc.sophus.gbc.HarborCoordinate;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
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
    for (int count = 0; count < 10; ++count) {
      Tensor sequence = Tensors.vector(i -> TestHelper.spawn_Se2C(), 8);
      Tensor point = TestHelper.spawn_Se2C();
      Tensor shift = TestHelper.spawn_Se2C();
      Tensor adSeq = LIE_GROUP_OPS.allConjugate(sequence, shift);
      Tensor adPnt = LIE_GROUP_OPS.conjugate(shift).apply(point);
      for (BarycentricCoordinate barycentricCoordinate : GbcHelper.biinvariant(Se2CoveringManifold.INSTANCE)) {
        Tensor w1 = barycentricCoordinate.weights(sequence, point);
        Tensor w2 = barycentricCoordinate.weights(adSeq, adPnt);
        if (!Chop._03.close(w1, w2)) {
          System.out.println("---");
          System.out.println(w1);
          System.out.println(w2);
          fail();
        }
      }
      for (int exp = 0; exp < 3; ++exp) {
        TensorUnaryOperator gr1 = HarborCoordinate.of(Se2CoveringManifold.INSTANCE, Power.function(exp), sequence);
        TensorUnaryOperator gr2 = HarborCoordinate.of(Se2CoveringManifold.INSTANCE, Power.function(exp), adSeq);
        Tensor w1 = gr1.apply(point);
        Tensor w2 = gr2.apply(adPnt);
        Chop._10.requireClose(w1, w2);
      }
    }
  }

  public void testLinearReproduction() {
    for (int length = 5; length < 10; ++length) {
      Tensor sequence = Tensors.vector(i -> TestHelper.spawn_Se2C(), length);
      TensorUnaryOperator grCoordinate = HarborCoordinate.of(Se2CoveringManifold.INSTANCE, InversePowerVariogram.of(2), sequence);
      for (int count = 0; count < 10; ++count) {
        Tensor point = TestHelper.spawn_Se2C();
        Tensor weights = grCoordinate.apply(point);
        Tensor mean = Se2CoveringBiinvariantMean.INSTANCE.mean(sequence, weights);
        Chop._07.requireClose(point, mean);
      }
    }
  }
}
