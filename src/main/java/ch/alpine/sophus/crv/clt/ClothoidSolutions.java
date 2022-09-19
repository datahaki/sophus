// code by jph
package ch.alpine.sophus.crv.clt;

import java.io.Serializable;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.itp.FindRoot;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.Re;
import ch.alpine.tensor.sca.Sign;

/** @param probes -min == max for tests to pass */
public record ClothoidSolutions(Tensor probes) implements Serializable {
  private static final Chop CHOP = Chop._08;

  /** @param clip
   * @return */
  public static ClothoidSolutions of(Clip clip) {
    return of(clip, 101);
  }

  /** @param clip
   * @param n
   * @return */
  public static ClothoidSolutions of(Clip clip, int n) {
    return new ClothoidSolutions(Subdivide.increasing(clip, n));
  }

  // ---
  public ClothoidSolutions(Tensor probes) {
    this.probes = probes.unmodifiable();
  }

  /** function is s1 odd
   * function is s2 even */
  public class Search implements Serializable {
    private final Tensor lambdas = Tensors.empty();
    public final Tensor defects_real;

    public Search(Scalar s1, Scalar s2) {
      ClothoidTangentDefect clothoidTangentDefect = ClothoidTangentDefect.of(s1, s2);
      ScalarUnaryOperator function = s -> Re.FUNCTION.apply(clothoidTangentDefect.apply(s));
      FindRoot findRoot = FindRoot.of(function, CHOP);
      Tensor defects = probes.map(clothoidTangentDefect);
      // System.out.println("defects=");
      // defects.stream().forEach(System.out::println);
      defects_real = defects.map(Re.FUNCTION);
      // Tensor defects_imag = defects.map(Imag.FUNCTION);
      boolean prev = Sign.isPositive(defects_real.Get(0));
      for (int index = 1; index < probes.length(); ++index) {
        boolean next = Sign.isPositive(defects_real.Get(index));
        if (prev && !next) {
          Scalar y0 = defects_real.Get(index - 1);
          Scalar y1 = defects_real.Get(index);
          try { // necessary because of degenerate input
            lambdas.append(findRoot.inside( //
                Clips.interval(probes.Get(index - 1), probes.Get(index)), //
                y0, //
                y1));
          } catch (Exception exception) {
            // ---
          }
        }
        prev = next;
      }
    }

    public Tensor lambdas() {
      return lambdas.unmodifiable();
    }
  }
}
