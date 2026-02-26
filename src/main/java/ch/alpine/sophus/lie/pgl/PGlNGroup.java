// code by jph
package ch.alpine.sophus.lie.pgl;

import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;

public class PGlNGroup extends PGlGroup {
  private final int n;

  public PGlNGroup(int n) {
    this.n = Integers.requirePositive(n);
  }

  public int dimensions() {
    return n * n - 1;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise(super.toString(), n);
  }
}
