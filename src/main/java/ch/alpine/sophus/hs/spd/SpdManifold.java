// code by jph
package ch.alpine.sophus.hs.spd;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.MetricManifold;
import ch.alpine.sophus.math.LowerVectorize0_2Norm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.re.LinearSolve;
import ch.alpine.tensor.sca.Chop;

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
 * Tests on Manifold-valued Data
 * 
 * Reference:
 * "Riemannian Geometric Statistics in Medical Image Analysis", 2020
 * Edited by Xavier Pennec, Stefan Sommer, Tom Fletcher, p. 82 */
public enum SpdManifold implements HomogeneousSpace, MetricManifold {
  INSTANCE;

  @Override // from Manifold
  public Exponential exponential(Tensor p) {
    return new SpdExponential(p);
  }

  @Override
  public Tensor flip(Tensor p, Tensor q) {
    return p.dot(LinearSolve.of(q, p));
  }

  @Override // from HomogeneousSpace
  public HsTransport hsTransport() {
    return SpdTransport.INSTANCE;
  }

  @Override // from HomogeneousSpace
  public BiinvariantMean biinvariantMean(Chop chop) {
    return IterativeBiinvariantMean.reduce(this, chop);
  }

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    return new SpdExponential(p).distance(q);
  }

  @Override // from TensorNorm
  public Scalar norm(Tensor v) {
    // TODO SOPHUS this is not in-sync with distance!
    return LowerVectorize0_2Norm.INSTANCE.norm(v);
  }
}
