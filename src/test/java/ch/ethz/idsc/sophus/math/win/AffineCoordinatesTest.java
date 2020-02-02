// code by jph
package ch.ethz.idsc.sophus.math.win;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.DiscreteUniformDistribution;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Mean;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class AffineCoordinatesTest extends TestCase {
  public void testMean() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 3; n < 10; ++n) {
      Tensor points = RandomVariate.of(distribution, n, 2);
      TensorUnaryOperator affineCoordinates = AffineCoordinates.of(points);
      Tensor weights = affineCoordinates.apply(Mean.of(points));
      Chop._10.requireClose(weights, ConstantArray.of(RealScalar.of(1.0 / n), n));
    }
  }

  public void testExact() {
    Distribution distribution = DiscreteUniformDistribution.of(-1000, 1000);
    for (int n = 3; n < 10; ++n) {
      Tensor points = RandomVariate.of(distribution, n, 2);
      TensorUnaryOperator affineCoordinates = AffineCoordinates.of(points);
      Tensor weights = affineCoordinates.apply(Mean.of(points));
      Chop._10.requireClose(weights, ConstantArray.of(RationalScalar.of(1, n), n));
      // ExactTensorQ.require(weights);
    }
  }

  public void testUnity() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 3; n < 10; ++n) {
      Tensor points = RandomVariate.of(distribution, n, n - 1);
      TensorUnaryOperator affineCoordinates = AffineCoordinates.of(points);
      Tensor weights = affineCoordinates.apply(Mean.of(points));
      Chop._10.requireClose(weights, ConstantArray.of(RealScalar.of(1.0 / n), n));
      Chop._10.requireClose(Tensor.of(points.stream().map(affineCoordinates)), IdentityMatrix.of(n));
    }
  }

  public void testPoints() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 5; n < 10; ++n) {
      Tensor p1 = RandomVariate.of(distribution, n, 2);
      TensorUnaryOperator affineCoordinates = AffineCoordinates.of(p1);
      Tensor p2 = Tensor.of(p1.stream().map(affineCoordinates)).dot(p1);
      Chop._08.requireClose(p1, p2);
    }
  }

  public void testSmallN() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 1; n < 3; ++n) {
      Tensor points = RandomVariate.of(distribution, n, 2);
      TensorUnaryOperator affineCoordinates = AffineCoordinates.of(points);
      Tensor tensor = Tensor.of(points.stream().map(affineCoordinates));
      Chop._10.requireClose(tensor, IdentityMatrix.of(n));
    }
  }

  public void testEmptyFail() {
    try {
      AffineCoordinates.of(Tensors.empty());
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
