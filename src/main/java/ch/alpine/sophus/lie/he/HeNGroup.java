// code by jph
package ch.alpine.sophus.lie.he;

import java.util.random.RandomGenerator;

import ch.alpine.sophus.hs.SpecificManifold;
import ch.alpine.sophus.lie.MatrixGroup;
import ch.alpine.sophus.lie.VectorEncodingMarker;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

public class HeNGroup extends HeGroup implements SpecificManifold, MatrixGroup, VectorEncodingMarker {
  private final int n;

  public HeNGroup(int n) {
    this.n = Integers.requirePositive(n);
  }

  @Override
  public MemberQ isPointQ() {
    return uvw -> VectorQ.of(uvw) && dimensions() == uvw.length();
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
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return RandomVariate.of(NormalDistribution.standard(), randomGenerator, dimensions());
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise(super.toString(), n);
  }
}
