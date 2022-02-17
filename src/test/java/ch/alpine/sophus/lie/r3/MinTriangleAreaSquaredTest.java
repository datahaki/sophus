// code by jph
package ch.alpine.sophus.lie.r3;

import java.util.Random;

import ch.alpine.sophus.gbc.AveragingWeights;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.sca.Clips;
import junit.framework.TestCase;

public class MinTriangleAreaSquaredTest extends TestCase {
  public void testSimple() {
    for (int n = 3; n <= 6; ++n) {
      Tensor polygon = CirclePoints.of(n);
      polygon.stream().forEach(row -> row.append(RealScalar.of(3)));
      Tensor weights = MinTriangleAreaSquared.INSTANCE.origin(polygon);
      Tolerance.CHOP.requireClose(weights, AveragingWeights.of(n));
    }
  }

  public void testSimpleUnit() {
    for (int n = 3; n <= 6; ++n) {
      Tensor polygon = CirclePoints.of(n);
      polygon.stream().forEach(row -> row.append(RealScalar.of(3)));
      polygon = polygon.map(s -> Quantity.of(s, "s"));
      Tensor weights = MinTriangleAreaSquared.INSTANCE.origin(polygon);
      Tolerance.CHOP.requireClose(weights, AveragingWeights.of(n));
    }
  }

  public void testFive() {
    Random random = new Random(3);
    Distribution distribution = UniformDistribution.unit();
    for (int count = 0; count < 10; ++count) {
      Tensor polygon1 = RandomVariate.of(distribution, random, 5, 3);
      Tensor polygon2 = polygon1.multiply(RealScalar.of(1000));
      Tolerance.CHOP.requireClose( //
          MinTriangleAreaSquared.normalize(polygon1), //
          MinTriangleAreaSquared.normalize(polygon2));
      Tolerance.CHOP.requireClose( //
          MinTriangleAreaSquared.INSTANCE.origin(polygon1), //
          MinTriangleAreaSquared.INSTANCE.origin(polygon2));
    }
  }

  public void testFiveUnit() {
    Random random = new Random(3);
    Distribution distribution = UniformDistribution.of(Clips.absolute(Quantity.of(1, "kg")));
    for (int count = 0; count < 10; ++count) {
      Tensor polygon1 = RandomVariate.of(distribution, random, 5, 3);
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
      Tolerance.CHOP.requireClose(weights, AveragingWeights.of(3));
    }
  }

  public void testTwo() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor polygon = RandomVariate.of(distribution, 2, 3);
      Tensor weights = MinTriangleAreaSquared.INSTANCE.origin(polygon);
      Tolerance.CHOP.requireClose(weights, AveragingWeights.of(2));
    }
  }

  public void testOne() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor polygon = RandomVariate.of(distribution, 1, 3);
      Tensor weights = MinTriangleAreaSquared.INSTANCE.origin(polygon);
      Tolerance.CHOP.requireClose(weights, AveragingWeights.of(1));
    }
  }

  public void testEmptyFail() {
    AssertFail.of(() -> MinTriangleAreaSquared.INSTANCE.origin(Tensors.empty()));
  }
}
