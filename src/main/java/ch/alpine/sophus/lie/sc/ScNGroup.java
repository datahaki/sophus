// code by jph
package ch.alpine.sophus.lie.sc;

import java.util.stream.IntStream;

import ch.alpine.sophus.lie.MatrixGroup;
import ch.alpine.sophus.lie.VectorEncodingMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.spa.SparseArray;

public class ScNGroup extends ScGroup implements MatrixGroup, VectorEncodingMarker {
  private final int n;

  public ScNGroup(int n) {
    this.n = n;
  }

  @Override
  public MemberQ isPointQ() {
    return t -> super.isPointQ().test(t) && t.length() == n;
  }

  @Override
  public Tensor matrixBasis() {
    Tensor tensor = SparseArray.of(RealScalar.ZERO, n, n, n);
    IntStream.range(0, n).forEach(i -> tensor.set(RealScalar.ONE, i, i, i));
    return tensor;
  }

  @Override
  public int dimensions() {
    return n;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("Sc", n);
  }
}
