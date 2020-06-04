// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.IOException;

import ch.ethz.idsc.sophus.gbc.AbsoluteCoordinate;
import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.RelativeCoordinate;
import ch.ethz.idsc.sophus.hs.HsMahalanobis.Norm;
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

public class HsMahalanobisTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    LieGroupOps lieGroupOps = new LieGroupOps(Se2CoveringGroup.INSTANCE);
    HsMahalanobis mahalanobis = new HsMahalanobis(Se2CoveringManifold.INSTANCE, Se2CoveringBiinvariantMean.INSTANCE);
    int n = 10;
    Tensor sequence = RandomVariate.of(UniformDistribution.of(-2, 2), n, 3);
    Tensor weights = ConstantArray.of(RationalScalar.of(1, n), n);
    // weights = NormalizeTotal.FUNCTION.apply(UnitVector.of(n, 0).add(UnitVector.of(n, 2)));
    // weights = UnitVector.of(n, 0);
    Tensor mean = Se2CoveringBiinvariantMean.INSTANCE.mean(sequence, weights);
    Tensor invert = Se2CoveringGroup.INSTANCE.element(mean).inverse().toCoordinate();
    Tensor centralized = lieGroupOps.allLeft(sequence, invert);
    // System.out.println(Pretty.of(centralized.map(Round._4)));
    Norm norm = Serialization.copy(mahalanobis.new Norm(centralized, weights));
    // for (Tensor x : centralized) {
    // System.out.println(x.map(Round._4));
    // System.out.println("distance=" + norm.norm(x).map(Round._4));
    // System.out.println("---");
    // }
    Tensor target = Tensor.of(centralized.stream().map(norm::norm));
    BarycentricCoordinate lieBarycentricCoordinate = AbsoluteCoordinate.custom( //
        Se2CoveringManifold.INSTANCE, t -> target.map(Scalar::reciprocal));
    // Tensor weights2 =
    lieBarycentricCoordinate.weights(centralized, Tensors.vector(0, 0, 0));
    // System.out.println(weights2);
    Tensor affine = RelativeCoordinate.affine(Se2CoveringManifold.INSTANCE).weights(centralized, Tensors.vector(0, 0, 0));
    Tolerance.CHOP.requireClose(weights, affine);
  }
}
