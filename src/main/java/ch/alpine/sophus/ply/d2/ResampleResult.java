// code by jph
package ch.alpine.sophus.ply.d2;

import java.util.List;
import java.util.stream.Collectors;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.itp.Interpolation;
import ch.alpine.tensor.itp.LinearInterpolation;
import ch.alpine.tensor.lie.r2.RotationMatrix;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

public class ResampleResult {
  private final Interpolation interpolation;
  private final List<Tensor> list;
  private final Clip clip;

  /** @param points
   * @param list */
  public ResampleResult(Tensor points, List<Tensor> list) {
    interpolation = LinearInterpolation.of(points);
    this.list = list;
    clip = Clips.positive(points.length());
  }

  public List<Tensor> getParameters() {
    return list;
  }

  public List<Tensor> getPoints() {
    return list.stream() //
        .map(vector -> vector.map(interpolation::at)) //
        .collect(Collectors.toList());
  }

  /** @param relativeZero in the interval [0, 1]
   * @param rate unitless
   * @return */
  public List<Tensor> getPointsSpin(Scalar relativeZero, Scalar rate) {
    // TODO rescale introduces error because it assumes regular sampling along the circle
    return list.stream() //
        .map(vector -> vector.map(param -> RotationMatrix.of(clip.rescale(param).subtract(relativeZero).multiply(rate)).dot(interpolation.at(param)))) //
        .collect(Collectors.toList());
  }
}
