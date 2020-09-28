// code by jph
package ch.ethz.idsc.sophus.crv.dubins;

import java.util.stream.Stream;

@FunctionalInterface
public interface DubinsPathGenerator {
  /** @return stream */
  Stream<DubinsPath> stream();
}
