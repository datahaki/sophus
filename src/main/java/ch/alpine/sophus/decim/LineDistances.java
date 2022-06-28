// code by jph
package ch.alpine.sophus.decim;

import ch.alpine.sophus.hs.HomogeneousSpace;

/** various norms for curve decimation */
public enum LineDistances {
  STANDARD {
    @Override
    public LineDistance supply(HomogeneousSpace homogeneousSpace) {
      return new HsLineDistance(homogeneousSpace);
    }
  },
  SYMMETRIZED {
    @Override
    public LineDistance supply(HomogeneousSpace homogeneousSpace) {
      return new SymmetricLineDistance(new HsLineDistance(homogeneousSpace));
    }
  },
  PROJECTED {
    @Override
    public LineDistance supply(HomogeneousSpace homogeneousSpace) {
      return new HsProjectedLineDistance(homogeneousSpace);
    }
  };

  public abstract LineDistance supply(HomogeneousSpace homogeneousSpace);
}
