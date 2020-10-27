// code by jph
package ch.ethz.idsc.sophus.lie.he;

import java.io.IOException;

import ch.ethz.idsc.sophus.gbc.AffineWrap;
import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.HsCoordinates;
import ch.ethz.idsc.sophus.gbc.LeverageCoordinate;
import ch.ethz.idsc.sophus.gbc.MetricCoordinate;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.math.NormWeighting;
import ch.ethz.idsc.sophus.math.TensorMapping;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class HeManifoldTest extends TestCase {
  private static final BarycentricCoordinate AFFINE = AffineWrap.of(HeManifold.INSTANCE);
  public static final BarycentricCoordinate INSTANCE = HsCoordinates.wrap(HeManifold.INSTANCE, MetricCoordinate.of( //
      NormWeighting.of( //
          new HeTarget(RnNorm.INSTANCE, RealScalar.ONE), //
          InversePowerVariogram.of(1))));
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
        Tensor constant = ConstantArray.of(RationalScalar.of(1, length), length);
        Tensor center = HeBiinvariantMean.INSTANCE.mean(sequence, constant);
        Tensor weights = barycentricCoordinate.weights(sequence, center);
        Tolerance.CHOP.requireClose(weights, constant);
      }
  }
}
