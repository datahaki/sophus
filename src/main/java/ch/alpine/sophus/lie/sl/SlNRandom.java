// code by jph
package ch.alpine.sophus.lie.sl;

import java.util.random.RandomGenerator;

import ch.alpine.tensor.Rational;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Sign;
import ch.alpine.tensor.sca.pow.Power;

record SlNRandom(int n) implements RandomSampleInterface {
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
}
