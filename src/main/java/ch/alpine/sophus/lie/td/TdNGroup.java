// code by jph
package ch.alpine.sophus.lie.td;

import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

import ch.alpine.sophus.lie.MatrixGroup;
import ch.alpine.sophus.math.api.SpecificManifold;
import ch.alpine.sophus.math.api.VectorEncodingMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.LogNormalDistribution;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.spa.SparseArray;

public class TdNGroup extends TdGroup implements SpecificManifold, MatrixGroup, VectorEncodingMarker {
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
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return RandomVariate.of(NormalDistribution.standard(), randomGenerator, n) //
        .append(RandomVariate.of(LogNormalDistribution.standard(), randomGenerator));
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("Td", n);
  }
}
