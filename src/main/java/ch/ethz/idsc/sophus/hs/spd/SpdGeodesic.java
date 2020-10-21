// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.math.GeodesicInterface;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarTensorFunction;

/** References:
 * "Riemannian Geometric Statistics in Medical Image Analysis", 2020
 * Edited by Xavier Pennec, Stefan Sommer, Tom Fletcher
 * p. 86 eqs 3.12 and 3.13, also
 * p. 89
 * 
 * "Subdivision Schemes for Positive Definite Matrices"
 * by Uri Itai, Nir Sharon
 * 
 * "Approximation schemes for functions of positive-definite matrix values"
 * by Nir Sharon, Uri Itai
 * 
 * Riemannian Variance Filtering: An Independent Filtering Scheme for Statistical
 * Tests on Manifold-valued Data */
public enum SpdGeodesic implements GeodesicInterface {
  INSTANCE;

  @Override // from TensorGeodesic
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    SpdExponential spdExp = new SpdExponential(p);
    Tensor w = spdExp.log(q);
    return scalar -> spdExp.exp(w.multiply(scalar));
  }

  @Override // from GeodesicInterface
  public Tensor split(Tensor p, Tensor q, Scalar scalar) {
    return curve(p, q).apply(scalar);
  }
}
