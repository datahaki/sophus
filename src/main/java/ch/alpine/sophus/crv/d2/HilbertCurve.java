// code by jph
package ch.alpine.sophus.crv.d2;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Function;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Cache;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.lie.r2.CirclePoints;

/** class does not have a public constructor
 * 
 * Reference:
 * "Hilbert curve - Representation as Lindenmayer system"
 * on Wikipedia, 2020
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/HilbertCurve.html">HilbertCurve</a> */
public class HilbertCurve {
  private static final int CACHE_SIZE = 6;
  private static final Function<Integer, Tensor> CACHE = Cache.of(HilbertCurve::build, CACHE_SIZE);

  /** @param n non-negative
   * @return */
  public static Tensor of(int n) {
    return CACHE.apply(Integers.requirePositiveOrZero(n));
  }

  private static Tensor build(int n) {
    return Tensor.of(new HilbertCurve(n).deque.stream());
  }

  // ---
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
