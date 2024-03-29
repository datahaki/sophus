// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.sophus.decim.LineDistance;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.math.api.TensorNorm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

/** distance between line spanned by p and q on S^n and point r on S^n */
public enum HnLineDistance implements LineDistance {
  INSTANCE;

  @Override // from LineDistance
  public TensorNorm tensorNorm(Tensor p, Tensor q) {
    return new Inner(p, q);
  }

  private static class Inner implements TensorNorm {
    private final Exponential exponential;
    private final Tensor v_pq;

    public Inner(Tensor p, Tensor q) {
      exponential = new HnExponential(p);
      v_pq = HnVectorNorm.NORMALIZE.apply(exponential.log(q));
    }

    @Override // from TensorNorm
    public Scalar norm(Tensor r) {
      Tensor v_pr = exponential.log(r);
      // TODO SOPHUS ALG not correct
      v_pr = v_pr.subtract(v_pq.multiply(LBilinearForm.between(v_pq, v_pr)));
      return HnVectorNorm.of(v_pr);
    }
  }
}
