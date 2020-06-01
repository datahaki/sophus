// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.gbc.GrCoordinate;
import ch.ethz.idsc.sophus.gbc.ProjectedCoordinate;
import ch.ethz.idsc.sophus.gbc.RelativeCoordinate;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.sca.Chop;
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
      {
        ProjectedCoordinate projectedCoordinate = RelativeCoordinate.smooth(Se2CoveringManifold.INSTANCE);
        Tensor w1 = projectedCoordinate.weights(sequence, point);
        Tensor w2 = projectedCoordinate.weights(adSeq, adPnt);
        Chop._10.requireClose(w1, w2);
      }
      {
        GrCoordinate gr1 = new GrCoordinate(Se2CoveringManifold.INSTANCE, sequence);
        GrCoordinate gr2 = new GrCoordinate(Se2CoveringManifold.INSTANCE, adSeq);
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
      {
        ProjectedCoordinate projectedCoordinate = RelativeCoordinate.smooth(Se2CoveringManifold.INSTANCE);
        Tensor w1 = projectedCoordinate.weights(sequence, point);
        // System.out.println(w1);
      }
      {
        GrCoordinate gr1 = new GrCoordinate(Se2CoveringManifold.INSTANCE, sequence);
        Tensor w1 = gr1.apply(point);
        // System.out.println(w1);
      }
    }
  }
}
