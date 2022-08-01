// code by jph
package ch.alpine.sophus.hs.r3.qh3;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.sample.BoxRandomSample;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.red.ScalarSummaryStatistics;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.Sign;

class ConvexHull3DTest {
  @Test
  void testSimple() {
    CoordinateBoundingBox ccb = CoordinateBoundingBox.of(Clips.unit(), Clips.unit(), Clips.interval(-3, 3));
    Tensor tensor = RandomSample.of(BoxRandomSample.of(ccb), 100);
    int[][] index = ConvexHull3D.of(tensor);
    Tensors.matrixInt(index);
  }

  @Test
  void testCuboid() {
    Clip[] clips = { Clips.unit(), Clips.unit(), Clips.interval(-3, 3) };
    CoordinateBoundingBox ccb = CoordinateBoundingBox.of(clips);
    Random random = new Random();
    Tensor tensor = RandomSample.of(BoxRandomSample.of(ccb), random, 200);
    for (int index = 0; index < tensor.length(); ++index) {
      int i = random.nextInt(3);
      if (random.nextBoolean())
        tensor.set(clips[i].min(), index, i);
      else
        tensor.set(clips[i].max(), index, i);
    }
    int[][] index = ConvexHull3D.of(tensor);
    long count = Stream.of(index).filter(a -> a.length > 3).count();
    assertTrue(5 <= count); // typically count is6
    Tensor faces = Tensors.matrixInt(index);
    ScalarSummaryStatistics scalarSummaryStatistics = faces.flatten(1) //
        .map(Scalar.class::cast) //
        .collect(ScalarSummaryStatistics.collector());
    Sign.requirePositiveOrZero(scalarSummaryStatistics.getMin());
  }
}
