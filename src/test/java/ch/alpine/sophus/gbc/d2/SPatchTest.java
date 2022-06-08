// code by jph
package ch.alpine.sophus.gbc.d2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.Genesis;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.red.Total;

class SPatchTest {
  @Test
  void test() {
    int n = 5;
    Genesis genesis = ThreePointCoordinate.of(Barycenter.MEAN_VALUE);
    SPatch sPatch = SPatch.of(n, genesis, 2);
    Tensor ls = sPatch.getLs();
    assertEquals(ls.length(), 15);
    sPatch.basis(Tensors.vector(2, 1));
    assertEquals(sPatch.basis(Tensors.vector(0, 0)), 0);
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < n; ++j) {
        Tensor rep = Tensors.vector(i, j);
        int basis = sPatch.basis(rep);
        Integers.requirePositiveOrZero(basis);
        // System.out.println(sPatch.embed(rep));
      }
    }
    for (Tensor xy : RandomVariate.of(UniformDistribution.of(-1, 1), 40, 2)) {
      Tensor weights = sPatch.apply(xy);
      Tolerance.CHOP.requireClose(Total.ofVector(weights), RealScalar.ONE);
      Tensor embed = sPatch.getEmbed();
      Tolerance.CHOP.requireClose(weights.dot(embed), xy);
    }
  }
}
