// code by jph
package ch.ethz.idsc.sophus.crv.decim;

import ch.ethz.idsc.sophus.hs.HsExponential;

/** various norms for curve decimation */
public enum LineDistances {
  STANDARD {
    @Override
    public LineDistance supply(HsExponential hsExponential) {
      return new HsLineDistance(hsExponential);
    }
  },
  MIDPOINT {
    @Override
    public LineDistance supply(HsExponential hsExponential) {
      return HsMidpointLineDistance.of(hsExponential);
    }
  },
  SYMMETRIZED {
    @Override
    public LineDistance supply(HsExponential hsExponential) {
      return new SymmetricLineDistance(new HsLineDistance(hsExponential));
    }
  },
  PROJECTED {
    @Override
    public LineDistance supply(HsExponential hsExponential) {
      return new HsProjectedLineDistance(hsExponential);
    }
  };

  public abstract LineDistance supply(HsExponential hsExponential);
}
