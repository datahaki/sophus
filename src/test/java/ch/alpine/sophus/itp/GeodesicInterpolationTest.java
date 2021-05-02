// code by jph
package ch.alpine.sophus.itp;

import java.io.IOException;

import ch.alpine.sophus.lie.se2c.Se2CoveringGeodesic;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.itp.Interpolation;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class GeodesicInterpolationTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    Tensor sequence = Tensors.fromString("{{1, 2, 3}, {4, 3, 2}, {5, -1, 2.5}}");
    Interpolation interpolation = //
        Serialization.copy(GeodesicInterpolation.of(Se2CoveringGeodesic.INSTANCE, sequence));
    Chop._12.requireClose( //
        interpolation.at(RealScalar.of(1.2)), //
        Se2CoveringGeodesic.INSTANCE.split(sequence.get(1), sequence.get(2), RealScalar.of(0.2)));
    Chop._12.requireClose( //
        interpolation.at(RealScalar.of(0)), //
        sequence.get(0));
    Chop._12.requireClose( //
        interpolation.at(RealScalar.of(2)), //
        sequence.get(2));
    AssertFail.of(() -> interpolation.at(RealScalar.of(-0.01)));
    AssertFail.of(() -> interpolation.at(RealScalar.of(2.01)));
    AssertFail.of(() -> interpolation.get(RealScalar.of(0)));
    AssertFail.of(() -> interpolation.get(Tensors.vector(0)));
  }
}
