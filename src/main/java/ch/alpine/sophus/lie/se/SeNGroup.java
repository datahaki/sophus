// code by jph
package ch.alpine.sophus.lie.se;

import ch.alpine.sophus.lie.LieExponential;
import ch.alpine.sophus.lie.SpecificLieGroup;
import ch.alpine.sophus.lie.se3.Se3Exponential;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.pdf.RandomSampleInterface;

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
  public LieExponential lieExponential() {
    return n == 3 //
        ? Se3Exponential.INSTANCE
        : super.lieExponential();
  }

  @Override
  public int dimensions() {
    return n * (n + 1) / 2;
  }

  @Override
  public RandomSampleInterface randomSampleInterface() {
    return new SeNRandom(n);
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
