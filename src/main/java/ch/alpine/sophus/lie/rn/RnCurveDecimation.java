// code by jph
package ch.alpine.sophus.lie.rn;

import ch.alpine.sophus.decim.CurveDecimation;
import ch.alpine.tensor.Scalar;

/** Quote from Wikipedia:
 * The algorithm is widely used in robotics to perform simplification and denoising
 * of range data acquired by a rotating range scanner.
 * In this field it is known as the split-and-merge algorithm and is attributed to Duda and Hart.
 * 
 * The expected complexity of this algorithm is O(n log n).
 * However, the worst-case complexity is O(n^2). */
public enum RnCurveDecimation {
  ;
  /** @param epsilon
   * @return */
  public static CurveDecimation of(Scalar epsilon) {
    return CurveDecimation.of(RnGroup.INSTANCE, epsilon);
  }
}
