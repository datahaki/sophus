// code by jph
package ch.alpine.sophus.lie.td;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.lie.GroupCheck;
import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.sophus.lie.gl.GlGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dot;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.TriangularDistribution;

class TdNGroupTest {
  @ParameterizedTest
  @ValueSource(ints = { 2, 3, 5 })
  void testMatch(int d) {
    TdNGroup tdNGroup = new TdNGroup(d);
    Tensor matrixBasis = tdNGroup.matrixBasis();
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(matrixBasis);
    Distribution distribution = TriangularDistribution.of(-0.1, 0, 0.1);
    int dim = matrixBasis.length();
    Tensor x = RandomVariate.of(distribution, dim);
    Tensor y = RandomVariate.of(distribution, dim);
    Exponential exponential0 = tdNGroup.exponential0();
    Tensor exp_x = exponential0.exp(x);
    Tensor exp_y = exponential0.exp(y);
    Tensor z = exponential0.log(tdNGroup.combine(exp_x, exp_y));
    Tensor X = MatrixExp.of(matrixAlgebra.toMatrix(x));
    Tensor Y = MatrixExp.of(matrixAlgebra.toMatrix(y));
    Tolerance.CHOP.requireClose(exp_x, X.get(Tensor.ALL, dim - 1));
    Tolerance.CHOP.requireClose(exp_y, Y.get(Tensor.ALL, dim - 1));
    Tensor Z = MatrixLog.of(Dot.of(X, Y));
    Tensor res = Z.get(Tensor.ALL, dim - 1);
    Tolerance.CHOP.requireClose(z, res);
    TdRandomSample randomSample = new TdRandomSample(NormalDistribution.standard(), d, TriangularDistribution.with(1, 0.1));
    GroupCheck.check(tdNGroup, randomSample);
  }

  @Test
  void testMatchCheck() {
    TdNGroup lieGroup = new TdNGroup(1);
    Tensor basis = lieGroup.matrixBasis();
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(basis);
    Exponential exponential0 = GlGroup.INSTANCE.exponential0();
    Tensor X = Tensors.vector(2, 1).dot(basis);
    Tensor x = exponential0.exp(X);
    Tensor TDX = x.get(Tensor.ALL, 1);
    Tensor TDY = Tensors.vector(10, 3);
    Tensor Y = TDY.dot(basis);
    Tensor y = exponential0.exp(Y);
    Tensor xyxi = GlGroup.INSTANCE.conjugation(x).apply(y);
    Tensor adxy = exponential0.log(xyxi);
    Tensor vector = matrixAlgebra.toVector(adxy);
    Tensor adjoint = lieGroup.adjoint(TDX, TDY);
    Tolerance.CHOP.requireClose(vector, adjoint);
  }
}
