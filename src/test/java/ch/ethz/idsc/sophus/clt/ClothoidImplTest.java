// code by jph
package ch.ethz.idsc.sophus.clt;

import java.lang.reflect.Modifier;

import ch.ethz.idsc.sophus.clt.par.ClothoidIntegral;
import ch.ethz.idsc.sophus.clt.par.ClothoidIntegration;
import ch.ethz.idsc.sophus.clt.par.ClothoidIntegrations;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.opt.Pi;
import junit.framework.TestCase;

public class ClothoidImplTest extends TestCase {
  public void testSimple() {
    LagrangeQuadratic lagrangeQuadratic = LagrangeQuadratic.interp(Pi.HALF, Pi.TWO, Pi.VALUE);
    for (ClothoidIntegration clothoidIntegration : ClothoidIntegrations.values()) {
      ClothoidIntegral clothoidIntegral = clothoidIntegration.clothoidIntegral(lagrangeQuadratic);
      try {
        new ClothoidImpl( //
            null, //
            lagrangeQuadratic, //
            clothoidIntegral, //
            Tensors.vector(1, 2));
        fail();
      } catch (Exception exception) {
        // ---
      }
    }
  }

  public void testFinal() {
    assertTrue(Modifier.isFinal(ClothoidImpl.class.getModifiers()));
  }
}
