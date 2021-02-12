// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.ext.Serialization;
import junit.framework.TestCase;

public class BiinvariantsTest extends TestCase {
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
