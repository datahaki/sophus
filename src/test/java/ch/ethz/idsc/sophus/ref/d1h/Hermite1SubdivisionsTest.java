// code by jph
package ch.ethz.idsc.sophus.ref.d1h;

import java.io.IOException;
import java.io.Serializable;
import java.util.function.Function;

import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.lie.rn.RnTransport;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.io.Serialization;
import junit.framework.TestCase;

public class Hermite1SubdivisionsTest extends TestCase {
  public void testSimple() {
    TestHelper.check( //
        RnHermite1Subdivisions.instance(), //
        Hermite1Subdivisions.standard(RnManifold.HS_EXP, RnTransport.INSTANCE));
  }

  public void testParams() {
    Scalar lambda = RationalScalar.of(-1, 16);
    Scalar mu = RationalScalar.of(-1, 3);
    TestHelper.check( //
        RnHermite1Subdivisions.of(lambda, mu), //
        Hermite1Subdivisions.of(RnManifold.HS_EXP, RnTransport.INSTANCE, lambda, mu));
  }

  public void testSerializableCast() throws ClassNotFoundException, IOException {
    @SuppressWarnings("unchecked")
    Function<Integer, Tensor> function = (Function<Integer, Tensor> & Serializable) i -> Tensors.empty();
    assertEquals(Serialization.copy(function).apply(3), Tensors.empty());
  }
}
