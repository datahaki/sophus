// code by jph
package ch.alpine.sophus.crv.clt.mid;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.api.ScalarBinaryOperator;

/** valid in
 * 0.0 <= s1 <= pi
 * 0.0 <= s2 <= pi
 * 
 * function is odd in s1
 * function is even in s2 */
/* package */ enum MidpointTangentOrder4 implements ScalarBinaryOperator {
  INSTANCE;

  private static final Scalar F10 = RealScalar.of(-1.5132991172677746);
  private static final Scalar F30 = RealScalar.of(+0.025958842855190786);
  private static final Scalar F50 = RealScalar.of(-0.0016715415881496136);
  private static final Scalar F70 = RealScalar.of(+0.00007142487216111225);
  // ---
  private static final Scalar F12 = RealScalar.of(+0.10442930473762928);
  private static final Scalar F14 = RealScalar.of(+0.0014895792174595167);
  private static final Scalar F16 = RealScalar.of(+0.00042434570303820097);
  // ---
  private static final Scalar F32 = RealScalar.of(-0.005501332153052616);
  private static final Scalar F34 = RealScalar.of(-0.0009630817093057504);
  // ---
  private static final Scalar F52 = RealScalar.of(+0.0004176683497549008);

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
