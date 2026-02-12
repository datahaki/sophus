// code by jph
package ch.alpine.sophus.lie.sc;

import java.io.Serializable;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.red.Times;
import ch.alpine.tensor.sca.Sign;
import ch.alpine.tensor.sca.exp.Exp;
import ch.alpine.tensor.sca.exp.Log;

public class ScGroup implements LieGroup, Serializable {
  public static final ScGroup INSTANCE = new ScGroup();

  @Override
  public MemberQ isPointQ() {
    return t -> VectorQ.of(t) //
        && t.stream().map(Scalar.class::cast).allMatch(Sign::isPositive);
  }

  private enum Exponential0 implements Exponential {
    INSTANCE;

    @Override // from Exponential
    public Tensor exp(Tensor x) {
      return VectorQ.require(x).maps(Exp.FUNCTION);
    }

    @Override // from Exponential
    public Tensor log(Tensor g) {
      return VectorQ.require(g).maps(Log.FUNCTION);
    }

    @Override
    public MemberQ isTangentQ() {
      return VectorQ::of;
    }
  }

  @Override
  public final Exponential exponential0() {
    return Exponential0.INSTANCE;
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
