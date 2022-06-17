// code by jph
package ch.alpine.sophus.lie.he;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.re.LinearSolve;

/** HeBarycenter remains only for testing purpose. */
/* package */ class HeBarycenter implements TensorUnaryOperator {
  private final Tensor lhs;

  public HeBarycenter(Tensor sequence) {
    Tensor tX = Transpose.of(Tensor.of(sequence.stream().map(HeFormat::of).map(HeFormat::x)));
    Tensor tY = Transpose.of(Tensor.of(sequence.stream().map(HeFormat::of).map(HeFormat::y)));
    lhs = Join.of(tX, tY);
    Tensor tensor = HeBiinvariantMean.xydot(sequence);
    Tensor z = Tensor.of(sequence.stream().map(HeFormat::of).map(HeFormat::z));
    lhs.append(z.subtract(tensor.multiply(RationalScalar.HALF)));
    lhs.append(Tensors.vector(l -> RealScalar.ONE, sequence.length()));
  }

  @Override
  public Tensor apply(Tensor mean) {
    HeFormat heFormat = HeFormat.of(mean);
    Tensor mXY = heFormat.x().dot(heFormat.y());
    Tensor rhs = Join.of(heFormat.x(), heFormat.y());
    rhs.append(heFormat.z().subtract(mXY.multiply(RationalScalar.HALF)));
    rhs.append(RealScalar.ONE);
    return LinearSolve.of(lhs, rhs);
  }
}
