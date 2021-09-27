// code by jph
package ch.alpine.sophus.math.var;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.api.ScalarUnaryOperator;

/** Quote from Numerical Recipes:
 * To use kriging, you must be able to estimate the mean square variation of
 * your function y(x) as a function of offset distance r, a so-called variogram
 * <pre>
 * v(r) == 1/2 E[ (y(x+r) - y(x)) ^ 2 ]
 * </pre> */
public enum Variograms {
  INVERSE_POWER {
    @Override
    public ScalarUnaryOperator of(Scalar param) {
      return InversePowerVariogram.of(param);
    }
  },
  MULTIQUADRIC {
    @Override
    public ScalarUnaryOperator of(Scalar param) {
      return MultiquadricVariogram.of(param);
    }
  },
  INVERSE_MULTIQUADRIC {
    @Override
    public ScalarUnaryOperator of(Scalar param) {
      return InverseMultiquadricVariogram.of(param);
    }
  },
  // ---
  /** 0 -> 1, Infinity -> 0 */
  GAUSSIAN {
    @Override
    public ScalarUnaryOperator of(Scalar param) {
      return GaussianVariogram.of(param);
    }
  },
  // ---
  /** 0 -> 0, Infinity -> 1 */
  EXPONENTIAL {
    @Override
    public ScalarUnaryOperator of(Scalar param) {
      return ExponentialVariogram.of(param, RealScalar.ONE);
    }
  },
  /** 0 -> 0, Infinity -> 1 */
  SPHERICAL {
    @Override
    public ScalarUnaryOperator of(Scalar param) {
      return SphericalVariogram.of(param, RealScalar.ONE);
    }
  },
  // ---
  /** 0 -> 0, Infinity -> Infinity */
  POWER {
    @Override
    public ScalarUnaryOperator of(Scalar param) {
      return PowerVariogram.of(RealScalar.ONE, param);
    }
  },
  /** 0 -> 0, Infinity -> Infinity */
  THIN_PLATE_SPLINE {
    @Override
    public ScalarUnaryOperator of(Scalar param) {
      return ThinPlateSplineVariogram.of(param);
    }
  },
  // ---
  ;

  /** @param param
   * @return variogram */
  public abstract ScalarUnaryOperator of(Scalar param);
}
