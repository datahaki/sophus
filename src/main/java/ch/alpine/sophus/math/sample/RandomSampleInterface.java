// code by jph
package ch.alpine.sophus.math.sample;

import java.util.Random;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.RandomVariateInterface;

/** RandomSampleInterface is a generalization of {@link RandomVariateInterface}.
 * 
 * RandomSampleInterface produces tensors from a multi-variate probability distribution.
 * 
 * Examples: {@link BoxRandomSample}, {@link BallRandomSample} */
@FunctionalInterface
public interface RandomSampleInterface {
  /** @return random sample from continuous or discrete set */
  Tensor randomSample(Random random);
}