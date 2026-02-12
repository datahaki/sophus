// code by jph
package ch.alpine.sophus.lie.sl;

import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.sophus.lie.MatrixGroup;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.mat.pi.LinearSubspace;

public class SlNGroup extends SlGroup implements MatrixGroup {
  private final int n;

  public SlNGroup(int n) {
    this.n = Integers.requirePositive(n);
  }

  @Override
  public MemberQ isPointQ() {
    return matrix -> matrix.length() == n //
        && super.isPointQ().test(matrix);
  }

  public MatrixAlgebra matrixAlgebra() {
    LinearSubspace linearSubspace = LinearSubspace.of(TSlMemberQ.INSTANCE::defect, n, n);
    return new MatrixAlgebra(linearSubspace.basis());
  }

  @Override
  public Tensor matrixBasis() {
    Tensor basis = Tensors.empty();
    for (int i = 0; i < n; ++i)
      for (int j = i + 1; j < n; ++j) {
        {
          Tensor tensor = Array.sparse(n, n);
          tensor.set(RealScalar.ONE, i, j);
          tensor.set(RealScalar.ONE.negate(), j, i);
          basis.append(tensor);
        }
        {
          Tensor tensor = Array.sparse(n, n);
          tensor.set(RealScalar.ONE, i, j);
          tensor.set(RealScalar.ONE, j, i);
          basis.append(tensor);
        }
      }
    for (int i = 1; i < n; ++i) {
      Tensor tensor = Array.sparse(n, n);
      tensor.set(RealScalar.ONE, i - 1, i - 1);
      tensor.set(RealScalar.ONE.negate(), i, i);
      basis.append(tensor);
    }
    return basis.multiply(RationalScalar.HALF);
  }

  public int dimensions() {
    return n * n - 1;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("SL", n);
  }
}
