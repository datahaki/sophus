// code by jph
package ch.alpine.sophus.hs.spd;

import java.util.random.RandomGenerator;

import ch.alpine.sophus.hs.SpecificManifold;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.pdf.c.NormalDistribution;

public class SpdNManifold extends SpdManifold implements SpecificManifold {
  private final int n;

  public SpdNManifold(int n) {
    this.n = Integers.requirePositive(n);
  }

  @Override
  public MemberQ isPointQ() {
    return p -> p.length() == n //
        && super.isPointQ().test(p);
  }

  @Override
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return new Spd0RandomSample(n, NormalDistribution.standard()).randomSample(randomGenerator);
  }

  @Override
  public int dimensions() {
    return n * (n + 1) / 2;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("Spd", n);
  }
}
