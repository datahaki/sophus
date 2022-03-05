// code by jph
package ch.alpine.sophus.lie.he;

import java.io.IOException;

import ch.alpine.sophus.api.TensorMapping;
import ch.alpine.sophus.gbc.AffineWrap;
import ch.alpine.sophus.gbc.AveragingWeights;
import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.gbc.HsCoordinates;
import ch.alpine.sophus.gbc.MetricCoordinate;
import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.math.NormWeighting;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class HeManifoldTest extends TestCase {
  private static final BarycentricCoordinate AFFINE = AffineWrap.of(HeManifold.INSTANCE);
  public static final BarycentricCoordinate INSTANCE = HsCoordinates.wrap(HeManifold.INSTANCE, new MetricCoordinate( //
      NormWeighting.of( //
          new HeTarget(Vector2Norm::of, RealScalar.ONE), //
          InversePowerVariogram.of(1))));
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = { //
      // LeveragesCoordinate.slow(HeManifold.INSTANCE, InversePowerVariogram.of(1)), //
      // LeveragesCoordinate.slow(HeManifold.INSTANCE, InversePowerVariogram.of(2)), //
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
          Tensor weights = barycentricCoordinate.weights(sequence, mean1);
          Tensor mean2 = HeBiinvariantMean.INSTANCE.mean(sequence, weights);
          Chop._05.requireClose(mean1, mean2);
          // ---
          Tensor shift = TestHelper.spawn_He(n);
          for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift))
            Chop._05.requireClose(weights, //
                barycentricCoordinate.weights(tensorMapping.slash(sequence), tensorMapping.apply(mean1)));
        }
  }

  public void testAffineBiinvariant() throws ClassNotFoundException, IOException {
    BarycentricCoordinate barycentricCoordinate = Serialization.copy(AFFINE);
    for (int n = 1; n < 3; ++n)
      for (int length = 2 * n + 2; length < 2 * n + 10; ++length) {
        int fn = n;
        Tensor sequence = Tensors.vector(i -> TestHelper.spawn_He(fn), length);
        Tensor mean1 = TestHelper.spawn_He(n);
        Tensor weights = barycentricCoordinate.weights(sequence, mean1);
        Tensor mean2 = HeBiinvariantMean.INSTANCE.mean(sequence, weights);
        Chop._08.requireClose(mean1, mean2);
        // ---
        Tensor shift = TestHelper.spawn_He(n);
        for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift))
          Chop._04.requireClose(weights, //
              barycentricCoordinate.weights(tensorMapping.slash(sequence), tensorMapping.apply(mean1)));
      }
  }

  public void testAffineCenter() throws ClassNotFoundException, IOException {
    BarycentricCoordinate barycentricCoordinate = Serialization.copy(AFFINE);
    for (int n = 1; n < 3; ++n)
      for (int length = 2 * n + 2; length < 2 * n + 10; ++length) {
        int fn = n;
        Tensor sequence = Tensors.vector(i -> TestHelper.spawn_He(fn), length);
        Tensor constant = AveragingWeights.of(length);
        Tensor center = HeBiinvariantMean.INSTANCE.mean(sequence, constant);
        Tensor weights = barycentricCoordinate.weights(sequence, center);
        Tolerance.CHOP.requireClose(weights, constant);
      }
  }
}
