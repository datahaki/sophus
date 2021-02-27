// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.Serializable;

import ch.ethz.idsc.sophus.decim.LineDistance;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;

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
      // TODO not correct
      v_pr = v_pr.subtract(v_pq.multiply((Scalar) v_pq.dot(v_pr)));
      return Vector2Norm.of(v_pr);
    }
  }
}