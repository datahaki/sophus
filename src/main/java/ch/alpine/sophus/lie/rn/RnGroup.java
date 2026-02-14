// code by jph
package ch.alpine.sophus.lie.rn;

import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

import ch.alpine.sophus.hs.SpecificManifold;
import ch.alpine.sophus.lie.MatrixGroup;
import ch.alpine.sophus.lie.VectorEncodingMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.spa.SparseArray;

public class RnGroup extends RGroup implements SpecificManifold, MatrixGroup, VectorEncodingMarker {
  private final int n;

  public RnGroup(int n) {
    this.n = n;
  }

  @Override
  public MemberQ isPointQ() {
    return tensor -> tensor.length() == dimensions() //
        && super.isPointQ().test(tensor);
  }

  @Override
  public Tensor matrixBasis() {
    Tensor tensor = SparseArray.of(RealScalar.ZERO, n, n + 1, n + 1);
    IntStream.range(0, n).forEach(i -> tensor.set(RealScalar.ONE, i, i, n));
    return tensor;
  }

  @Override // from RandomSampleInterface
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return RandomVariate.of(NormalDistribution.standard(), randomGenerator, dimensions());
  }

  @Override
  public int dimensions() {
    return n;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise(super.toString(), n);
  }
}
