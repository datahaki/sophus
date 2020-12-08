// code by jph
package ch.ethz.idsc.sophus.lie.r3;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.lie.r2.CirclePoints;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.qty.Quantity;
import junit.framework.TestCase;

public class MinTriangleAreaSquaredTest extends TestCase {
  public void testSimple() {
    for (int n = 3; n <= 6; ++n) {
      Tensor polygon = CirclePoints.of(n);
      polygon.stream().forEach(row -> row.append(RealScalar.of(3)));
      Tensor weights = MinTriangleAreaSquared.INSTANCE.origin(polygon);
      Tolerance.CHOP.requireClose(weights, ConstantArray.of(RationalScalar.of(1, n), n));
    }
  }

  public void testSimpleUnit() {
    for (int n = 3; n <= 6; ++n) {
      Tensor polygon = CirclePoints.of(n);
      polygon.stream().forEach(row -> row.append(RealScalar.of(3)));
      polygon = polygon.map(s -> Quantity.of(s, "s"));
      Tensor weights = MinTriangleAreaSquared.INSTANCE.origin(polygon);
      Tolerance.CHOP.requireClose(weights, ConstantArray.of(RationalScalar.of(1, n), n));
    }
  }

  public void testFive() {
    Distribution distribution = UniformDistribution.unit();
    for (int count = 0; count < 10; ++count) {
      Tensor polygon1 = RandomVariate.of(distribution, 5, 3);
      Tensor polygon2 = polygon1.multiply(RealScalar.of(1000));
      Tolerance.CHOP.requireClose( //
          MinTriangleAreaSquared.normalize(polygon1), //
          MinTriangleAreaSquared.normalize(polygon2));
      Tolerance.CHOP.requireClose( //
          MinTriangleAreaSquared.INSTANCE.origin(polygon1), //
          MinTriangleAreaSquared.INSTANCE.origin(polygon2));
    }
  }

  public void testThree() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor polygon = RandomVariate.of(distribution, 3, 3);
      Tensor weights = MinTriangleAreaSquared.INSTANCE.origin(polygon);
      Tolerance.CHOP.requireClose(weights, ConstantArray.of(RationalScalar.of(1, 3), 3));
    }
  }

  public void testTwo() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor polygon = RandomVariate.of(distribution, 2, 3);
      Tensor weights = MinTriangleAreaSquared.INSTANCE.origin(polygon);
      Tolerance.CHOP.requireClose(weights, ConstantArray.of(RationalScalar.of(1, 2), 2));
    }
  }

  public void testOne() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor polygon = RandomVariate.of(distribution, 1, 3);
      Tensor weights = MinTriangleAreaSquared.INSTANCE.origin(polygon);
      Tolerance.CHOP.requireClose(weights, ConstantArray.of(RationalScalar.of(1, 1), 1));
    }
  }

  public void testEmptyFail() {
    AssertFail.of(() -> MinTriangleAreaSquared.INSTANCE.origin(Tensors.empty()));
  }
}
