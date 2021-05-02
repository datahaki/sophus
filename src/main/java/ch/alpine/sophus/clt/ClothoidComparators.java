// code by jph
package ch.alpine.sophus.clt;

import java.util.Comparator;

import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.sca.Abs;

public enum ClothoidComparators implements Comparator<Clothoid> {
  /** sort according to length of clothoid */
  LENGTH {
    @Override
    public int compare(Clothoid clothoid1, Clothoid clothoid2) {
      return Scalars.compare(clothoid1.length(), clothoid2.length());
    }
  },
  /** sort according to absolute curvature at head of clothoid */
  CURVATURE_HEAD {
    @Override
    public int compare(Clothoid clothoid1, Clothoid clothoid2) {
      return Scalars.compare( //
          Abs.FUNCTION.apply(clothoid1.curvature().head()), //
          Abs.FUNCTION.apply(clothoid2.curvature().head()));
    }
  }, //
  ;
}
