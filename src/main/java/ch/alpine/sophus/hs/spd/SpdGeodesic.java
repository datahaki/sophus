// code by jph
package ch.alpine.sophus.hs.spd;

import ch.alpine.sophus.hs.HsGeodesic;
import ch.alpine.sophus.math.Geodesic;

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
public enum SpdGeodesic {
  ;
  public static final Geodesic INSTANCE = new HsGeodesic(SpdManifold.INSTANCE);
}
