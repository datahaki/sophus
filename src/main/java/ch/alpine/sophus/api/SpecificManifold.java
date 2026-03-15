// code by jph
package ch.alpine.sophus.api;

import ch.alpine.tensor.pdf.RandomSampleInterface;

/** dimensions and random sample */
public interface SpecificManifold extends Manifold {
  /** @return */
  int dimensions();

  /** Remark: specific manifold does not extend RSI directly,
   * but returns an instance via {@link #randomSampleInterface()}.
   * This is because formulas might differ depending on dimension
   * (sphere, projective space, ...)
   * 
   * @return implementation of a default random sample formula */
  RandomSampleInterface randomSampleInterface();
}
