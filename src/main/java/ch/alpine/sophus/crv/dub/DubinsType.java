package ch.alpine.sophus.crv.dub;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.PackageTestAccess;
import ch.alpine.tensor.sca.Abs;
import ch.alpine.tensor.sca.Sign;

public enum DubinsType {
  LSR(+1, +0, -1, Steer2TurnsDiffSide.INSTANCE), //
  RSL(-1, +0, +1, Steer2TurnsDiffSide.INSTANCE), //
  LSL(+1, +0, +1, Steer2TurnsSameSide.INSTANCE), //
  RSR(-1, +0, -1, Steer2TurnsSameSide.INSTANCE), //
  LRL(+1, -1, +1, Steer3Turns.INSTANCE), //
  RLR(-1, +1, -1, Steer3Turns.INSTANCE);

  private final Tensor signature;
  private final Tensor signatureAbs;
  private final boolean isFirstTurnRight;
  private final boolean isFirstEqualsLast;
  private final boolean containsStraight;
  private final DubinsSteer dubinsSteer;

  private DubinsType(int s0s, int s1s, int s2s, DubinsSteer dubinsSteer) {
    signature = Tensors.vector(s0s, s1s, s2s).unmodifiable();
    signatureAbs = signature.map(Abs.FUNCTION).unmodifiable();
    isFirstTurnRight = s0s == -1;
    isFirstEqualsLast = s0s == s2s;
    containsStraight = s1s == 0;
    this.dubinsSteer = dubinsSteer;
  }

  /** @return true if type is RSL or RSR or RLR */
  public boolean isFirstTurnRight() {
    return isFirstTurnRight;
  }

  /** @return true if type is LSL or RSR or LRL or RLR */
  public boolean isFirstEqualsLast() {
    return isFirstEqualsLast;
  }

  public Tensor signatureAbs() {
    return signatureAbs;
  }

  public boolean containsStraight() {
    return containsStraight;
  }

  /* package */ DubinsSteer dubinsSteer() {
    return dubinsSteer;
  }

  /** @param index 0, 1, or 2
   * @param radius positive
   * @return vector with first and second entry unitless.
   * result is multiplied with length of segment */
  @PackageTestAccess
  Tensor tangent(int index, Scalar radius) {
    return Tensors.of(RealScalar.ONE, RealScalar.ZERO, //
        signature.Get(index).divide(Sign.requirePositive(radius)));
  }
}