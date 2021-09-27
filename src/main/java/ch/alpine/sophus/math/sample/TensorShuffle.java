// code by jph
package ch.alpine.sophus.math.sample;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import ch.alpine.tensor.Tensor;

public enum TensorShuffle {
  ;
  private static final Random RANDOM = new SecureRandom();

  /** @param tensor
   * @return random permutation of entries of tensor */
  public static Tensor of(Tensor tensor) {
    return Tensor.of(stream(tensor));
  }

  /** @param tensor
   * @return stream of entries of tensor in random order */
  public static Stream<Tensor> stream(Tensor tensor) {
    List<Integer> list = IntStream.range(0, tensor.length()) //
        .boxed() //
        .collect(Collectors.toList());
    Collections.shuffle(list, RANDOM);
    return list.stream().map(tensor::get);
  }
}
