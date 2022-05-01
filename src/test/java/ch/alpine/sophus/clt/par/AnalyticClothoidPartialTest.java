// code by jph
package ch.alpine.sophus.clt.par;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.Tolerance;

class AnalyticClothoidPartialTest {
  @Test
  public void testSimple1() throws ClassNotFoundException, IOException {
    ClothoidPartial clothoidPartial = Serialization.copy(AnalyticClothoidPartial.of(RealScalar.of(1.3), RealScalar.of(-0.7), RealScalar.of(0.2)));
    Scalar result = clothoidPartial.il(RealScalar.of(0.3));
    Scalar expect = ComplexScalar.of(0.1082616101141145, 0.27929038997053457);
    Tolerance.CHOP.requireClose(result, expect);
  }

  @Test
  public void testSimple2() {
    ClothoidPartial clothoidPartial = AnalyticClothoidPartial.of(1.5, 0.3, -0.4);
    Scalar result = clothoidPartial.il(RealScalar.of(0.4));
    Scalar expect = ComplexScalar.of(0.012847600349171472, 0.39973677660732543);
    Tolerance.CHOP.requireClose(result, expect);
  }

  @Test
  public void testCircle() throws ClassNotFoundException, IOException {
    ClothoidPartial clothoidPartial = Serialization.copy(AnalyticClothoidPartial.of(1.3, -0.7, 0));
    Scalar result = clothoidPartial.il(RealScalar.of(0.3));
    Scalar expect = ComplexScalar.of(0.10990181566815083, 0.2785521975010192);
    Tolerance.CHOP.requireClose(result, expect);
  }

  @Test
  public void testLine() throws ClassNotFoundException, IOException {
    ClothoidPartial clothoidPartial = Serialization.copy(AnalyticClothoidPartial.of(1.3, 0, 0));
    Scalar result = clothoidPartial.il(RealScalar.of(0.3));
    Scalar expect = ComplexScalar.of(0.0802496485873762, 0.2890674556251579);
    Tolerance.CHOP.requireClose(result, expect);
  }
}
