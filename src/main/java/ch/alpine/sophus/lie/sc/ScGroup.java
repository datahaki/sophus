// code by jph
package ch.alpine.sophus.lie.sc;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.api.LieExponential;
import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.lie.AbstractLieGroup;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.red.Times;
import ch.alpine.tensor.sca.Sign;

public class ScGroup extends AbstractLieGroup {
  public static final ScGroup INSTANCE = new ScGroup();

  @Override
  public MemberQ isPointQ() {
    return t -> VectorQ.of(t) //
        && t.stream().map(Scalar.class::cast).allMatch(Sign::isPositive);
  }

  @Override
  public final LieExponential exponential0() {
    return ScExponential0.INSTANCE;
  }

  @Override
  public BiinvariantMean biinvariantMean() {
    return (Tensor sequence, Tensor weights) -> {
      AffineQ.INSTANCE.require(weights);
      Exponential exponential = exponential0();
      return exponential.exp(weights.dot(exponential.log().slash(sequence)));
    };
  }

  @Override
  public Tensor neutral(Tensor element) {
    return VectorQ.require(element).maps(Scalar::one);
  }

  @Override
  public Tensor invert(Tensor element) {
    return VectorQ.require(element).maps(Scalar::reciprocal);
  }

  @Override
  public Tensor combine(Tensor element1, Tensor element2) {
    if (isPointQ().test(element1) && isPointQ().test(element2))
      return Times.of(element1, element2);
    throw new Throw(element1, element2);
  }

  @Override
  public Tensor adjoint(Tensor point, Tensor tensor) {
    if (isPointQ().test(point))
      return tensor;
    throw new Throw(point);
  }

  @Override
  public Tensor dL(Tensor point, Tensor tensor) {
    if (isPointQ().test(point))
      return Times.of(point, tensor);
    throw new Throw(point);
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("Sc");
  }
}
