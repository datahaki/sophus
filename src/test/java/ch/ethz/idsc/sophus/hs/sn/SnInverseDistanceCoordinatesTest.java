// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SnInverseDistanceCoordinatesTest extends TestCase {
  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Norm._2);
  private static final ProjectedCoordinate[] PROJECTED_COORDINATES = { //
      HsBarycentricCoordinate.linear(SnManifold.INSTANCE), //
      HsBarycentricCoordinate.smooth(SnManifold.INSTANCE) };

  public void testLinearReproduction() {
    Distribution distribution = NormalDistribution.of(0, 0.2);
    int fail = 0;
    for (ProjectedCoordinate projectedCoordinate : PROJECTED_COORDINATES)
      for (int d = 3; d < 7; ++d)
        for (int n = d + 1; n < 10; ++n)
          try {
            Tensor mean = UnitVector.of(d, 0);
            Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, d).stream().map(mean::add).map(NORMALIZE));
            Tensor weights = projectedCoordinate.weights(sequence, mean);
            VectorQ.requireLength(weights, n);
            AffineQ.require(weights);
            Tensor evaluate = SnMeanDefect.INSTANCE.defect(sequence, weights, mean);
            Chop._12.requireAllZero(evaluate);
            Tensor point = SnMean.INSTANCE.mean(sequence, weights);
            Chop._12.requireClose(mean, point);
          } catch (Exception exception) {
            ++fail;
          }
    assertTrue(fail < 5);
  }

  public void testLagrangeProperty() {
    Distribution distribution = NormalDistribution.of(0, 0.2);
    for (ProjectedCoordinate projectedCoordinate : PROJECTED_COORDINATES)
      for (int d = 3; d < 7; ++d)
        for (int n = d + 1; n < 10; ++n) {
          Tensor center = UnitVector.of(d, 0);
          Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, d).stream().map(center::add).map(NORMALIZE));
          for (Tensor mean : sequence) {
            Tensor weights = projectedCoordinate.weights(sequence, mean);
            VectorQ.requireLength(weights, n);
            AffineQ.require(weights);
            Tensor evaluate = SnMeanDefect.INSTANCE.defect(sequence, weights, mean);
            Chop._06.requireAllZero(evaluate);
            Tensor point = SnMean.INSTANCE.mean(sequence, weights);
            Chop._06.requireClose(mean, point);
          }
        }
  }
}
