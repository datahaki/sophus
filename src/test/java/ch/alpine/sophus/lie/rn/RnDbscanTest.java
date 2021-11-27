// code by jph
package ch.alpine.sophus.lie.rn;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ch.alpine.sophus.math.sample.BoxRandomSample;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.opt.nd.CoordinateBounds;
import ch.alpine.tensor.opt.nd.NdCenters;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.red.Tally;
import junit.framework.TestCase;

public class RnDbscanTest extends TestCase {
  public void testSimple() {
    RandomSampleInterface randomSampleInterface = BoxRandomSample.of(CoordinateBounds.of(Tensors.vector(0, 0), Tensors.vector(1, 1)));
    Tensor points = RandomSample.of(randomSampleInterface, 40);
    Integer[] integers = RnDbscan.of(points, NdCenters.VECTOR_2_NORM, RealScalar.of(0.1), 3);
    assertEquals(integers.length, points.length());
  }

  public void testUniform() {
    Tensor points = Range.of(0, 8).map(Tensors::of);
    Integer[] integers = RnDbscan.of(points, NdCenters.VECTOR_1_NORM, RealScalar.of(1), 3);
    assertEquals(Tensors.vector(integers), Array.zeros(integers.length));
  }

  public void testInsufficientRadius() {
    Tensor points = Range.of(0, 8).map(Tensors::of);
    Integer[] integers = RnDbscan.of(points, NdCenters.VECTOR_2_NORM, RealScalar.of(0.1), 2);
    assertEquals(Tensors.vector(integers), ConstantArray.of(RealScalar.of(-1), integers.length));
  }

  public void testInsufficientPoints() {
    Tensor points = Range.of(0, 8).map(Tensors::of);
    Integer[] integers = RnDbscan.of(points, NdCenters.VECTOR_INFINITY_NORM, RealScalar.of(1), 4);
    assertEquals(Tensors.vector(integers), ConstantArray.of(RealScalar.of(-1), integers.length));
  }

  public void testQuantity() {
    Tensor points = Range.of(0, 8).map(Tensors::of).map(s -> Quantity.of(s, "m"));
    Integer[] integers = RnDbscan.of(points, NdCenters.VECTOR_2_NORM, Quantity.of(1, "m"), 3);
    assertEquals(Tensors.vector(integers), Array.zeros(integers.length));
  }

  public void testSpaced() {
    Tensor points = Join.of(Range.of(0, 7), Range.of(10, 15), Range.of(20, 24)).map(Tensors::of);
    Integer[] integers = RnDbscan.of(points, NdCenters.VECTOR_INFINITY_NORM, Quantity.of(1, ""), 3);
    assertEquals(Tensors.vector(integers), Tensors.vector(0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2));
  }

  public void testSpacedPermuted() {
    Tensor points = Join.of(Range.of(0, 7), Range.of(10, 15), Range.of(20, 24)).map(Tensors::of);
    List<Integer> perm = IntStream.range(0, points.length()).boxed().collect(Collectors.toList());
    Collections.shuffle(perm);
    points = Tensor.of(perm.stream().map(points::get));
    Integer[] integers = RnDbscan.of(points, NdCenters.VECTOR_1_NORM, Quantity.of(1, ""), 3);
    Map<Tensor, Long> map = Tally.of(Tensors.vector(integers)); // for instance {1=7, 2=5, 0=4}
    assertEquals(map.size(), 3);
    assertTrue(map.values().contains(4L));
    assertTrue(map.values().contains(5L));
    assertTrue(map.values().contains(7L));
  }

  public void testFail() {
    Tensor points = Range.of(0, 8).map(Tensors::of);
    AssertFail.of(() -> RnDbscan.of(points, NdCenters.VECTOR_2_NORM, RealScalar.of(-1.1), 3));
    AssertFail.of(() -> RnDbscan.of(points, NdCenters.VECTOR_2_NORM, RealScalar.of(+1.1), 0));
  }
}
