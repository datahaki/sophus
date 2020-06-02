// code by jph
package ch.ethz.idsc.sophus.lie.sc;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.lie.LieVectorLogManifold;

public enum ScManifold {
  ;
  public static final VectorLogManifold INSTANCE = //
      LieVectorLogManifold.of(ScGroup.INSTANCE, ScExponential.INSTANCE::log);
}
