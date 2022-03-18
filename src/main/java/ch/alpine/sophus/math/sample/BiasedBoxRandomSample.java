// code by jph
package ch.alpine.sophus.math.sample;

import java.io.Serializable;
import java.util.Random;

import ch.alpine.sophus.math.noise.SimplexContinuousNoise;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.ArgMin;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;

public class BiasedBoxRandomSample implements RandomSampleInterface, Serializable {
  private final RandomSampleInterface randomSampleInterface;
  private final int draws;

  /** @param coordinateBoundingBox
   * @param draws positive */
  public BiasedBoxRandomSample(CoordinateBoundingBox coordinateBoundingBox, int draws) {
    randomSampleInterface = BoxRandomSample.of(coordinateBoundingBox);
    this.draws = Integers.requirePositive(draws);
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(Random random) {
    Tensor tensor = RandomSample.of(randomSampleInterface, draws);
    Tensor result = Tensor.of(tensor.stream().map(SimplexContinuousNoise.FUNCTION));
    int argMin = ArgMin.of(result);
    return tensor.get(argMin);
  }
}
