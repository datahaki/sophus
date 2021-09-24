// code by jph
package ch.alpine.sophus.lie.rn;

import ch.alpine.sophus.math.sample.BoxRandomSample;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.opt.nd.NdBox;
import ch.alpine.tensor.opt.nd.NdCenterBase;
import junit.framework.TestCase;

public class RnDbscanTest extends TestCase {
  public void testSimple() {
    RandomSampleInterface randomSampleInterface = BoxRandomSample.of(NdBox.of(Tensors.vector(0, 0), Tensors.vector(1, 1)));
    Tensor points = RandomSample.of(randomSampleInterface, 40);
    Integer[] integers = RnDbscan.of(points, NdCenterBase::of2Norm, RealScalar.of(0.1), 3);
    assertEquals(integers.length, points.length());
  }

  public void testUniform() {
    Tensor points = Range.of(0, 8).map(Tensors::of);
    Integer[] integers = RnDbscan.of(points, NdCenterBase::of2Norm, RealScalar.of(1.1), 3);
    assertEquals(Tensors.vector(integers), Array.zeros(integers.length));
  }

  public void testInsufficientRadius() {
    Tensor points = Range.of(0, 8).map(Tensors::of);
    Integer[] integers = RnDbscan.of(points, NdCenterBase::of2Norm, RealScalar.of(0.1), 2);
    assertEquals(Tensors.vector(integers), ConstantArray.of(RealScalar.of(-1), integers.length));
  }

  public void testInsufficientPoints() {
    Tensor points = Range.of(0, 8).map(Tensors::of);
    Integer[] integers = RnDbscan.of(points, NdCenterBase::of2Norm, RealScalar.of(1.1), 4);
    assertEquals(Tensors.vector(integers), ConstantArray.of(RealScalar.of(-1), integers.length));
  }

  public void testFail() {
    Tensor points = Range.of(0, 8).map(Tensors::of);
    AssertFail.of(() -> RnDbscan.of(points, NdCenterBase::of2Norm, RealScalar.of(-1.1), 3));
    AssertFail.of(() -> RnDbscan.of(points, NdCenterBase::of2Norm, RealScalar.of(+1.1), 0));
  }
}
