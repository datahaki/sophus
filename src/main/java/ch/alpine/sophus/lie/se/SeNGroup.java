// code by jph
package ch.alpine.sophus.lie.se;

import java.util.random.RandomGenerator;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.SpecificManifold;
import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.sophus.lie.MatrixGroup;
import ch.alpine.sophus.lie.se3.Se3Exponential0;
import ch.alpine.sophus.lie.so.SoNGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.ArrayFlatten;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.Reverse;
import ch.alpine.tensor.alg.Subsets;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.io.Primitives;
import ch.alpine.tensor.mat.pi.LinearSubspace;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

public class SeNGroup extends SeGroup implements MatrixGroup, SpecificManifold {
  private final int n;

  /** @param n in SE(n) */
  public SeNGroup(int n) {
    Integers.requireLessEquals(2, n);
    this.n = n;
  }

  @Override
  public MemberQ isPointQ() {
    return t -> t.length() == n + 1 //
        && super.isPointQ().test(t);
  }

  @Override
  public Exponential exponential0() {
    return n == 3 //
        ? Se3Exponential0.INSTANCE
        : SeExponential0.INSTANCE;
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

  @Override
  public int dimensions() {
    return n * (n + 1) / 2;
  }

  @Override
  public Tensor randomSample(RandomGenerator randomGenerator) {
    Tensor rot = new SoNGroup(n).randomSample(randomGenerator);
    Tensor pnt = RandomVariate.of(NormalDistribution.standard(), randomGenerator, n, 1);
    return ArrayFlatten.of(new Tensor[][] { { rot, pnt } }).append(UnitVector.of(n + 1, n));
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("SE", n);
  }
}
