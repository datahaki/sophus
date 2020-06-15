// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.IOException;

import ch.ethz.idsc.sophus.gbc.AbsoluteCoordinate;
import ch.ethz.idsc.sophus.gbc.AffineCoordinate;
import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.HsMahalanobisDeprecated.Norm;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringBiinvariantMean;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringGroup;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import junit.framework.TestCase;

public class HsMahalanobisDeprecatedTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    LieGroupOps lieGroupOps = new LieGroupOps(Se2CoveringGroup.INSTANCE);
    HsMahalanobisDeprecated mahalanobis = new HsMahalanobisDeprecated(Se2CoveringManifold.INSTANCE, Se2CoveringBiinvariantMean.INSTANCE);
    int n = 10;
    Tensor sequence = RandomVariate.of(UniformDistribution.of(-2, 2), n, 3);
    Tensor weights = ConstantArray.of(RationalScalar.of(1, n), n);
    Tensor mean = Se2CoveringBiinvariantMean.INSTANCE.mean(sequence, weights);
    Tensor invert = Se2CoveringGroup.INSTANCE.element(mean).inverse().toCoordinate();
    Tensor centralized = lieGroupOps.allLeft(sequence, invert);
    Norm norm = Serialization.copy(mahalanobis.new Norm(centralized, weights));
    Tensor target = Tensor.of(centralized.stream().map(norm::norm));
    BarycentricCoordinate lieBarycentricCoordinate = AbsoluteCoordinate.custom( //
        Se2CoveringManifold.INSTANCE, t -> target.map(Scalar::reciprocal));
    lieBarycentricCoordinate.weights(centralized, Tensors.vector(0, 0, 0));
    Tensor affine = AffineCoordinate.of(Se2CoveringManifold.INSTANCE).weights(centralized, Tensors.vector(0, 0, 0));
    Tolerance.CHOP.requireClose(weights, affine);
  }
}
