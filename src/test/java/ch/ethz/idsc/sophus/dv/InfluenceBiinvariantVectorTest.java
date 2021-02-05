// code by jph
package ch.ethz.idsc.sophus.dv;

import java.io.IOException;
import java.io.Serializable;

import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.red.Frobenius;
import junit.framework.TestCase;

public class InfluenceBiinvariantVectorTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    InfluenceBiinvariantVector influenceBiinvariantVector = new InfluenceBiinvariantVector( //
        RnManifold.INSTANCE, Tensors.empty(), (TensorMetric & Serializable) (x, y) -> Frobenius.between(x, y));
    Serialization.copy(influenceBiinvariantVector);
  }
}
