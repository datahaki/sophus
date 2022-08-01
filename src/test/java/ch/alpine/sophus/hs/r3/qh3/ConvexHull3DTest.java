// code by jph
package ch.alpine.sophus.hs.r3.qh3;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.sample.BoxRandomSample;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

class ConvexHull3DTest {
  @Test
  void testSimple() {
    CoordinateBoundingBox ccb = CoordinateBoundingBox.of(Clips.unit(), Clips.unit(), Clips.interval(-3, 3));
    Tensor tensor = RandomSample.of(BoxRandomSample.of(ccb), 100);
    ConvexHull3D.of(tensor);
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
    List<int[]> index = ConvexHull3D.of(tensor);
    long count = index.stream().filter(a -> a.length > 3).count();
    assertTrue(5 <= count); // typically count is 6
    assertTrue(0 <= index.stream().flatMapToInt(IntStream::of).min().orElse(-1));
  }
}
