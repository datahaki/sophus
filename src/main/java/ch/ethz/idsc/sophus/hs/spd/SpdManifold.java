// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.hs.HsManifold;
import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LinearSolve;

/** Sym+(n) == GL+(n)/SO(n)
 * 
 * Quote: "where GL+(n) is the space of matrices with positive determinant
 * (we take here the connected components of positive determinant to simplify)"
 * 
 * Reference:
 * "Riemannian Geometry for the Statistical Analysis of Diffusion Tensor Data"
 * by P. Thomas Fletcher, Sarang Joshi */
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
}
