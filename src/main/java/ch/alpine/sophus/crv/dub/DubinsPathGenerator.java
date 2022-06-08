// code by jph
package ch.alpine.sophus.crv.dub;

import java.util.stream.Stream;

@FunctionalInterface
public interface DubinsPathGenerator {
  /** @return stream */
  Stream<DubinsPath> stream();
}
