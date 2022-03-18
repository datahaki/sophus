// code by jph
package ch.alpine.sophus.srf.io;

import java.util.stream.Stream;

public enum WavefrontFormat {
  ;
  /** parse Wavefront .obj file
   * 
   * @param stream
   * @return */
  public static Wavefront parse(Stream<String> stream) {
    WavefrontImpl wavefrontImpl = new WavefrontImpl();
    stream.sequential().forEach(wavefrontImpl::parse);
    return wavefrontImpl;
  }
}
