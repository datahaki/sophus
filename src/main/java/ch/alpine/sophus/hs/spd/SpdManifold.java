// code by jph
package ch.alpine.sophus.hs.spd;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.hs.HsManifold;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.TangentSpace;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.re.LinearSolve;

/** Sym+(n) == GL+(n)/SO(n)
 * 
 * Quote: "where GL+(n) is the space of matrices with positive determinant
 * (we take here the connected components of positive determinant to simplify)"
 * 
 * References:
 * "Riemannian Geometry for the Statistical Analysis of Diffusion Tensor Data"
 * by P. Thomas Fletcher, Sarang Joshi
 * 
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
public enum SpdManifold implements HsManifold {
  INSTANCE;

  @Override // from VectorLogManifold
  public TangentSpace logAt(Tensor p) {
    return new SpdExponential(p);
  }

  @Override // from HsManifold
  public Exponential exponential(Tensor p) {
    return new SpdExponential(p);
  }

  @Override // from HsManifold
  public Tensor flip(Tensor p, Tensor q) {
    return p.dot(LinearSolve.of(q, p));
  }

  @Override
  public HsTransport hsTransport() {
    return SpdTransport.INSTANCE;
  }
}
