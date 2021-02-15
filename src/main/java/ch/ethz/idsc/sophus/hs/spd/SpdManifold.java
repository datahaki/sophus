// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.hs.HsManifold;
import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

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

  @Override // from HsExponential
  public Exponential exponential(Tensor p) {
    return new SpdExponential(p);
  }

  @Override // from VectorLogManifold
  public TangentSpace logAt(Tensor p) {
    return new SpdExponential(p);
  }
}
