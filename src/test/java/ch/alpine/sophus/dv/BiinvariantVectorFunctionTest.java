// code by jph
package ch.alpine.sophus.dv;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.math.api.TensorMetric;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.nrm.FrobeniusNorm;

class BiinvariantVectorFunctionTest {
  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    BiinvariantVectorFunction influenceBiinvariantVector = new BiinvariantVectorFunction( //
        new HsDesign(RnGroup.INSTANCE), Tensors.empty(), (TensorMetric & Serializable) (x, y) -> FrobeniusNorm.between(x, y));
    Serialization.copy(influenceBiinvariantVector);
  }

  @Test
  void testNonPublic() {
    assertFalse(Modifier.isPublic(BiinvariantVectorFunction.class.getModifiers()));
  }
}
