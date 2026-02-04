package ch.alpine.sophus.hs;

import ch.alpine.sophus.lie.VectorEncodingMarker;
import ch.alpine.tensor.pdf.RandomSampleInterface;

/** TODO SOPHUS somewhat redundant to {@link VectorEncodingMarker} */
public interface SpecificManifold extends RandomSampleInterface {
  int dimensions();
}
