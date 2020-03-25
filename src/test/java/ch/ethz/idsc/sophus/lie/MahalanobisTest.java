// code by jph
package ch.ethz.idsc.sophus.lie;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.Mahalanobis.Norm;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringAffineCoordinate;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringBiinvariantMean;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringExponential;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringGroup;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import junit.framework.TestCase;

public class MahalanobisTest extends TestCase {
  public void testSimple() {
    LieGroupOps lieGroupOps = new LieGroupOps(Se2CoveringGroup.INSTANCE);
    Mahalanobis mahalanobis = new Mahalanobis(Se2CoveringGroup.INSTANCE, Se2CoveringExponential.INSTANCE, Se2CoveringBiinvariantMean.INSTANCE);
    int n = 10;
    Tensor sequence = RandomVariate.of(UniformDistribution.of(-2, 2), n, 3);
    Tensor weights = ConstantArray.of(RationalScalar.of(1, n), n);
    // weights = NormalizeTotal.FUNCTION.apply(UnitVector.of(n, 0).add(UnitVector.of(n, 2)));
    // weights = UnitVector.of(n, 0);
    Tensor mean = Se2CoveringBiinvariantMean.INSTANCE.mean(sequence, weights);
    Tensor invert = Se2CoveringGroup.INSTANCE.element(mean).inverse().toCoordinate();
    Tensor centralized = lieGroupOps.allLeft(sequence, invert);
    // System.out.println(Pretty.of(centralized.map(Round._4)));
    Norm norm = mahalanobis.new Norm(centralized, weights);
    // for (Tensor x : centralized) {
    // System.out.println(x.map(Round._4));
    // System.out.println("distance=" + norm.norm(x).map(Round._4));
    // System.out.println("---");
    // }
    Tensor target = Tensor.of(centralized.stream().map(norm::norm));
    ProjectedCoordinate lieBarycentricCoordinate = new HsBarycentricCoordinate( //
        LieFlattenLogManifold.of(Se2CoveringGroup.INSTANCE, Se2CoveringExponential.INSTANCE::log), //
        t -> target.map(Scalar::reciprocal));
    // Tensor weights2 =
    lieBarycentricCoordinate.weights(centralized, Tensors.vector(0, 0, 0));
    // System.out.println(weights2);
    Tensor affine = Se2CoveringAffineCoordinate.INSTANCE.weights(centralized, Tensors.vector(0, 0, 0));
    Tolerance.CHOP.requireClose(weights, affine);
  }
}
