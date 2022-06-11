// code by jph
// adapted from https://github.com/AtsushiSakai/PythonRobotics
package ch.alpine.sophus.crv.dub;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.red.Min;
import ch.alpine.tensor.sca.Abs;
import ch.alpine.tensor.sca.pow.Sqrt;
import ch.alpine.tensor.sca.tri.Cos;
import ch.alpine.tensor.sca.tri.Sin;

/* package */ enum FlexiTurns {
  ;
  public static Scalar maxRadius2TurnsDiffSide(Scalar dist_tr, Scalar th_tr, Scalar th_total, Scalar radius) {
    Scalar turn = th_total.subtract(th_tr);
    Scalar beta = Cos.FUNCTION.apply(th_tr).add(Cos.FUNCTION.apply(turn)).multiply(RealScalar.of(0.5));
    Scalar term = Sqrt.FUNCTION.apply(RealScalar.ONE.subtract(beta.multiply(beta)));
    Scalar aux = Sin.FUNCTION.apply(th_tr).add(term).add(term).add(Sin.FUNCTION.apply(turn));
    if (Scalars.lessThan(radius.multiply(aux), dist_tr))
      return radius;
    return dist_tr.divide(aux);
  }

  public static Scalar maxRadius2TurnsSameSide(Scalar dist_tr, Scalar th_tr, Scalar th_total, Scalar radius) {
    Scalar turn = th_total.subtract(th_tr);
    Scalar turn1 = Abs.FUNCTION.apply(Sin.FUNCTION.apply(th_tr));
    Scalar turn2 = Abs.FUNCTION.apply(Sin.FUNCTION.apply(turn));
    // since turn \in [0,2pi), then turn/2 \in [0,pi) -> 0 <= sturn;
    Scalar sturn = Sin.FUNCTION.apply(th_total.multiply(RealScalar.of(0.5)));
    Scalar aux = sturn.multiply(sturn).multiply(RealScalar.of(2));
    Scalar ds = Min.of(turn1, turn2).multiply(dist_tr);
    if (Scalars.lessThan(radius.multiply(aux), ds))
      return radius;
    return ds.divide(aux);
  }

  public static Scalar maxRadius3Turns(Scalar radius) {
    return radius;
  }
}
