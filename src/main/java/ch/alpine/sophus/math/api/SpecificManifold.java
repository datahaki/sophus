// code by jph
package ch.alpine.sophus.math.api;

import ch.alpine.tensor.pdf.RandomSampleInterface;

// TODO SOPHUS somewhat redundant to VectorEncodingMarker
public interface SpecificManifold extends RandomSampleInterface {
  int dimensions();
}
