// code by jph
package ch.alpine.sophus.lie.sp;

import java.util.random.RandomGenerator;

import ch.alpine.sophus.hs.SpecificManifold;
import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.pi.LinearSubspace;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

public class SpNGroup extends SpGroup implements SpecificManifold {
  private final int n;
  private final LinearSubspace linearSubspace;

  public SpNGroup(int n) {
    this.n = n;
    linearSubspace = LinearSubspace.of(TSpMemberQ.INSTANCE::defect, 2 * n, 2 * n);
  }

  public MatrixAlgebra matrixAlgebra() {
    return new MatrixAlgebra(linearSubspace.basis());
  }

  @Override
  public int dimensions() {
    return n * (2 * n + 1);
  }

  @Override
  public Tensor randomSample(RandomGenerator randomGenerator) {
    Tensor weights = RandomVariate.of(NormalDistribution.standard(), randomGenerator, linearSubspace.dimensions());
    return MatrixExp.of(linearSubspace.apply(weights));
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise(super.toString(), "2*" + n);
  }
}
