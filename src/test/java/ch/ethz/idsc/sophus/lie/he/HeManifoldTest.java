// code by jph
package ch.ethz.idsc.sophus.lie.he;

import java.io.IOException;

import ch.ethz.idsc.sophus.gbc.AbsoluteCoordinate;
import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.ProjectedCoordinate;
import ch.ethz.idsc.sophus.gbc.RelativeCoordinate;
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
  private static final ProjectedCoordinate AFFINE = RelativeCoordinate.affine(HeManifold.INSTANCE);
  public static final ProjectedCoordinate INSTANCE = AbsoluteCoordinate.custom( //
      HeManifold.INSTANCE, InverseNorm.of(new HeTarget(RnNorm.INSTANCE, RealScalar.ONE)));
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = { //
      RelativeCoordinate.of(HeManifold.INSTANCE, InversePowerVariogram.of(1)), //
      RelativeCoordinate.of(HeManifold.INSTANCE, InversePowerVariogram.of(2)), //
      AFFINE, //
      INSTANCE //
  };
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(HeGroup.INSTANCE);

  public void testSimple() {
    int fails = 0;
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int n = 1; n < 3; ++n)
        for (int length = 2 * n + 2; length < 2 * n + 10; ++length)
          try {
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
              Chop._10.requireClose(weights1, weightsL);
              // System.out.println("WL=" + weightsL.map(Round._3));
            }
            { // invariant under right action
              Tensor weightsR = barycentricCoordinate.weights(LIE_GROUP_OPS.allRight(sequence, shift), LIE_GROUP_OPS.combine(mean1, shift));
              // System.out.println("WR=" + weightsR.map(Round._3));
              Chop._10.requireClose(weights1, weightsR);
            }
            { // invariant under inversion
              Tensor weightsI = barycentricCoordinate.weights( //
                  LIE_GROUP_OPS.allInvert(sequence), //
                  LIE_GROUP_OPS.invert(mean1));
              // System.out.println("WI=" + weightsI.map(Round._3));
              Chop._10.requireClose(weights1, weightsI);
            }
          } catch (Exception exception) {
            ++fails;
          }
    assertTrue(fails < 3);
  }

  public void testAffineBiinvariant() throws ClassNotFoundException, IOException {
    int fails = 0;
    BarycentricCoordinate barycentricCoordinate = Serialization.copy(AFFINE);
    for (int n = 1; n < 3; ++n)
      for (int length = 2 * n + 2; length < 2 * n + 10; ++length)
        try {
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
        } catch (Exception exception) {
          ++fails;
        }
    assertTrue(fails < 3);
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
