// code by jph
package ch.ethz.idsc.sophus.clt;

import java.lang.reflect.Modifier;

import ch.ethz.idsc.sophus.clt.par.ClothoidIntegral;
import ch.ethz.idsc.sophus.clt.par.ClothoidIntegration;
import ch.ethz.idsc.sophus.clt.par.ClothoidIntegrations;
import ch.ethz.idsc.tensor.ComplexScalar;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.num.Pi;
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

  public void testProd() {
    Scalar z = ComplexScalar.of(2, 3);
    Scalar a = ComplexScalar.of(5, 11);
    Tensor vector = Tensors.vector(5, 11);
    Tensor tensor = ClothoidImpl.prod(z, vector);
    assertEquals(tensor, Tensors.vector(-23, 37));
    ExactTensorQ.require(tensor);
    Scalar compare = z.multiply(a);
    assertEquals(compare, ComplexScalar.of(-23, 37));
  }

  public void testFinal() {
    assertTrue(Modifier.isFinal(ClothoidImpl.class.getModifiers()));
  }
}
