// code by jph
package ch.alpine.sophus.hs.rs;

import ch.alpine.sophus.lie.so2.ArcTan2D;
import ch.alpine.sophus.math.api.GeodesicSpace;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.lie.rot.AngleVector;

public enum Se2inR2S implements GeodesicSpace {
  METHOD_0(RnSBezierSplit.METHOD_0), //
  METHOD_1(RnSBezierSplit.METHOD_1), //
  ;

  private final RnSBezierSplit rnSBezierSplit;

  Se2inR2S(RnSBezierSplit rnSBezierSplit) {
    this.rnSBezierSplit = rnSBezierSplit;
  }

  @Override
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    Tensor pv0 = Tensors.of(p.extract(0, 2), AngleVector.of(p.Get(2)));
    Tensor pv1 = Tensors.of(q.extract(0, 2), AngleVector.of(q.Get(2)));
    return scalar -> {
      Tensor split = rnSBezierSplit.split(pv0, pv1, scalar);
      return split.get(0).append(ArcTan2D.of(split.get(1)));
    };
  }
}
