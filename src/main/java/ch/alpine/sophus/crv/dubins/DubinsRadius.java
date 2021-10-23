// code by jph
package ch.alpine.sophus.crv.dubins;

import ch.alpine.sophus.math.ArcTan2D;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.Hypot;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.red.Min;
import ch.alpine.tensor.sca.Abs;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Cos;
import ch.alpine.tensor.sca.Mod;
import ch.alpine.tensor.sca.Sin;
import ch.alpine.tensor.sca.Sqrt;

public enum DubinsRadius {
  ;
  /** @param xya
   * @param type
   * @param clip
   * @return may return NaN */
  public static Scalar getMax(Tensor xya, DubinsType type, Clip clip) {
    xya = type.isFirstTurnRight() ? Se2Flip.FUNCTION.apply(xya) : xya;
    Scalar hypot = Hypot.ofVector(xya.extract(0, 2));
    Scalar turn1 = ArcTan2D.of(xya.extract(0, 2));
    Scalar angle = Mod.function(Pi.TWO).apply(xya.Get(2));
    if (type.containsStraight())
      return type.isFirstEqualsLast() //
          ? getMax2turnsSameSide(hypot, turn1, angle, clip.max()) // .type == LSL || type == RSR
          : getMax2turnsDiffSide(hypot, turn1, angle, clip.max()); // type == LSR || type == RSL
    return getMaxRadius3turns(hypot, turn1, angle, clip.min());
  }

  private static Scalar getMax2turnsDiffSide(Scalar hypot, Scalar turn1, Scalar angle, Scalar rMax) {
    Scalar turn2 = turn1.subtract(angle);
    Scalar sbeta = Cos.FUNCTION.apply(turn1).add(Cos.FUNCTION.apply(turn2)).multiply(RationalScalar.HALF);
    Scalar val = Sqrt.FUNCTION.apply(RealScalar.ONE.subtract(sbeta.multiply(sbeta)));
    Scalar aux = Sin.FUNCTION.apply(turn1).add(val.add(val)).add(Sin.FUNCTION.apply(turn2));
    return Scalars.lessThan(rMax.multiply(aux), hypot) //
        ? rMax
        : hypot.divide(aux);
  }

  private static Scalar getMax2turnsSameSide(Scalar hypot, Scalar turn1, Scalar angle, Scalar rMax) {
    Scalar turn2 = angle.subtract(turn1);
    Scalar sturn1 = Abs.FUNCTION.apply(Sin.FUNCTION.apply(turn1));
    Scalar sturn2 = Abs.FUNCTION.apply(Sin.FUNCTION.apply(turn2));
    Scalar sturn = Sin.FUNCTION.apply(angle.multiply(RationalScalar.HALF));
    Scalar aux = sturn.multiply(sturn).multiply(RealScalar.TWO);
    Scalar ds = Min.of(sturn1, sturn2).multiply(hypot);
    return Scalars.lessThan(rMax.multiply(aux), ds) //
        ? rMax
        : ds.divide(aux);
  }

  private static Scalar getMaxRadius3turns(Scalar hypot, Scalar turn1, Scalar angle, Scalar rbnd) {
    return rbnd;
  }
}
