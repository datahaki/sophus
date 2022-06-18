// code by jph
package ch.alpine.sophus.hs;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;

class BiinvariantsTest {
  @Test
  void testDistanceSequenceNullFail() {
    for (Biinvariant biinvariant : Biinvariants.all(RnGroup.INSTANCE).values())
      assertThrows(Exception.class, () -> biinvariant.distances(null));
  }

  @Test
  void testVarDistVariogramNullFail() {
    for (Biinvariant biinvariant : Biinvariants.all(RnGroup.INSTANCE).values())
      assertThrows(Exception.class, () -> biinvariant.var_dist(null, Tensors.empty()));
  }

  @Test
  void testWeightingVariogramNullFail() {
    for (Biinvariant biinvariant : Biinvariants.all(RnGroup.INSTANCE).values())
      assertThrows(Exception.class, () -> biinvariant.weighting(null, Tensors.empty()));
  }

  @Test
  void testCoordinateVariogramNullFail() {
    for (Biinvariant biinvariant : Biinvariants.all(RnGroup.INSTANCE).values())
      assertThrows(Exception.class, () -> biinvariant.coordinate(null, Tensors.empty()));
  }

  @Test
  void testCoordinateSequenceNullFail() {
    for (Biinvariant biinvariant : Biinvariants.all(RnGroup.INSTANCE).values())
      assertThrows(Exception.class, () -> biinvariant.coordinate(InversePowerVariogram.of(2), null));
  }

  @Test
  void testSerializationFail() throws ClassNotFoundException, IOException {
    // in earlier versions, the instance used to be non-serializable
    for (Biinvariant biinvariant : Biinvariants.all(RnGroup.INSTANCE).values())
      Serialization.copy(biinvariant.coordinate(InversePowerVariogram.of(2), Tensors.empty()));
  }
}
