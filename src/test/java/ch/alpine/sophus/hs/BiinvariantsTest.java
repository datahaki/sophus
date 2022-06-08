// code by jph
package ch.alpine.sophus.hs;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;

class BiinvariantsTest {
  @ParameterizedTest
  @EnumSource(Biinvariants.class)
  public void testDistanceSequenceNullFail(Biinvariant biinvariant) {
    assertThrows(Exception.class, () -> biinvariant.distances(RnGroup.INSTANCE, null));
  }

  @ParameterizedTest
  @EnumSource(Biinvariants.class)
  public void testVarDistVariogramNullFail(Biinvariant biinvariant) {
    assertThrows(Exception.class, () -> biinvariant.var_dist(RnGroup.INSTANCE, null, Tensors.empty()));
  }

  @ParameterizedTest
  @EnumSource(Biinvariants.class)
  public void testWeightingVariogramNullFail(Biinvariant biinvariant) {
    assertThrows(Exception.class, () -> biinvariant.weighting(RnGroup.INSTANCE, null, Tensors.empty()));
  }

  @ParameterizedTest
  @EnumSource(Biinvariants.class)
  public void testCoordinateVariogramNullFail(Biinvariant biinvariant) {
    assertThrows(Exception.class, () -> biinvariant.coordinate(RnGroup.INSTANCE, null, Tensors.empty()));
  }

  @ParameterizedTest
  @EnumSource(Biinvariants.class)
  public void testCoordinateSequenceNullFail(Biinvariant biinvariant) {
    assertThrows(Exception.class, () -> biinvariant.coordinate(RnGroup.INSTANCE, InversePowerVariogram.of(2), null));
  }

  @ParameterizedTest
  @EnumSource(Biinvariants.class)
  public void testSerializationFail(Biinvariant biinvariant) throws ClassNotFoundException, IOException {
    // in earlier versions, the instance used to be non-serializable
    Serialization.copy(biinvariant.coordinate(RnGroup.INSTANCE, InversePowerVariogram.of(2), Tensors.empty()));
  }
}
