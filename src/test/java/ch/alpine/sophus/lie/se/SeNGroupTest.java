// code by jph
package ch.alpine.sophus.lie.se;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.sophus.lie.se2.Se2Group;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.rot.RotationMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import showcase.GroupCheck;

class SeNGroupTest {
  @Test
  void testSe2() {
    SeNGroup seNGroup = new SeNGroup(2);
    Tensor basis = seNGroup.matrixBasis();
    Tensor xya = RandomVariate.of(UniformDistribution.unit(20), 3);
    Tensor cmp = Se2Group.INSTANCE.exponential0().exp(xya);
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(basis);
    Tensor V = matrixAlgebra.toMatrix(xya);
    Tensor exp = seNGroup.exponential0().exp(V);
    Tensor rot = RotationMatrix.of(cmp.Get(2));
    Tolerance.CHOP.requireClose(rot, Tensor.of(exp.stream().limit(2).map(row -> row.extract(0, 2))));
    Tolerance.CHOP.requireClose(exp.Get(0, 2), cmp.Get(0));
    Tolerance.CHOP.requireClose(exp.Get(1, 2), cmp.Get(1));
  }

  @ParameterizedTest
  @ValueSource(ints = { 2, 3, 5 })
  void testMatchCheck(int d) {
    SeNGroup seNGroup = new SeNGroup(d);
    GroupCheck.check(seNGroup);
    seNGroup.isPointQ().requireMember(IdentityMatrix.of(d + 1));
  }
}
