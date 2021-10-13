// code by ob
package ch.alpine.sophus.flt.ga;

import java.util.Objects;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.BoundedLinkedList;
import ch.alpine.tensor.itp.BinaryAverage;

public class GeodesicExtrapolationFilter implements TensorUnaryOperator {
  /** @param geodesicExtrapolation
   * @param radius
   * @return */
  public static TensorUnaryOperator of(TensorUnaryOperator geodesicExtrapolation, BinaryAverage binaryAverage, int radius) {
    return new GeodesicExtrapolationFilter(geodesicExtrapolation, binaryAverage, radius);
  }

  // ---
  private final TensorUnaryOperator geodesicExtrapolation;
  private final BinaryAverage binaryAverage;
  private final BoundedLinkedList<Tensor> boundedLinkedList;

  private GeodesicExtrapolationFilter(TensorUnaryOperator geodesicExtrapolation, BinaryAverage binaryAverage, int radius) {
    this.geodesicExtrapolation = Objects.requireNonNull(geodesicExtrapolation);
    this.binaryAverage = binaryAverage;
    this.boundedLinkedList = new BoundedLinkedList<>(radius);
  }

  @Override
  public Tensor apply(Tensor tensor) {
    Tensor result = Tensors.empty();
    // Initializing BL up until extrapolation is possible
    for (int i = 0; i < 2; i++) {
      boundedLinkedList.add(tensor.get(i));
      result.append(tensor.get(i));
    }
    for (int index = 1; index < tensor.length() - 1; index++) {
      // Extrapolation Step
      Tensor temp = geodesicExtrapolation.apply(Tensor.of(boundedLinkedList.stream()));
      // Measurement update step
      Scalar alpha = RealScalar.of(0.2);
      temp = binaryAverage.split(temp, tensor.get(index + 1), alpha);
      boundedLinkedList.add(temp);
      result.append(temp);
    }
    return result;
  }
}