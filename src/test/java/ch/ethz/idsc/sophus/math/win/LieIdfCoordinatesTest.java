// code by jph
package ch.ethz.idsc.sophus.math.win;

import ch.ethz.idsc.sophus.lie.rn.RnGroup;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class LieIdfCoordinatesTest extends TestCase {
  public void testLinearReproduction() {
    Distribution distribution = UniformDistribution.unit();
    for (int d = 2; d < 6; ++d)
      for (int n = d + 1; n < 10; ++n) {
        Tensor points = RandomVariate.of(distribution, n, d);
        Tensor x = RandomVariate.of(distribution, d);
        TensorUnaryOperator idfCoordinates = IdfCoordinates.of(Norm._2::ofVector, points);
        TensorUnaryOperator rn_Coordinates = new LieIdfCoordinates(RnGroup.INSTANCE, p -> p, InverseNorm.of(Norm._2::ofVector)).of(points);
        Tensor w1 = idfCoordinates.apply(x);
        Tensor w2 = rn_Coordinates.apply(x);
        Chop._06.requireClose(w1, w2);
      }
  }

  public void testLagrangeProperty() {
    Distribution distribution = UniformDistribution.unit();
    for (int d = 2; d < 6; ++d)
      for (int n = d + 1; n < 10; ++n) {
        Tensor points = RandomVariate.of(distribution, n, d);
        TensorUnaryOperator idfCoordinates = IdfCoordinates.of(Norm._2::ofVector, points);
        TensorUnaryOperator rn_Coordinates = new LieIdfCoordinates(RnGroup.INSTANCE, p -> p, InverseNorm.of(Norm._2::ofVector)).of(points);
        Chop._06.requireClose(Tensor.of(points.stream().map(idfCoordinates)), IdentityMatrix.of(n));
        Chop._06.requireClose(Tensor.of(points.stream().map(rn_Coordinates)), IdentityMatrix.of(n));
      }
  }
}
