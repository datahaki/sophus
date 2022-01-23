// code by jph
package ch.alpine.sophus.flt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.alpine.sophus.flt.bm.BiinvariantMeanCenter;
import ch.alpine.sophus.flt.ga.GeodesicCenter;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.img.ImageFilter;

/** CenterFilter uses <em>odd</em> number of elements from the signal always.
 * 
 * <p>CenterFilter is different towards the boundaries of the signal when
 * compared to a 1-dimensional {@link ImageFilter}.
 * 
 * <p>For instance, CenterFilter for radius 2
 * <pre>
 * out[0] = f[0]
 * out[1] = f[0, 1, 2]
 * out[2] = f[0, 1, 2, 3, 4]
 * out[3] = f[1, 2, 3, 4, 5]
 * ...
 * </pre>
 * 
 * Whereas ImageFilter for radius 2
 * <pre>
 * out[0] = f[0, 1, 2] (not centered at 0)
 * out[1] = f[0, 1, 2, 3] (not centered at 1)
 * out[2] = f[0, 1, 2, 3, 4]
 * out[3] = f[1, 2, 3, 4, 5]
 * ...
 * </pre>
 * 
 * Hint: {@link GeodesicCenter}, and {@link BiinvariantMeanCenter} are typically used
 * for the tensorUnaryOperator */
public record CenterFilter(TensorUnaryOperator tensorUnaryOperator, int radius) implements TensorUnaryOperator {
  public CenterFilter {
    Objects.requireNonNull(tensorUnaryOperator);
    Integers.requirePositiveOrZero(radius);
  }

  @Override // from TensorUnaryOperator
  public Tensor apply(Tensor tensor) {
    List<Tensor> list = new ArrayList<>(tensor.length());
    for (int index = 0; index < tensor.length(); ++index) {
      int lo = Math.max(0, index - radius);
      int hi = Math.min(index + radius, tensor.length() - 1);
      int delta = Math.min(index - lo, hi - index);
      list.add(tensorUnaryOperator.apply(tensor.extract(index - delta, index + delta + 1)));
    }
    Integers.requireEquals(list.size(), tensor.length());
    return Unprotect.using(list);
  }
}
