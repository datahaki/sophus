// code by jph
package ch.alpine.sophus.lie.se;

import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.sophus.lie.MatrixGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.Reverse;
import ch.alpine.tensor.alg.Subsets;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.io.Primitives;
import ch.alpine.tensor.mat.pi.LinearSubspace;

public class SeNGroup extends SeGroup implements MatrixGroup {
  private final int n;

  public SeNGroup(int n) {
    Integers.requireLessEquals(2, n);
    this.n = n;
  }

  @Override
  public MemberQ isPointQ() {
    return t -> t.length() == n + 1 //
        && super.isPointQ().test(t);
  }

  public MatrixAlgebra matrixAlgebra() {
    int m = n + 1;
    LinearSubspace linearSubspace = LinearSubspace.of(TSeMemberQ.INSTANCE::defect, m, m);
    return new MatrixAlgebra(linearSubspace.basis());
  }

  @Override
  public Tensor matrixBasis() {
    Tensor basis = Tensors.empty();
    for (int i = 0; i < n; ++i) {
      Tensor elem = Array.sparse(n + 1, n + 1);
      elem.set(RealScalar.ONE, i, n);
      basis.append(elem);
    }
    Scalar parity = RealScalar.ONE;
    for (Tensor ij : Subsets.of(Reverse.of(Range.of(0, n)), 2)) {
      int[] index = Primitives.toIntArray(ij);
      int i = index[0];
      int j = index[1];
      Tensor elem = Array.sparse(n + 1, n + 1);
      elem.set(parity, i, j);
      parity = parity.negate();
      elem.set(parity, j, i);
      basis.append(elem);
    }
    return basis;
  }

  public int dimensions() {
    return n * (n + 1) / 2;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("SE", n);
  }
}
