// code by jph
package ch.alpine.sophus.lie.sl;

import java.util.random.RandomGenerator;

import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.sophus.lie.SpecificGroup;
import ch.alpine.tensor.Rational;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.mat.pi.LinearSubspace;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Sign;
import ch.alpine.tensor.sca.pow.Power;

public class SlNGroup extends SlGroup implements SpecificGroup {
  private final int n;

  public SlNGroup(int n) {
    this.n = Integers.requirePositive(n);
  }

  @Override
  public MemberQ isPointQ() {
    return matrix -> matrix.length() == n //
        && super.isPointQ().test(matrix);
  }

  public MatrixAlgebra matrixAlgebra() {
    LinearSubspace linearSubspace = LinearSubspace.of(TSlMemberQ.INSTANCE::defect, n, n);
    return new MatrixAlgebra(linearSubspace.basis());
  }

  @Override
  public int dimensions() {
    return n * n - 1;
  }

  @Override
  public Tensor randomSample(RandomGenerator randomGenerator) {
    Tensor matrix = RandomVariate.of(NormalDistribution.standard(), randomGenerator, n, n);
    Scalar det = Det.of(matrix);
    if (Sign.isNegative(det)) {
      matrix.set(Tensor::negate, 0);
      det = det.negate();
    }
    return matrix.divide(Power.of(det, Rational.of(1, n)));
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
