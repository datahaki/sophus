// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.Serializable;

import ch.ethz.idsc.sophus.crv.decim.LineDistance;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.lie.TensorProduct;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.red.Hypot;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.ArcSin;
import ch.ethz.idsc.tensor.sca.Clips;

/** distance between line spanned by p and q on S^n and point r on S^n */
public enum SnLineDistance implements LineDistance {
  INSTANCE;

  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Norm._2);

  @Override // from LineDistance
  public TensorNorm tensorNorm(Tensor p, Tensor q) {
    return new SnLine(p, q);
  }

  private class SnLine implements TensorNorm, Serializable {
    private static final long serialVersionUID = 5186443472462114943L;
    private final Tensor m;

    public SnLine(Tensor p, Tensor q) {
      Tensor id_pp = IdentityMatrix.of(p.length()).subtract(TensorProduct.of(p, p));
      Tensor qn = NORMALIZE.apply(id_pp.dot(q));
      Tensor Q = TensorProduct.of(qn, qn);
      m = id_pp.subtract(id_pp.dot(Q));
    }

    @Override // from TensorNorm
    public Scalar norm(Tensor r) {
      return ArcSin.FUNCTION.apply(Clips.unit().apply(Hypot.ofVector(m.dot(r))));
    }
  }
}