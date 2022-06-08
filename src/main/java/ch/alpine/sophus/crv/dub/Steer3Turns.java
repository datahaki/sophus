// code by jph
// adapted from https://github.com/mcapino/trajectorytools
// adapted from https://github.com/AtsushiSakai/PythonRobotics
package ch.alpine.sophus.crv.dub;

import java.util.Optional;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.sca.tri.ArcCos;

/* package */ enum Steer3Turns implements DubinsSteer {
  INSTANCE;

  private static final Scalar FOUR = RealScalar.of(4.0);

  @Override // from DubinsSteer
  public Optional<Tensor> steer(Scalar dist_tr, Scalar th_tr, Scalar th_total, Scalar radius) {
    Scalar aux = dist_tr.divide(FOUR).divide(radius);
    if (StaticHelper.greaterThanOne(aux))
      return Optional.empty();
    Scalar th_aux = ArcCos.FUNCTION.apply(aux);
    Scalar th_pha = Pi.HALF.add(th_aux);
    return Optional.of(Tensors.of( //
        StaticHelper.principalValue(th_tr.add(th_pha)), //
        Pi.VALUE.add(th_aux).add(th_aux), //
        StaticHelper.principalValue(th_total.subtract(th_tr).add(th_pha))).multiply(radius));
  }
}
