// code by jph
package ch.alpine.sophus.lie.sl;

import ch.alpine.sophus.lie.SpecificLieGroup;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.pdf.RandomSampleInterface;

public class SlNGroup extends SlGroup implements SpecificLieGroup {
  private final int n;

  public SlNGroup(int n) {
    this.n = Integers.requirePositive(n);
  }

  @Override
  public MemberQ isPointQ() {
    return matrix -> matrix.length() == n //
        && super.isPointQ().test(matrix);
  }

  @Override
  public int dimensions() {
    return n * n - 1;
  }

  @Override
  public RandomSampleInterface randomSampleInterface() {
    return new SlNRandom(n);
  }

  @Override
  public int matrixOrder() {
    return n;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise(super.toString(), n);
  }
}
