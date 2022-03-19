// code by jph
package ch.alpine.sophus.clt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.clt.par.ClothoidIntegral;
import ch.alpine.sophus.clt.par.ClothoidIntegration;
import ch.alpine.sophus.clt.par.ClothoidIntegrations;
import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.num.Pi;

public class ClothoidImplTest {
  @Test
  public void testSimple() {
    LagrangeQuadratic lagrangeQuadratic = LagrangeQuadratic.interp(Pi.HALF, Pi.TWO, Pi.VALUE);
    for (ClothoidIntegration clothoidIntegration : ClothoidIntegrations.values()) {
      ClothoidIntegral clothoidIntegral = clothoidIntegration.clothoidIntegral(lagrangeQuadratic);
      assertThrows(Exception.class, () -> new ClothoidImpl(null, lagrangeQuadratic, clothoidIntegral, Tensors.vector(1, 2)));
    }
  }

  @Test
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

  @Test
  public void testFinal() {
    assertTrue(Modifier.isFinal(ClothoidImpl.class.getModifiers()));
  }
}
