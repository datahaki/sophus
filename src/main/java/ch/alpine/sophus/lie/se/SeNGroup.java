// code by jph
package ch.alpine.sophus.lie.se;

import java.util.random.RandomGenerator;

import ch.alpine.sophus.api.LieExponential;
import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.sophus.lie.SpecificLieGroup;
import ch.alpine.sophus.lie.se3.Se3Exponential;
import ch.alpine.sophus.lie.so.SoNGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ArrayFlatten;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.mat.pi.LinearSubspace;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

public class SeNGroup extends SeGroup implements SpecificLieGroup {
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
  public LieExponential exponential0() {
    return n == 3 //
        ? Se3Exponential.INSTANCE
        : SeExponential.INSTANCE;
  }

  public MatrixAlgebra matrixAlgebra() {
    int m = n + 1;
    LinearSubspace linearSubspace = LinearSubspace.of(TSeMemberQ.INSTANCE::defect, m, m);
    return new MatrixAlgebra(linearSubspace.basis());
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
  public int matrixOrder() {
    return n + 1;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise(super.toString(), n);
  }
}
