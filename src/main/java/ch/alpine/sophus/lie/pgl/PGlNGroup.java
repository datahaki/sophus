// code by jph
package ch.alpine.sophus.lie.pgl;

import ch.alpine.sophus.lie.MatrixGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Drop;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.Tuples;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.io.Primitives;

public class PGlNGroup extends PGlGroup implements MatrixGroup {
  private final int n;

  public PGlNGroup(int n) {
    this.n = Integers.requirePositive(n);
  }

  @Override
  public Tensor matrixBasis() {
    Tensor basis = Tensors.empty();
    for (Tensor ij : Drop.tail(Tuples.of(Range.of(0, n), 2), 1)) {
      int[] index = Primitives.toIntArray(ij);
      int i = index[0];
      int j = index[1];
      Tensor elem = Array.sparse(n, n);
      elem.set(RealScalar.ONE, i, j);
      basis.append(elem);
    }
    return basis;
  }

  public int dimensions() {
    return n * n - 1;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise(super.toString(), n);
  }
}
