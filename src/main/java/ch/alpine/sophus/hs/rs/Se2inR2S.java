// code by jph
package ch.alpine.sophus.hs.rs;

import ch.alpine.sophus.api.SplitInterface;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.lie.r2.AngleVector;
import ch.alpine.tensor.red.VectorAngle;

public enum Se2inR2S implements SplitInterface {
  METHOD_0(RnSBezierSplit.METHOD_0), //
  METHOD_1(RnSBezierSplit.METHOD_1), //
  ;

  private final RnSBezierSplit rnSBezierSplit;

  private Se2inR2S(RnSBezierSplit rnSBezierSplit) {
    this.rnSBezierSplit = rnSBezierSplit;
  }

  @Override
  public Tensor split(Tensor p, Tensor q, Scalar scalar) {
    Tensor pv0 = Tensors.of(p.extract(0, 2), AngleVector.of(p.Get(2)));
    Tensor pv1 = Tensors.of(q.extract(0, 2), AngleVector.of(q.Get(2)));
    Tensor split = rnSBezierSplit.split(pv0, pv1, scalar);
    return split.get(0).append(VectorAngle.of(UnitVector.of(2, 0), split.get(1)).orElseThrow());
  }
}
