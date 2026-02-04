package ch.alpine.sophus.lie.he;

import ch.alpine.sophus.lie.MatrixGroup;
import ch.alpine.sophus.lie.VectorEncodingMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;

public class HeNGroup extends HeGroup implements MatrixGroup, VectorEncodingMarker {
  private final int n;

  public HeNGroup(int n) {
    this.n = Integers.requirePositive(n);
  }

  @Override
  public boolean isMember(Tensor uvw) {
    return dimensions() == uvw.length() //
        && super.isMember(uvw);
  }

  @Override
  public Tensor matrixBasis() {
    int d = n + 2;
    Tensor basis = Tensors.empty();
    for (int i = 1; i <= n; ++i) {
      Tensor elem = Array.sparse(d, d);
      elem.set(RealScalar.ONE, 0, i);
      basis.append(elem);
    }
    for (int i = 1; i <= n; ++i) {
      Tensor elem = Array.sparse(d, d);
      elem.set(RealScalar.ONE, i, n + 1);
      basis.append(elem);
    }
    {
      Tensor elem = Array.sparse(d, d);
      elem.set(RealScalar.ONE, 0, n + 1);
      basis.append(elem);
    }
    return basis;
  }

  @Override
  public int dimensions() {
    return n * 2 + 1;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("He", n);
  }
}
