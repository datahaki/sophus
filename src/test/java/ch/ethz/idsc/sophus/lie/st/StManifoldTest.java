// code by jph
package ch.ethz.idsc.sophus.lie.st;

import java.io.IOException;

import ch.ethz.idsc.sophus.gbc.AffineWrap;
import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.LeverageCoordinate;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.math.TensorMapping;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class StManifoldTest extends TestCase {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(StGroup.INSTANCE);
  private static final BarycentricCoordinate AFFINE = AffineWrap.of(StManifold.INSTANCE);
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = { //
      LeverageCoordinate.slow(StManifold.INSTANCE, InversePowerVariogram.of(1)), //
      LeverageCoordinate.slow(StManifold.INSTANCE, InversePowerVariogram.of(2)), //
      AFFINE };

  public void testSimple() {
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int n = 1; n < 3; ++n)
        for (int length = n + 2; length < n + 8; ++length) {
          int fn = n;
          Tensor sequence = Tensors.vector(i -> TestHelper.spawn_St(fn), length);
          Tensor mean1 = TestHelper.spawn_St(n);
          Tensor weights = barycentricCoordinate.weights(sequence, mean1);
          Tensor mean2 = StBiinvariantMean.INSTANCE.mean(sequence, weights);
          Chop._06.requireClose(mean1, mean2);
          // ---
          Tensor shift = TestHelper.spawn_St(n);
          for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift))
            Chop._03.requireClose(weights, //
                barycentricCoordinate.weights(tensorMapping.slash(sequence), tensorMapping.apply(mean1)));
        }
  }

  public void testAffineBiinvariant() throws ClassNotFoundException, IOException {
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int n = 1; n < 3; ++n)
        for (int length = n + 2; length < n + 8; ++length) {
          barycentricCoordinate = Serialization.copy(barycentricCoordinate);
          int fn = n;
          Tensor sequence = Tensors.vector(i -> TestHelper.spawn_St(fn), length);
          Tensor mean1 = TestHelper.spawn_St(n);
          Tensor weights = barycentricCoordinate.weights(sequence, mean1);
          Tensor mean2 = StBiinvariantMean.INSTANCE.mean(sequence, weights);
          Chop._08.requireClose(mean1, mean2); // linear reproduction
          // ---
          Tensor shift = TestHelper.spawn_St(n);
          for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift))
            Chop._05.requireClose(weights, barycentricCoordinate.weights( //
                tensorMapping.slash(sequence), tensorMapping.apply(mean1)));
        }
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
