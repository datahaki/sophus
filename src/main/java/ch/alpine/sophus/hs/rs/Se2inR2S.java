// code by jph
package ch.alpine.sophus.hs.rs;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.sophus.hs.r2.ArcTan2D;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.lie.r2.AngleVector;

public enum Se2inR2S implements GeodesicSpace {
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
    return split.get(0).append(ArcTan2D.of(split.get(1)));
  }

  @Override
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    // TODO SOPHUS IMPL this can be improved
    return t -> split(p, q, t);
  }
}
