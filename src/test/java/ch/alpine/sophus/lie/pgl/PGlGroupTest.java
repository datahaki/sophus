// code by jph
package ch.alpine.sophus.lie.pgl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.jet.LinearFractionalTransform;
import ch.alpine.tensor.mat.pi.LinearSubspace;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.d.DiscreteUniformDistribution;

class PGlGroupTest {
  @RepeatedTest(5)
  void testCombine(RepetitionInfo repetitionInfo) {
    int d = repetitionInfo.getCurrentRepetition();
    int n = d + 2;
    Distribution distribution = DiscreteUniformDistribution.of(-1000, 1000);
    LinearFractionalTransform lft1 = LinearFractionalTransform.fit( //
        RandomVariate.of(distribution, n, d), //
        RandomVariate.of(distribution, n, d));
    LinearFractionalTransform lft2 = LinearFractionalTransform.fit( //
        RandomVariate.of(distribution, n, d), //
        RandomVariate.of(distribution, n, d));
    Tensor tensor = PGlGroup.INSTANCE.combine(lft1.matrix(), lft2.matrix());
    assertEquals(tensor.length(), n - 1);
  }

  @ParameterizedTest
  @ValueSource(ints = { 2, 3, 4 })
  void testSimple(int n) {
    LinearSubspace linearSubspace = LinearSubspace.of(TPGlMemberQ.INSTANCE::defect, n, n);
    int dim = n * n - 1;
    assertEquals(linearSubspace.dimensions(), dim);
    Tensor w = RandomVariate.of(NormalDistribution.of(0, 0.0003), dim);
    Tensor address = linearSubspace.apply(w);
    address.negate();
    // IO.println(Pretty.of(MatrixExp.of(address).map(Round._4)));
  }
}
