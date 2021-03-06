// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.Serializable;

import ch.ethz.idsc.sophus.decim.LineDistance;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.TensorProduct;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;
import ch.ethz.idsc.tensor.sca.ArcSin;
import ch.ethz.idsc.tensor.sca.Clips;

/** distance between line spanned by p and q on S^n and point r on S^n */
public enum SnLineDistance implements LineDistance {
  INSTANCE;

  @Override // from LineDistance
  public TensorNorm tensorNorm(Tensor p, Tensor q) {
    return new SnLine(p, q);
  }

  private class SnLine implements TensorNorm, Serializable {
    private final Tensor m;

    public SnLine(Tensor p, Tensor q) {
      Tensor id_pp = IdentityMatrix.of(p.length()).subtract(TensorProduct.of(p, p));
      Tensor qn = Vector2Norm.NORMALIZE.apply(id_pp.dot(q)); // qn is orthogonal to p
      // Tolerance.CHOP.requireZero((Scalar) p.dot(qn));
      m = id_pp.subtract(TensorProduct.of(qn, qn)); // m(x) = x - <x,p>p - <x,qn>qn
    }

    @Override // from TensorNorm
    public Scalar norm(Tensor r) {
      return ArcSin.FUNCTION.apply(Clips.unit().apply(Vector2Norm.of(m.dot(r))));
    }
  }
}