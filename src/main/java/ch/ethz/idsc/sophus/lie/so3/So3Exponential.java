// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;

public class So3Exponential implements Exponential, TangentSpace, Serializable {
  private final Tensor p;
  private final Tensor pinv;

  public So3Exponential(Tensor p) {
    this.p = OrthogonalMatrixQ.require(p);
    pinv = Transpose.of(p);
  }

  /** @throws Exception if vp is not tangent to p */
  @Override // from Exponential
  public Tensor exp(Tensor vp) { // tangent vector at p
    return p.dot(Rodrigues.INSTANCE.exp(pinv.dot(vp)));
  }

  @Override // from Exponential
  public Tensor log(Tensor q) {
    return p.dot(Rodrigues.INSTANCE.log(pinv.dot(q)));
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor q) {
    // p dot corresponds to a basis transform, which is obsolete
    return Rodrigues.INSTANCE.vectorLog(pinv.dot(q));
  }
}
