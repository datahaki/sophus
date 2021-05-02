// code by jph
package ch.alpine.sophus.crv.dubins;

import java.util.Comparator;

import ch.alpine.tensor.Scalars;

public enum DubinsPathComparators implements Comparator<DubinsPath> {
  /** sort according to increasing length */
  LENGTH {
    @Override
    public int compare(DubinsPath dubinsPath1, DubinsPath dubinsPath2) {
      return Scalars.compare(dubinsPath1.length(), dubinsPath2.length());
    }
  },
  /** sort according to increasing curvature */
  TOTAL_CURVATURE {
    @Override
    public int compare(DubinsPath dubinsPath1, DubinsPath dubinsPath2) {
      return Scalars.compare(dubinsPath1.totalCurvature(), dubinsPath2.totalCurvature());
    }
  }, //
  ;
}
