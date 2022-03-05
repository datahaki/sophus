// code by jph
package ch.alpine.sophus.dv;

import java.io.IOException;
import java.io.Serializable;

import ch.alpine.sophus.api.TensorMetric;
import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.nrm.FrobeniusNorm;
import junit.framework.TestCase;

public class InfluenceBiinvariantVectorTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    InfluenceBiinvariantVector influenceBiinvariantVector = new InfluenceBiinvariantVector( //
        RnManifold.INSTANCE, Tensors.empty(), (TensorMetric & Serializable) (x, y) -> FrobeniusNorm.between(x, y));
    Serialization.copy(influenceBiinvariantVector);
  }
}
