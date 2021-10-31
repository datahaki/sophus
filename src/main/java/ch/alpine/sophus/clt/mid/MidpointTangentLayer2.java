// code by jph
package ch.alpine.sophus.clt.mid;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.api.ScalarBinaryOperator;

/** valid in
 * 0.0 <= s1 <= pi
 * 5.0 <= s2 <=
 * 
 * function is odd in s1
 * function is even in s2 */
/* package */ enum MidpointTangentLayer2 implements ScalarBinaryOperator {
  INSTANCE;

  private static final Scalar F00 = RealScalar.of(-8.718824550021568);
  // ---
  private static final Scalar F10 = RealScalar.of(+0.41886037239611823);
  private static final Scalar F01 = RealScalar.of(+0.6289477508471536);
  // ---
  private static final Scalar F20 = RealScalar.of(+0.4952725757372764);
  private static final Scalar F11 = RealScalar.of(-0.6480979519274961);
  private static final Scalar F02 = RealScalar.of(-0.05557499460091992);
  // ---
  private static final Scalar F30 = RealScalar.of(+0.06218754870627631);
  private static final Scalar F21 = RealScalar.of(-0.0960073086685556);
  private static final Scalar F12 = RealScalar.of(+0.04696893706813651);
  private static final Scalar F03 = RealScalar.of(+0.009993452969666621);

  @Override
  public Scalar apply(Scalar s1, Scalar s2) {
    Scalar p10 = s1;
    Scalar p01 = s2;
    // ---
    Scalar p20 = p10.multiply(p10);
    Scalar p11 = p10.multiply(p01);
    Scalar p02 = p01.multiply(p01);
    // ---
    Scalar p30 = p20.multiply(p10);
    Scalar p21 = p20.multiply(p01);
    Scalar p12 = p10.multiply(p02);
    Scalar p03 = p01.multiply(p02);
    // ---
    return F00 //
        .add(F10.multiply(p10)) //
        .add(F01.multiply(p01)) //
        // ---
        .add(F20.multiply(p20)) //
        .add(F11.multiply(p11)) //
        .add(F02.multiply(p02)) //
        // ---
        .add(F30.multiply(p30)) //
        .add(F21.multiply(p21)) //
        .add(F12.multiply(p12)) //
        .add(F03.multiply(p03)) //
    ;
  }
}
