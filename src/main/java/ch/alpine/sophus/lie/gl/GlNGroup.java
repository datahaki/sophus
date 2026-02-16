// code by jph
package ch.alpine.sophus.lie.gl;

import java.util.random.RandomGenerator;

import ch.alpine.sophus.lie.MatrixGroup;
import ch.alpine.sophus.math.api.SpecificManifold;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.Tuples;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.io.Primitives;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

public class GlNGroup extends GlGroup implements SpecificManifold, MatrixGroup {
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
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return MatrixExp.of(RandomVariate.of(NormalDistribution.standard(), randomGenerator, n, n));
  }

  @Override
  public int dimensions() {
    return n * n;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise(super.toString(), n);
  }
}
