// code by jph
package ch.alpine.sophus.crv;

import java.io.Serializable;
import java.util.stream.IntStream;

import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.N;

public abstract class AbstractBSplineInterpolation implements Serializable {
  private static final Chop CHOP_DEFAULT = Chop._12;
  private static final int MAXITER = 500;
  // ---
  protected final GeodesicSpace geodesicSpace;
  private final int degree;
  private final Tensor target;

  /** @param geodesicSpace corresponding to lie group
   * @param degree of underlying b-spline
   * @param target points to interpolate */
  public AbstractBSplineInterpolation(GeodesicSpace geodesicSpace, int degree, Tensor target) {
    this.geodesicSpace = geodesicSpace;
    this.degree = Integers.requirePositiveOrZero(degree);
    this.target = target;
  }

  public class Iteration {
    private final Tensor control;
    private final int steps;

    private Iteration(Tensor control, int steps) {
      this.control = control;
      this.steps = steps;
    }

    /** Gauss-Seidel-style updates, generally faster than Jacobi
     * 
     * @return */
    public Iteration stepGaussSeidel() {
      Tensor copy = control.copy();
      for (int index = 0; index < copy.length(); ++index) {
        // evaluate curve only at current parameter value
        Tensor refin = geodesicBSplineFunction(copy).apply(RealScalar.of(index));
        // update control point at given index
        copy.set(move(copy.get(index), refin, target.get(index)), index);
      }
      return new Iteration(copy, steps + 1);
    }

    /** Jacobi-style updates
     * 
     * @return */
    public Iteration stepJacobi() {
      // evaluate curve at all parameter values
      Tensor refine = Range.of(0, target.length()).map(geodesicBSplineFunction(control));
      return new Iteration(Tensor.of(IntStream.range(0, control.length()) //
          .mapToObj(index -> move(control.get(index), refine.get(index), target.get(index)))), steps + 1);
    }

    public Tensor control() {
      return control.unmodifiable();
    }

    /** @return iteration count */
    public int steps() {
      return steps;
    }
  }

  public final Iteration init() {
    return new Iteration(target, 0);
  }

  public final Iteration untilClose(Chop chop, int maxiter) {
    Iteration iteration = init();
    Tensor p = iteration.control();
    for (int count = 0; count < maxiter; ++count) {
      iteration = iteration.stepGaussSeidel();
      Tensor q = iteration.control();
      if (chop.isClose(p.map(N.DOUBLE), q))
        break;
      p = q;
    }
    return iteration;
  }

  public final Tensor apply() {
    return untilClose(CHOP_DEFAULT, MAXITER).control();
  }

  private GeodesicBSplineFunction geodesicBSplineFunction(Tensor control) {
    return GeodesicBSplineFunction.of(geodesicSpace, degree, control);
  }

  /** @param p previous control point position
   * @param e evaluated location of bspline curve
   * @param t target position of bspline curve
   * @return */
  protected abstract Tensor move(Tensor p, Tensor e, Tensor t);
}
