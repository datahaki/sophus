// code by jph
package ch.ethz.idsc.sophus.lie.he;

import java.io.IOException;

import ch.ethz.idsc.sophus.gbc.AffineCoordinate;
import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.LeverageCoordinate;
import ch.ethz.idsc.sophus.gbc.MetricCoordinate;
import ch.ethz.idsc.sophus.krg.InversePowerVariogram;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.math.InverseNorm;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class HeManifoldTest extends TestCase {
  private static final BarycentricCoordinate AFFINE = AffineCoordinate.of(HeManifold.INSTANCE);
  public static final BarycentricCoordinate INSTANCE = MetricCoordinate.custom( //
      HeManifold.INSTANCE, InverseNorm.of(new HeTarget(RnNorm.INSTANCE, RealScalar.ONE)));
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = { //
      LeverageCoordinate.slow(HeManifold.INSTANCE, InversePowerVariogram.of(1)), //
      LeverageCoordinate.slow(HeManifold.INSTANCE, InversePowerVariogram.of(2)), //
      AFFINE, //
      INSTANCE //
  };
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(HeGroup.INSTANCE);

  public void testSimple() {
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int n = 1; n < 3; ++n)
        for (int length = 2 * n + 2; length < 2 * n + 10; ++length) {
          int fn = n;
          Tensor sequence = Tensors.vector(i -> TestHelper.spawn_He(fn), length);
          Tensor mean1 = TestHelper.spawn_He(n);
          Tensor weights1 = barycentricCoordinate.weights(sequence, mean1);
          Tensor mean2 = HeBiinvariantMean.INSTANCE.mean(sequence, weights1);
          Chop._08.requireClose(mean1, mean2);
          // ---
          Tensor shift = TestHelper.spawn_He(n);
          { // invariant under left action
            Tensor weightsL = barycentricCoordinate.weights(LIE_GROUP_OPS.allLeft(sequence, shift), LIE_GROUP_OPS.combine(shift, mean1));
            Chop._08.requireClose(weights1, weightsL);
          }
          { // invariant under right action
            Tensor weightsR = barycentricCoordinate.weights(LIE_GROUP_OPS.allRight(sequence, shift), LIE_GROUP_OPS.combine(mean1, shift));
            Chop._08.requireClose(weights1, weightsR);
          }
          { // invariant under inversion
            Tensor weightsI = barycentricCoordinate.weights( //
                LIE_GROUP_OPS.allInvert(sequence), //
                LIE_GROUP_OPS.invert(mean1));
            Chop._08.requireClose(weights1, weightsI);
          }
        }
  }

  public void testAffineBiinvariant() throws ClassNotFoundException, IOException {
    BarycentricCoordinate barycentricCoordinate = Serialization.copy(AFFINE);
    for (int n = 1; n < 3; ++n)
      for (int length = 2 * n + 2; length < 2 * n + 10; ++length) {
        int fn = n;
        Tensor sequence = Tensors.vector(i -> TestHelper.spawn_He(fn), length);
        Tensor mean1 = TestHelper.spawn_He(n);
        Tensor weights1 = barycentricCoordinate.weights(sequence, mean1);
        Tensor mean2 = HeBiinvariantMean.INSTANCE.mean(sequence, weights1);
        Chop._08.requireClose(mean1, mean2);
        // ---
        Tensor shift = TestHelper.spawn_He(n);
        { // invariant under left action
          Tensor weightsL = barycentricCoordinate.weights(LIE_GROUP_OPS.allLeft(sequence, shift), LIE_GROUP_OPS.combine(shift, mean1));
          Chop._08.requireClose(weights1, weightsL);
        }
        { // invariant under right action
          Tensor weightsR = barycentricCoordinate.weights(LIE_GROUP_OPS.allRight(sequence, shift), LIE_GROUP_OPS.combine(mean1, shift));
          Chop._08.requireClose(weights1, weightsR);
        }
        { // invariant under inversion
          Tensor weightsI = barycentricCoordinate.weights( //
              LIE_GROUP_OPS.allInvert(sequence), //
              LIE_GROUP_OPS.invert(mean1));
          Chop._08.requireClose(weights1, weightsI);
        }
      }
  }

  public void testAffineCenter() throws ClassNotFoundException, IOException {
    BarycentricCoordinate barycentricCoordinate = Serialization.copy(AFFINE);
    for (int n = 1; n < 3; ++n)
      for (int length = 2 * n + 2; length < 2 * n + 10; ++length) {
        int fn = n;
        Tensor sequence = Tensors.vector(i -> TestHelper.spawn_He(fn), length);
        Tensor constant = ConstantArray.of(RationalScalar.of(1, length), length);
        Tensor center = HeBiinvariantMean.INSTANCE.mean(sequence, constant);
        Tensor weights = barycentricCoordinate.weights(sequence, center);
        Tolerance.CHOP.requireClose(weights, constant);
      }
  }
}
