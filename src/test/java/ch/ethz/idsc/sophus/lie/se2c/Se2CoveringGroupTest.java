// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.GbcHelper;
import ch.ethz.idsc.sophus.gbc.GrCoordinate;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.ConstantArray;
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
      for (BarycentricCoordinate barycentricCoordinate : GbcHelper.relatives(Se2CoveringManifold.INSTANCE)) {
        Tensor w1 = barycentricCoordinate.weights(sequence, point);
        Tensor w2 = barycentricCoordinate.weights(adSeq, adPnt);
        Chop._10.requireClose(w1, w2);
      }
      for (int exp = 0; exp < 3; ++exp) {
        GrCoordinate gr1 = new GrCoordinate(Se2CoveringManifold.INSTANCE, Power.function(exp), sequence);
        GrCoordinate gr2 = new GrCoordinate(Se2CoveringManifold.INSTANCE, Power.function(exp), adSeq);
        Tensor w1 = gr1.apply(point);
        Tensor w2 = gr2.apply(adPnt);
        Chop._10.requireClose(w1, w2);
      }
    }
  }

  public void testMean() {
    for (int count = 5; count < 10; ++count) {
      Tensor sequence = Tensors.vector(i -> TestHelper.spawn_Se2C(), count);
      Tensor point = //
          Se2CoveringBiinvariantMean.INSTANCE.mean(sequence, ConstantArray.of(RationalScalar.of(1, count), count));
      // System.out.println("---");
      for (BarycentricCoordinate barycentricCoordinate : GbcHelper.relatives(Se2CoveringManifold.INSTANCE)) {
        Tensor w1 = barycentricCoordinate.weights(sequence, point);
        // System.out.println(w1);
      }
      {
        GrCoordinate gr1 = new GrCoordinate(Se2CoveringManifold.INSTANCE, Power.function(2), sequence);
        Tensor w1 = gr1.apply(point);
        // System.out.println(w1);
      }
    }
  }
}
