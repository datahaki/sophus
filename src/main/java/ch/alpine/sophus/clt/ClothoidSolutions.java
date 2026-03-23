// code by jph
package ch.alpine.sophus.clt;

import java.io.Serializable;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.opt.fnd.FindRoot;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.Re;
import ch.alpine.tensor.sca.Sign;

/** @param probes -min == max for tests to pass */
public class ClothoidSolutions implements Serializable {
  private static final Chop CHOP = Chop._10;
  private final ClothoidTangentDefect clothoidTangentDefect;
  private final Tensor probes;
  /** function is s1 odd
   * function is s2 even */
  private final Tensor lambdas = Tensors.empty();
  private final Tensor defects_real;

  public ClothoidSolutions(ClothoidTangentDefect clothoidTangentDefect, Clip clip) {
    this.clothoidTangentDefect = clothoidTangentDefect;
    probes = Subdivide.increasing(clip, 100);
    ScalarUnaryOperator function = clothoidTangentDefect::defect;
    FindRoot findRoot = FindRoot.of(function, CHOP);
    Tensor defects = probes.maps(clothoidTangentDefect);
    defects_real = defects.maps(Re.FUNCTION);
    // Tensor defects_imag = defects.map(Imag.FUNCTION);
    boolean prev = Sign.isPositive(defects_real.Get(0));
    for (int index = 1; index < probes.length(); ++index) {
      boolean next = Sign.isPositive(defects_real.Get(index));
      if (prev && !next) {
        Scalar x0 = probes.Get(index - 1);
        Scalar x1 = probes.Get(index);
        Scalar y0 = defects_real.Get(index - 1);
        Scalar y1 = defects_real.Get(index);
        try { // necessary because of degenerate input
          lambdas.append(findRoot.inside(Clips.interval(x0, x1), y0, y1));
        } catch (Exception exception) {
          System.err.println(Tensors.of(x0, x1, y0, y1));
        }
      }
      prev = next;
    }
  }

  public ClothoidTangentDefect clothoidTangentDefect() {
    return clothoidTangentDefect;
  }

  public Tensor defectsXY() {
    return Transpose.of(Tensors.of(probes, defects_real));
  }

  public Tensor lambdas() {
    return lambdas.unmodifiable();
  }
}
