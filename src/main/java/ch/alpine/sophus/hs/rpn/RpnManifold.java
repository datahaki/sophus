// code by jph
package ch.alpine.sophus.hs.rpn;

import ch.alpine.sophus.hs.SpecificHomogeneousSpace;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.pdf.RandomSampleInterface;

/** real projective plane
 * 
 * Reference:
 * "Eichfeldtheorie" by Helga Baum, 2005, p. 22 */
public class RpnManifold extends RpManifold implements SpecificHomogeneousSpace {
  private final int n;
  private final int length;

  public RpnManifold(int n) {
    this.n = Integers.requirePositiveOrZero(n);
    length = n + 1;
  }

  @Override // from MemberQ
  public MemberQ isPointQ() {
    return p -> p.length() == length //
        && super.isPointQ().test(p);
  }

  @Override
  public RandomSampleInterface randomSampleInterface() {
    return HemisphereRandomSample.of(n);
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
