// code by jph
package ch.alpine.sophus.dv;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.sophus.lie.se2.Se2RandomSample;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.mat.SquareMatrixQ;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class GardenBiinvariantTest {
  @Test
  void testSymmetric() {
    Tensor sequence = RandomSample.of(Se2RandomSample.of(NormalDistribution.standard()), 20);
    Sedarim sedarim = Biinvariants.GARDEN.of(Se2CoveringGroup.INSTANCE).distances(sequence);
    Tensor matrix = Tensor.of(sequence.stream().map(sedarim::sunder));
    SquareMatrixQ.require(matrix);
    Tensor diff = Transpose.of(matrix).subtract(matrix);
    assertFalse(Chop._04.allZero(diff));
  }

  @Test
  void testNonPublic() {
    assertFalse(Modifier.isPublic(GardenBiinvariant.class.getModifiers()));
  }
}
