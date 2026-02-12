// code by jph
package ch.alpine.sophus.lie.rn;

import java.io.Serializable;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.LinearBiinvariantMean;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.MetricManifold;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.math.api.BilinearForm;
import ch.alpine.sophus.math.api.FrobeniusForm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.chq.MemberQ;

/** Euclidean vector space, group action is addition, the neutral element is 0.
 * 
 * the implementation also covers the case R^1 where the elements are of type {@link Scalar}
 * (instead of a vector of length 1)
 * 
 * in Euclidean space
 * the exponential function is the identity
 * the logarithm function is the identity
 * 
 * Euclidean vector metric */
public class RGroup implements LieGroup, MetricManifold, Serializable {
  public static final RGroup INSTANCE = new RGroup();

  protected RGroup() {
  }

  @Override
  public boolean isMember(Tensor tensor) {
    return VectorQ.of(tensor);
  }

  private enum Exponential0 implements Exponential {
    INSTANCE;

    @Override // from Exponential
    public Tensor exp(Tensor v) {
      return v.copy();
    }

    @Override // from Exponential
    public Tensor log(Tensor y) {
      return y.copy();
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
    return LinearBiinvariantMean.INSTANCE;
  }

  @Override
  public BilinearForm bilinearForm(Tensor p) {
    return FrobeniusForm.INSTANCE;
  }

  @Override
  public Tensor neutral(Tensor element) {
    return element.maps(Scalar::zero);
  }

  @Override
  public Tensor invert(Tensor element) {
    return element.negate();
  }

  @Override
  public Tensor combine(Tensor element1, Tensor element2) {
    return element1.add(element2);
  }

  @Override
  public Tensor adjoint(Tensor point, Tensor tensor) {
    return tensor.copy();
  }

  @Override
  public Tensor dL(Tensor point, Tensor tensor) {
    return tensor;
  }

  @Override
  public Tensor dR(Tensor point, Tensor tensor) {
    return tensor;
  }

  @Override
  public String toString() {
    return "R";
  }
}
