// code by jph
package ch.alpine.sophus.lie.gl;

import ch.alpine.sophus.lie.MatrixGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.Tuples;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.io.Primitives;

public class GlNGroup extends GlGroup implements MatrixGroup {
  private final int n;

  public GlNGroup(int n) {
    this.n = Integers.requirePositive(n);
  }

  @Override
  public Tensor matrixBasis() {
    Tensor basis = Tensors.empty();
    for (Tensor ij : Tuples.of(Range.of(0, n), 2)) {
      int[] index = Primitives.toIntArray(ij);
      int i = index[0];
      int j = index[1];
      Tensor elem = Array.sparse(n, n);
      elem.set(RealScalar.ONE, i, j);
      basis.append(elem);
    }
    return basis;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("GL", n);
  }
}
