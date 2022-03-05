// code by jph
package ch.alpine.sophus.hs.sn;

import java.io.Serializable;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.api.TensorNorm;
import ch.alpine.sophus.decim.LineDistance;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.Vector2Norm;

/** distance between line spanned by p and q on S^n and point r on S^n */
public enum SnLineDistanceAlt implements LineDistance {
  INSTANCE;

  @Override // from LineDistance
  public TensorNorm tensorNorm(Tensor p, Tensor q) {
    return new Inner(p, q);
  }

  private class Inner implements TensorNorm, Serializable {
    private final Exponential exponential;
    private final Tensor v_pq;

    public Inner(Tensor p, Tensor q) {
      exponential = new SnExponential(p);
      v_pq = Vector2Norm.NORMALIZE.apply(exponential.log(q));
    }

    @Override // from TensorNorm
    public Scalar norm(Tensor r) {
      Tensor v_pr = exponential.log(r);
      // TODO SOPHUS API not correct
      v_pr = v_pr.subtract(v_pq.multiply((Scalar) v_pq.dot(v_pr)));
      return Vector2Norm.of(v_pr);
    }
  }
}