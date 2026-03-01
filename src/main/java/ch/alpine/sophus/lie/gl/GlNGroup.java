// code by jph
package ch.alpine.sophus.lie.gl;

import java.util.random.RandomGenerator;

import ch.alpine.sophus.lie.SpecificLieGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

public class GlNGroup extends GlGroup implements SpecificLieGroup {
  private final int n;

  public GlNGroup(int n) {
    this.n = Integers.requirePositive(n);
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
  public int matrixOrder() {
    return n;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise(super.toString(), n);
  }
}
