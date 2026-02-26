// code by jph
package ch.alpine.sophus.api;

/** this interface means that the input to Exponential#exp are vectors */
public interface VectorEncodingMarker { // TODO obsolete due to LieExponential
  int dimensions();
}
