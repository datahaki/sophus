// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import java.util.Iterator;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;

/** class exists for demo */
/* package */ enum ClothoidCurvatures {
  ;
  /** @param tensor
   * @return vector of length of given tensor with curvature estimates */
  public static Tensor of(Tensor tensor) {
    Tensor vector = Tensors.reserve(tensor.length());
    Iterator<Tensor> iterator = tensor.iterator();
    if (iterator.hasNext()) {
      Clothoid.Curvature clothoidCurvature = null;
      Tensor p = iterator.next();
      while (iterator.hasNext()) {
        clothoidCurvature = new Clothoid(p, p = iterator.next()).new Curvature();
        vector.append(clothoidCurvature.head());
      }
      vector.append(clothoidCurvature.tail());
    }
    return vector;
  }
}
