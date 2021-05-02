// code by ob, jph
package ch.alpine.sophus.flt.ga;

import java.util.Objects;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.BoundedLinkedList;
import ch.alpine.tensor.itp.BinaryAverage;

/** input to the operator are the individual elements of the sequence */
public class GeodesicIIRn implements TensorUnaryOperator {
  /** @param geodesicExtrapolation
   * @param binaryAverage
   * @param radius
   * @param alpha
   * @return
   * @throws Exception if either parameter is null */
  public static TensorUnaryOperator of( //
      TensorUnaryOperator geodesicExtrapolation, BinaryAverage binaryAverage, int radius, Scalar alpha) {
    return new GeodesicIIRn( //
        Objects.requireNonNull(geodesicExtrapolation), //
        Objects.requireNonNull(binaryAverage), //
        radius, //
        Objects.requireNonNull(alpha));
  }

  /***************************************************/
  private final TensorUnaryOperator geodesicExtrapolation;
  private final BoundedLinkedList<Tensor> boundedLinkedList;
  private final BinaryAverage binaryAverage;
  private final Scalar alpha;

  /* package */ GeodesicIIRn( //
      TensorUnaryOperator geodesicExtrapolation, BinaryAverage binaryAverage, int radius, Scalar alpha) {
    this.geodesicExtrapolation = geodesicExtrapolation;
    this.binaryAverage = binaryAverage;
    this.alpha = alpha;
    this.boundedLinkedList = new BoundedLinkedList<>(radius);
  }

  @Override
  public Tensor apply(Tensor x) {
    Tensor value = boundedLinkedList.size() < 2 //
        ? x.copy()
        : binaryAverage.split(geodesicExtrapolation.apply(Tensor.of(boundedLinkedList.stream())), x, alpha);
    boundedLinkedList.add(value);
    return value;
  }
}
