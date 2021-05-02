// code by jph
package ch.alpine.sophus.bm;

import java.util.Arrays;

import ch.alpine.tensor.Tensor;

public enum BiinvariantMeanTestHelper {
  ;
  /** used to permute input to {@link BiinvariantMean}: sequence and weights */
  public static Tensor order(Tensor tensor, int[] index) {
    return Tensor.of(Arrays.stream(index).mapToObj(tensor::get));
  }
}
