// code by jph
package ch.ethz.idsc.sophus.math;

import java.io.Serializable;

import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Reverse;
import ch.ethz.idsc.tensor.alg.RotateLeft;
import ch.ethz.idsc.tensor.red.ArgMin;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/Cycles.html">Cycles</a> */
public class Cycles implements GroupElement, Serializable {
  /** @param cycles
   * @return */
  public static Cycles of(Tensor cycles) {
    return new Cycles(Tensor.of(cycles.stream() //
        .filter(Tensors::nonEmpty) //
        .map(Cycles::minFirst) //
        .sorted((v1, v2) -> Scalars.compare(v1.Get(0), v2.Get(0)))));
  }

  private static Tensor minFirst(Tensor vector) {
    return RotateLeft.of(vector, ArgMin.of(vector));
  }

  /***************************************************/
  private final Tensor cycles;

  private Cycles(Tensor cycles) {
    this.cycles = cycles;
  }

  @Override // from GroupElement
  public Tensor toCoordinate() {
    return cycles;
  }

  @Override // from GroupElement
  public Cycles inverse() {
    return new Cycles(Tensor.of(cycles.stream().map(Reverse::of)));
  }

  @Override // from GroupElement
  public Tensor combine(Tensor tensor) {
    // TODO Auto-generated method stub
    return null;
  }
}
