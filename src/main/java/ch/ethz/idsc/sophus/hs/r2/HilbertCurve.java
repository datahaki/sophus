// code by jph
package ch.ethz.idsc.sophus.hs.r2;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Function;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.ext.Cache;
import ch.ethz.idsc.tensor.lie.r2.CirclePoints;

/** class does not have a public constructor
 * 
 * Reference:
 * "Hilbert curve - Representation as Lindenmayer system"
 * on Wikipedia, 2020 */
public class HilbertCurve {
  private static final Function<Integer, Tensor> CACHE = Cache.of(HilbertCurve::build, 6);

  /** @param n non-negative
   * @return */
  public static Tensor of(int n) {
    return CACHE.apply(n);
  }

  private static Tensor build(int n) {
    return Tensor.of(new HilbertCurve(n).deque.stream());
  }

  /***************************************************/
  private static final Tensor[] MOVES = CirclePoints.of(4).stream().toArray(Tensor[]::new);
  // ---
  private final Deque<Tensor> deque = new ArrayDeque<>();
  private int curr = 0;
  private int last = Integer.MAX_VALUE;

  private HilbertCurve(int n) {
    deque.push(Tensors.vector(1, 1));
    a(n);
  }

  void a(int n) {
    if (--n < 0)
      return;
    r(+1); // -
    b(n); // b
    f(); // f
    r(-1); // +
    a(n); // a
    f(); // f
    a(n); // a
    r(-1); // +
    f(); // f
    b(n); // b
    r(+1); // -
  }

  void b(int n) {
    if (--n < 0)
      return;
    r(-1); // +
    a(n); // a
    f(); // f
    r(+1); // -
    b(n); // b
    f(); // f
    b(n); // b
    r(+1); // -
    f(); // f
    a(n); // a
    r(-1); // +
  }

  void r(int dir) {
    curr += dir;
  }

  void f() {
    Tensor point = deque.peek();
    if (last == curr)
      deque.pop();
    last = curr;
    deque.push(point.add(MOVES[Math.floorMod(curr, 4)]));
  }
}
