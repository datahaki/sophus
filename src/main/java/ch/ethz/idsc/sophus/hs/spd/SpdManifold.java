// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

/** Sym+(n) == GL+(n)/SO(n)
 * 
 * where GL+(n) is the space of matrices with positive determinant
 * 
 * Reference:
 * "Riemannian Geometry for the Statistical Analysis of Diffusion Tensor Data"
 * by P. Thomas Fletcher, Sarang Joshi */
public enum SpdManifold implements HsExponential, VectorLogManifold {
  INSTANCE;

  @Override // from HsExponential
  public Exponential exponential(Tensor point) {
    return new SpdExponential(point);
  }

  @Override // from VectorLogManifold
  public TangentSpace logAt(Tensor point) {
    return new SpdExponential(point);
  }
}
