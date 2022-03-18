// code by jph
package ch.alpine.sophus.hs;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;

public class BiinvariantsTest {
  @Test
  public void testDistanceSequenceNullFail() {
    for (Biinvariant biinvariant : Biinvariants.values())
      try {
        biinvariant.distances(RnManifold.INSTANCE, null);
        System.out.println(biinvariant);
        fail();
      } catch (Exception exception) {
        // ---
      }
  }

  @Test
  public void testVarDistVariogramNullFail() {
    for (Biinvariant biinvariant : Biinvariants.values())
      try {
        biinvariant.var_dist(RnManifold.INSTANCE, null, Tensors.empty());
        System.out.println(biinvariant);
        fail();
      } catch (Exception exception) {
        // ---
      }
  }

  @Test
  public void testWeightingVariogramNullFail() {
    for (Biinvariant biinvariant : Biinvariants.values())
      try {
        biinvariant.weighting(RnManifold.INSTANCE, null, Tensors.empty());
        System.out.println(biinvariant);
        fail();
      } catch (Exception exception) {
        // ---
      }
  }

  @Test
  public void testCoordinateVariogramNullFail() {
    for (Biinvariant biinvariant : Biinvariants.values())
      try {
        biinvariant.coordinate(RnManifold.INSTANCE, null, Tensors.empty());
        System.out.println(biinvariant);
        fail();
      } catch (Exception exception) {
        // ---
      }
  }

  @Test
  public void testCoordinateSequenceNullFail() {
    for (Biinvariant biinvariant : Biinvariants.values())
      try {
        biinvariant.coordinate(RnManifold.INSTANCE, InversePowerVariogram.of(2), null);
        System.out.println(biinvariant);
        fail();
      } catch (Exception exception) {
        // ---
      }
  }

  @Test
  public void testSerializationFail() {
    for (Biinvariant biinvariant : Biinvariants.values())
      try {
        Serialization.copy(biinvariant.coordinate(RnManifold.INSTANCE, InversePowerVariogram.of(2), Tensors.empty()));
      } catch (Exception exception) {
        System.out.println(biinvariant);
        fail();
      }
  }
}
