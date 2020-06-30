// code by jph
package ch.ethz.idsc.sophus.lie.st;

import java.io.IOException;

import ch.ethz.idsc.sophus.gbc.AffineCoordinate;
import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.LeverageCoordinate;
import ch.ethz.idsc.sophus.krg.InversePowerVariogram;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class StManifoldTest extends TestCase {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(StGroup.INSTANCE);
  private static final BarycentricCoordinate AFFINE = AffineCoordinate.of(StManifold.INSTANCE);
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = { //
      LeverageCoordinate.of(StManifold.INSTANCE, InversePowerVariogram.of(1)), //
      LeverageCoordinate.of(StManifold.INSTANCE, InversePowerVariogram.of(2)), //
      AFFINE };

  public void testSimple() {
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int n = 1; n < 3; ++n)
        for (int length = n + 2; length < n + 8; ++length) {
          int fn = n;
          Tensor sequence = Tensors.vector(i -> TestHelper.spawn_St(fn), length);
          Tensor mean1 = TestHelper.spawn_St(n);
          Tensor weights1 = barycentricCoordinate.weights(sequence, mean1);
          Tensor mean2 = StBiinvariantMean.INSTANCE.mean(sequence, weights1);
          Chop._06.requireClose(mean1, mean2);
          // ---
          Tensor shift = TestHelper.spawn_St(n);
          // invariant under left action
          {
            Tensor weightsL = barycentricCoordinate.weights(LIE_GROUP_OPS.allLeft(sequence, shift), LIE_GROUP_OPS.combine(shift, mean1));
            Chop._06.requireClose(weights1, weightsL);
          }
          // invariant under right action
          {
            Tensor weightsR = barycentricCoordinate.weights(LIE_GROUP_OPS.allRight(sequence, shift), LIE_GROUP_OPS.combine(mean1, shift));
            Chop._06.requireClose(weights1, weightsR);
          }
          // invariant under inversion
          {
            Tensor weightsI = barycentricCoordinate.weights( //
                LIE_GROUP_OPS.allInvert(sequence), LIE_GROUP_OPS.invert(mean1));
            Chop._06.requireClose(weights1, weightsI);
          }
        }
  }

  public void testAffineBiinvariant() {
    int fails = 0;
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int n = 1; n < 3; ++n)
        for (int length = n + 2; length < n + 8; ++length)
          try {
            barycentricCoordinate = Serialization.copy(barycentricCoordinate);
            int fn = n;
            Tensor sequence = Tensors.vector(i -> TestHelper.spawn_St(fn), length);
            Tensor mean1 = TestHelper.spawn_St(n);
            Tensor weights1 = barycentricCoordinate.weights(sequence, mean1);
            Tensor mean2 = StBiinvariantMean.INSTANCE.mean(sequence, weights1);
            Chop._08.requireClose(mean1, mean2); // linear reproduction
            // ---
            Tensor shift = TestHelper.spawn_St(n);
            { // invariant under left action
              Tensor weightsL = barycentricCoordinate.weights(LIE_GROUP_OPS.allLeft(sequence, shift), LIE_GROUP_OPS.combine(shift, mean1));
              Chop._08.requireClose(weights1, weightsL);
            }
            { // invariant under right action
              Tensor weightsR = barycentricCoordinate.weights(LIE_GROUP_OPS.allRight(sequence, shift), LIE_GROUP_OPS.combine(mean1, shift));
              Chop._10.requireClose(weights1, weightsR);
            }
            { // invariant under inversion
              Tensor weightsI = barycentricCoordinate.weights( //
                  LIE_GROUP_OPS.allInvert(sequence), LIE_GROUP_OPS.invert(mean1));
              Chop._06.requireClose(weights1, weightsI);
            }
          } catch (Exception exception) {
            ++fails;
          }
    assertTrue(fails < 3);
  }

  public void testAffineCenter() throws ClassNotFoundException, IOException {
    BarycentricCoordinate barycentricCoordinate = Serialization.copy(AFFINE);
    for (int n = 1; n < 3; ++n)
      for (int length = n + 2; length < n + 8; ++length) {
        int fn = n;
        Tensor sequence = Tensors.vector(i -> TestHelper.spawn_St(fn), length);
        Tensor constant = ConstantArray.of(RationalScalar.of(1, length), length);
        Tensor center = StBiinvariantMean.INSTANCE.mean(sequence, constant);
        Tensor weights = barycentricCoordinate.weights(sequence, center);
        Tolerance.CHOP.requireClose(weights, constant);
      }
  }
}
