// code by jph
package ch.ethz.idsc.sophus.decim;

import ch.ethz.idsc.sophus.hs.HsManifold;

/** various norms for curve decimation */
public enum LineDistances {
  STANDARD {
    @Override
    public LineDistance supply(HsManifold hsManifold) {
      return new HsLineDistance(hsManifold);
    }
  },
  MIDPOINT {
    @Override
    public LineDistance supply(HsManifold hsManifold) {
      return HsMidpointLineDistance.of(hsManifold);
    }
  },
  SYMMETRIZED {
    @Override
    public LineDistance supply(HsManifold hsManifold) {
      return new SymmetricLineDistance(new HsLineDistance(hsManifold));
    }
  },
  PROJECTED {
    @Override
    public LineDistance supply(HsManifold hsManifold) {
      return new HsProjectedLineDistance(hsManifold);
    }
  };

  public abstract LineDistance supply(HsManifold hsManifold);
}
