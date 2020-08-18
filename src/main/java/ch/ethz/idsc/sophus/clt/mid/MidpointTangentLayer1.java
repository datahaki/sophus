// code by jph
package ch.ethz.idsc.sophus.clt.mid;

import ch.ethz.idsc.sophus.math.ScalarBinaryOperator;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;

/** valid in
 * 0.0 <= s1 <= pi
 * 2.5 <= s2 <= 5.0
 * 
 * function is odd in s1
 * function is even in s2 */
public enum MidpointTangentLayer1 implements ScalarBinaryOperator {
  INSTANCE;

  private static final Scalar F00 = RealScalar.of(+6.380368118368151);
  // ---
  private static final Scalar F10 = RealScalar.of(-0.9495367964333498);
  private static final Scalar F01 = RealScalar.of(+0.585676617409449);
  // ---
  private static final Scalar F20 = RealScalar.of(+0.2288056599457352);
  private static final Scalar F11 = RealScalar.of(-0.1989917375748728);
  private static final Scalar F02 = RealScalar.of(-0.09719194392863043);
  // ---
  private static final Scalar F30 = RealScalar.of(-0.023484123050310758);
  private static final Scalar F21 = RealScalar.of(-0.06676169192831326);
  private static final Scalar F12 = RealScalar.of(+0.03164655983326101);
  private static final Scalar F03 = RealScalar.of(-0.008550670838006792);

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
