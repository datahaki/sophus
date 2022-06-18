// code by jph
package ch.alpine.sophus.dv;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.lang.reflect.Modifier;
import java.util.Map;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.sophus.lie.se2.Se2RandomSample;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.SquareMatrixQ;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.pdf.c.NormalDistribution;

class HarborBiinvariantTest {
  @Test
  void testSymmetric() {
    Tensor sequence = RandomSample.of(Se2RandomSample.of(NormalDistribution.standard()), 15);
    Map<Biinvariants, Biinvariant> map = Biinvariants.kriging(Se2CoveringGroup.INSTANCE);
    for (Biinvariant biinvariant : map.values()) {
      Sedarim sedarim = biinvariant.distances(sequence);
      Tensor matrix = Tensor.of(sequence.stream().map(sedarim::sunder));
      SquareMatrixQ.require(matrix);
      SymmetricMatrixQ.require(matrix);
    }
  }

  @Test
  void testNonPublic() {
    assertFalse(Modifier.isPublic(HarborBiinvariant.class.getModifiers()));
  }
}
