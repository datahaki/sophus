// code by jph
package ch.alpine.sophus.lie.rn;

import ch.alpine.sophus.api.BilinearForm;
import ch.alpine.sophus.api.MetricManifold;
import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.LinearBiinvariantMean;
import ch.alpine.sophus.lie.AbstractLieGroup;
import ch.alpine.sophus.lie.LieExponential;
import ch.alpine.sophus.math.FrobeniusForm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
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
public class RGroup extends AbstractLieGroup implements MetricManifold {
  public static final RGroup INSTANCE = new RGroup();

  protected RGroup() {
  }

  @Override
  public MemberQ isPointQ() {
    /* scalars, vectors, arrays, unstructured tensors */
    return _ -> true;
  }

  @Override
  public final LieExponential exponential0() {
    return RExponential0.INSTANCE;
  }

  @Override
  public final BiinvariantMean biinvariantMean() {
    return LinearBiinvariantMean.INSTANCE;
  }

  @Override
  public final BilinearForm bilinearForm(Tensor p) {
    return FrobeniusForm.INSTANCE;
  }

  @Override
  public final Tensor neutral(Tensor element) {
    return element.maps(Scalar::zero);
  }

  @Override
  public final Tensor invert(Tensor element) {
    return element.negate();
  }

  @Override
  public final Tensor combine(Tensor element1, Tensor element2) {
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
