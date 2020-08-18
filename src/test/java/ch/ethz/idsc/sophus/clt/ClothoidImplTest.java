// code by jph
package ch.ethz.idsc.sophus.clt;

import java.lang.reflect.Modifier;

import ch.ethz.idsc.sophus.clt.par.AnalyticClothoidIntegral;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.opt.Pi;
import junit.framework.TestCase;

public class ClothoidImplTest extends TestCase {
  public void testSimple() {
    LagrangeQuadratic lagrangeQuadratic = LagrangeQuadratic.interp(Pi.HALF, Pi.TWO, Pi.VALUE);
    try {
      new ClothoidImpl( //
          null, //
          lagrangeQuadratic, //
          AnalyticClothoidIntegral.of(lagrangeQuadratic), //
          Tensors.vector(1, 2));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testFinal() {
    assertTrue(Modifier.isFinal(ClothoidImpl.class.getModifiers()));
  }
}
