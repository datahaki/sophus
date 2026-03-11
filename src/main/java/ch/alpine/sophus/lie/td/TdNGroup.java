// code by jph
package ch.alpine.sophus.lie.td;

import java.util.random.RandomGenerator;

import ch.alpine.sophus.api.VectorEncodingMarker;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.LogNormalDistribution;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Sign;

public class TdNGroup extends TdGroup implements VectorEncodingMarker {
  private final int n;

  public TdNGroup(int n) {
    this.n = n;
  }

  @Override
  public MemberQ isPointQ() {
    return t_lambda -> VectorQ.ofLength(t_lambda, dimensions()) //
        && Sign.isPositive(Last.of(t_lambda));
  }

  @Override
  public int dimensions() {
    return n + 1;
  }

  @Override
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return RandomVariate.of(NormalDistribution.standard(), randomGenerator, n) //
        .append(RandomVariate.of(LogNormalDistribution.standard(), randomGenerator));
  }

  @Override
  public int matrixOrder() {
    return n + 1;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("Td", n);
  }
}
