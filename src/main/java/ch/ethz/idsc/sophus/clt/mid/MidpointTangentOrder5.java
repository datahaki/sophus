// code by jph
package ch.ethz.idsc.sophus.clt.mid;

import ch.ethz.idsc.sophus.math.ScalarBinaryOperator;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;

/** valid in
 * 0.0 <= s1 <= pi
 * 0.0 <= s2 <= pi
 * 
 * function is odd in s1
 * function is even in s2 */
/* package */ enum MidpointTangentOrder5 implements ScalarBinaryOperator {
  INSTANCE;

  private static final Scalar F10 = RealScalar.of(-1.5006205701038964);
  private static final Scalar F30 = RealScalar.of(+0.020348415382503517);
  private static final Scalar F50 = RealScalar.of(-0.0005328054077587399);
  private static final Scalar F70 = RealScalar.of(-0.000010474446207605217);
  // ---
  private static final Scalar F12 = RealScalar.of(+0.09257648911893812);
  private static final Scalar F14 = RealScalar.of(+0.003922452795500254);
  private static final Scalar F16 = RealScalar.of(+0.00031770814964551277);
  // ---
  private static final Scalar F32 = RealScalar.of(-0.00453786314493132);
  private static final Scalar F34 = RealScalar.of(-0.0011608780624864858);
  // ---
  private static final Scalar F52 = RealScalar.of(+0.0004860300460672423);

  @Override
  public Scalar apply(Scalar s1, Scalar s2) {
    Scalar p10 = s1;
    Scalar p01 = s2;
    // ---
    Scalar p20 = p10.multiply(p10);
    Scalar p30 = p20.multiply(p10);
    Scalar p50 = p30.multiply(p20);
    Scalar p70 = p50.multiply(p20);
    // ---
    Scalar p02 = p01.multiply(p01);
    Scalar p04 = p02.multiply(p02);
    Scalar p06 = p04.multiply(p02);
    // ---
    Scalar p12 = p10.multiply(p02);
    Scalar p14 = p10.multiply(p04);
    Scalar p16 = p10.multiply(p06);
    // ---
    Scalar p32 = p30.multiply(p02);
    Scalar p34 = p30.multiply(p04);
    // ---
    Scalar p52 = p50.multiply(p02);
    return F10.multiply(p10) //
        .add(F30.multiply(p30)) //
        .add(F50.multiply(p50)) //
        .add(F70.multiply(p70)) //
        // ---
        .add(F12.multiply(p12)) //
        .add(F14.multiply(p14)) //
        .add(F16.multiply(p16)) //
        // ---
        .add(F32.multiply(p32)) //
        .add(F34.multiply(p34)) //
        // ---
        .add(F52.multiply(p52)) //
    ;
  }
}
