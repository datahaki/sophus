// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.MidpointInterface;
import ch.ethz.idsc.sophus.ref.d1.CurveSubdivision;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;

public class ControlMidpoints implements CurveSubdivision, Serializable {
  private static final long serialVersionUID = 83413311523575130L;

  /** @param midpointInterface
   * @return */
  public static CurveSubdivision of(MidpointInterface midpointInterface) {
    return new ControlMidpoints(Objects.requireNonNull(midpointInterface));
  }

  /***************************************************/
  private final MidpointInterface midpointInterface;

  private ControlMidpoints(MidpointInterface midpointInterface) {
    this.midpointInterface = midpointInterface;
  }

  @Override // from CurveSubdivision
  public Tensor cyclic(Tensor tensor) {
    if (Tensors.isEmpty(tensor))
      return Tensors.empty();
    Tensor result = Tensors.reserve(tensor.length());
    Iterator<Tensor> iterator = tensor.iterator();
    Tensor prev = iterator.next();
    Tensor _1st = prev;
    Tensor next = prev;
    while (iterator.hasNext()) {
      next = iterator.next();
      result.append(midpointInterface.midpoint(prev, next));
      prev = next;
    }
    result.append(midpointInterface.midpoint(next, _1st));
    return result;
  }

  @Override // from CurveSubdivision
  public Tensor string(Tensor tensor) {
    if (Tensors.isEmpty(tensor))
      return Tensors.empty();
    Tensor result = Tensors.reserve(tensor.length() - 1);
    Iterator<Tensor> iterator = tensor.iterator();
    Tensor prev = iterator.next();
    while (iterator.hasNext())
      result.append(midpointInterface.midpoint(prev, prev = iterator.next()));
    return result;
  }
}
