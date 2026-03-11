// code by jph
package ch.alpine.sophus.api;

import ch.alpine.tensor.pdf.RandomSampleInterface;

/** dimensions and random sample */
public interface SpecificManifold extends RandomSampleInterface {
  /** @return */
  int dimensions();
}
