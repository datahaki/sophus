package ch.alpine.sophus.lie.td;

import java.util.stream.IntStream;

import ch.alpine.sophus.lie.MatrixGroup;
import ch.alpine.sophus.lie.VectorEncodingMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.spa.SparseArray;

public class TdNGroup extends TdGroup implements MatrixGroup, VectorEncodingMarker {
  private final int n;

  public TdNGroup(int n) {
    this.n = n;
  }

  @Override
  public Tensor matrixBasis() {
    int d = dimensions();
    Tensor tensor = SparseArray.of(RealScalar.ZERO, d, d, d);
    IntStream.range(0, d).forEach(i -> tensor.set(RealScalar.ONE, i, i, n));
    return tensor;
  }

  @Override
  public int dimensions() {
    return n + 1;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("Td", n);
  }
}
