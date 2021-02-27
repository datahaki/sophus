// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import java.io.Serializable;

import ch.ethz.idsc.sophus.decim.LineDistance;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** distance between line spanned by p and q on S^n and point r on S^n */
public enum HnLineDistance implements LineDistance {
  INSTANCE;

  @Override // from LineDistance
  public TensorNorm tensorNorm(Tensor p, Tensor q) {
    return new Inner(p, q);
  }

  private class Inner implements TensorNorm, Serializable {
    private final Exponential exponential;
    private final Tensor v_pq;

    public Inner(Tensor p, Tensor q) {
      exponential = new HnExponential(p);
      v_pq = HnVectorNorm.NORMALIZE.apply(exponential.log(q));
    }

    @Override // from TensorNorm
    public Scalar norm(Tensor r) {
      Tensor v_pr = exponential.log(r);
      // TODO not correct
      v_pr = v_pr.subtract(v_pq.multiply(LBilinearForm.between(v_pq, v_pr)));
      return HnVectorNorm.of(v_pr);
    }
  }
}