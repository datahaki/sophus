// code by jph
package ch.alpine.sophus.itp;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.itp.Interpolation;
import ch.alpine.tensor.sca.Chop;

class GeodesicInterpolationTest {
  @Test
  public void testSimple() throws ClassNotFoundException, IOException {
    Tensor sequence = Tensors.fromString("{{1, 2, 3}, {4, 3, 2}, {5, -1, 2.5}}");
    Interpolation interpolation = //
        Serialization.copy(GeodesicInterpolation.of(Se2CoveringGroup.INSTANCE, sequence));
    Chop._12.requireClose( //
        interpolation.at(RealScalar.of(1.2)), //
        Se2CoveringGroup.INSTANCE.split(sequence.get(1), sequence.get(2), RealScalar.of(0.2)));
    Chop._12.requireClose( //
        interpolation.at(RealScalar.of(0)), //
        sequence.get(0));
    Chop._12.requireClose( //
        interpolation.at(RealScalar.of(2)), //
        sequence.get(2));
    assertThrows(Exception.class, () -> interpolation.at(RealScalar.of(-0.01)));
    assertThrows(Exception.class, () -> interpolation.at(RealScalar.of(2.01)));
    assertThrows(Exception.class, () -> interpolation.get(RealScalar.of(0)));
    assertThrows(Exception.class, () -> interpolation.get(Tensors.vector(0)));
  }
}
