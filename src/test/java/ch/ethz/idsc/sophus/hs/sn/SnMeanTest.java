// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.so2.AngleVector;
import ch.ethz.idsc.sophus.lie.so3.So3Exponential;
import ch.ethz.idsc.sophus.math.ArcTan2D;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Mean;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SnMeanTest extends TestCase {
  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Norm._2);
  private static final ProjectedCoordinate[] PROJECTED_COORDINATES = { //
      HsBarycentricCoordinate.linear(SnManifold.INSTANCE), //
      HsBarycentricCoordinate.smooth(SnManifold.INSTANCE) };

  public void testSpecific() {
    Distribution distribution = NormalDistribution.of(0, 0.2);
    for (ProjectedCoordinate projectedCoordinate : PROJECTED_COORDINATES)
      for (int count = 0; count < 10; ++count) {
        Tensor rotation = So3Exponential.INSTANCE.exp(RandomVariate.of(distribution, 3));
        Tensor mean = rotation.dot(NORMALIZE.apply(Tensors.vector(1, 1, 1)));
        Tensor sequence = Tensor.of(IdentityMatrix.of(3).stream().map(rotation::dot));
        Tensor weights = projectedCoordinate.weights(sequence, mean);
        Chop._12.requireClose(weights, NormalizeTotal.FUNCTION.apply(Tensors.vector(1, 1, 1)));
        Tensor evaluate = SnMean.INSTANCE.defect(sequence, weights, mean);
        Chop._12.requireAllZero(evaluate);
        Tensor point = SnMean.INSTANCE.mean(sequence, weights);
        Chop._12.requireClose(mean, point);
      }
  }

  public void testS1Linear() {
    int fail = 0;
    Distribution distribution = UniformDistribution.of(0, Math.PI);
    for (int n = 2; n < 10; ++n)
      for (int count = 0; count < 10; ++count)
        try {
          Tensor angles = RandomVariate.of(distribution, n);
          Tensor sequence = angles.map(AngleVector::of);
          Tensor weights = ConstantArray.of(RationalScalar.of(1, n), n);
          Tensor point = SnMean.INSTANCE.mean(sequence, weights);
          Chop._12.requireClose(ArcTan2D.of(point), Mean.of(angles));
        } catch (Exception exception) {
          ++fail;
        }
    assertTrue(fail < 3);
  }
}
