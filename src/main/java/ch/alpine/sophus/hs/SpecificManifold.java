// code by jph
package ch.alpine.sophus.hs;

import ch.alpine.tensor.pdf.RandomSampleInterface;

// TODO SOPHUS somewhat redundant to VectorEncodingMarker
public interface SpecificManifold extends RandomSampleInterface {
  int dimensions();
}
