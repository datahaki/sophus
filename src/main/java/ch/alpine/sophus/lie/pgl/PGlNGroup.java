// code by jph
package ch.alpine.sophus.lie.pgl;

import ch.alpine.sophus.lie.MatrixGroup;
import ch.alpine.sophus.lie.sl.SlNGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;

public class PGlNGroup extends PGlGroup implements MatrixGroup {
  private final int n;

  public PGlNGroup(int n) {
    this.n = Integers.requirePositive(n);
  }

  @Override
  public Tensor matrixBasis() {
    SlNGroup slNGroup = new SlNGroup(n);
    return slNGroup.matrixBasis();
  }

  public int dimensions() {
    return n * n - 1;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise(super.toString(), n);
  }
}
