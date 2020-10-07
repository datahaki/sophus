// code by jph
package ch.ethz.idsc.sophus.hs.r2;

import java.util.ArrayDeque;
import java.util.Deque;

import ch.ethz.idsc.tensor.Integers;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.Join;
import ch.ethz.idsc.tensor.lie.r2.CirclePoints;

public class HilbertCurve {
  private static final Tensor[] DIR = CirclePoints.of(4).stream().toArray(Tensor[]::new);

  /** @param n
   * @return */
  public static Tensor of(int n) {
    return Tensor.of(new HilbertCurve(n).deque.stream());
  }

  public static Tensor closed(int n) {
    Integers.requirePositive(n);
    Tensor ante = Tensors.vector((1 << n) - 1, -1);
    Tensor post = Tensors.vector(0, -1);
    Tensor tensor = of(n);
    if (n % 2 == 0)
      return Join.of(Tensors.of(ante), tensor, Tensors.of(post));
    tensor.set(ante, 0);
    tensor.set(post, tensor.length() - 1);
    return tensor;
  }

  /***************************************************/
  private final Deque<Tensor> deque = new ArrayDeque<>();
  private int curr = 0;
  private int last = Integer.MAX_VALUE;
  private Tensor point = Array.zeros(2);

  private HilbertCurve(int n) {
    deque.push(point);
    a(n);
  }

  public void a(int n) {
    --n;
    if (n < 0)
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

  public void b(int n) {
    --n;
    if (n < 0)
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

  public void r(int dir) {
    curr += dir;
  }

  public void f() {
    point = point.add(DIR[Math.floorMod(curr, 4)]);
    if (last == curr)
      deque.pop();
    last = curr;
    deque.push(point);
  }
}
