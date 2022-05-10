// code by jph
package ch.alpine.sophus.dv;

import java.io.IOException;
import java.io.Serializable;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.TensorMetric;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.nrm.FrobeniusNorm;

class InfluenceBiinvariantVectorTest {
  @Test
  public void testSimple() throws ClassNotFoundException, IOException {
    InfluenceBiinvariantVector influenceBiinvariantVector = new InfluenceBiinvariantVector( //
        RnGroup.INSTANCE, Tensors.empty(), (TensorMetric & Serializable) (x, y) -> FrobeniusNorm.between(x, y));
    Serialization.copy(influenceBiinvariantVector);
  }
}
