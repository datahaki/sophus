// code by jph
package ch.alpine.sophus.ref.d1h;

import java.io.IOException;
import java.io.Serializable;
import java.util.function.Function;

import ch.alpine.sophus.lie.LieTransport;
import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import junit.framework.TestCase;

public class Hermite1SubdivisionsTest extends TestCase {
  public void testSimple() {
    TestHelper.check( //
        RnHermite1Subdivisions.instance(), //
        Hermite1Subdivisions.standard(RnManifold.INSTANCE, LieTransport.INSTANCE));
  }

  public void testParams() {
    Scalar lambda = RationalScalar.of(-1, 16);
    Scalar mu = RationalScalar.of(-1, 3);
    TestHelper.check( //
        RnHermite1Subdivisions.of(lambda, mu), //
        Hermite1Subdivisions.of(RnManifold.INSTANCE, LieTransport.INSTANCE, lambda, mu));
  }

  public void testSerializableCast() throws ClassNotFoundException, IOException {
    @SuppressWarnings("unchecked")
    Function<Integer, Tensor> function = (Function<Integer, Tensor> & Serializable) i -> Tensors.empty();
    assertEquals(Serialization.copy(function).apply(3), Tensors.empty());
  }
}
