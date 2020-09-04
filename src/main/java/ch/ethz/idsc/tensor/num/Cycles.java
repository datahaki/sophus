// code by jph
package ch.ethz.idsc.tensor.num;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ch.ethz.idsc.tensor.IntegerQ;
import ch.ethz.idsc.tensor.Integers;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Last;
import ch.ethz.idsc.tensor.alg.RotateLeft;
import ch.ethz.idsc.tensor.red.ArgMin;
import ch.ethz.idsc.tensor.red.Tally;
import ch.ethz.idsc.tensor.sca.Sign;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/Cycles.html">Cycles</a> */
public class Cycles implements Serializable {
  /** Example:
   * <pre>
   * tensor == {{1, 20}, {4, 10, 19, 6, 18}, {5, 9}, {7, 14, 13}}
   * </pre>
   * 
   * @param tensor
   * @return */
  public static Cycles of(Tensor tensor) {
    return new Cycles(map(normal(tensor)));
  }

  private static Map<Integer, Integer> map(Tensor cycles) {
    Map<Integer, Integer> map = new HashMap<>();
    for (Tensor cycle : cycles) {
      int prev = parse(Last.of(cycle).Get());
      Iterator<Tensor> iterator = cycle.iterator();
      while (iterator.hasNext())
        map.put(prev, prev = parse(iterator.next().Get()));
    }
    return map;
  }

  private static int parse(Scalar scalar) {
    return Integers.requirePositiveOrZero(Scalars.intValueExact(scalar));
  }

  private static Tensor normal(Tensor tensor) {
    Map<Scalar, Long> map = Tally.of(tensor.stream() //
        .flatMap(Tensor::stream) //
        .map(Scalar.class::cast));
    boolean value = map.keySet().stream() //
        .allMatch(scalar -> IntegerQ.of(scalar) && Sign.isPositiveOrZero(scalar));
    boolean allMatch = map.values().stream() //
        .allMatch(tally -> 1 == tally);
    if (value && allMatch)
      return Tensor.of(tensor.stream() //
          .filter(cycle -> 1 < cycle.length()) //
          .map(Cycles::minFirst) //
          .sorted((v1, v2) -> Scalars.compare(v1.Get(0), v2.Get(0))));
    throw TensorRuntimeException.of(tensor);
  }

  private static Tensor minFirst(Tensor vector) {
    return RotateLeft.of(vector, ArgMin.of(vector));
  }

  static Cycles of(Map<Integer, Integer> map) {
    Map<Integer, Integer> m = new HashMap<>();
    map.entrySet().stream() //
        .filter(entry -> entry.getKey() != entry.getValue()) //
        .forEach(entry -> m.put(entry.getKey(), entry.getValue()));
    return new Cycles(m);
  }

  /***************************************************/
  private final Map<Integer, Integer> map;

  private Cycles(Map<Integer, Integer> map) {
    this.map = map;
  }

  /** @return map without singletons, i.e. no trivial associations a -> a exist */
  public Map<Integer, Integer> map() {
    return Collections.unmodifiableMap(map);
  }

  /** @return for instance {{1, 20}, {4, 10, 19, 6, 18}, {5, 9}, {7, 14, 13}} */
  public Tensor toTensor() {
    Set<Integer> visited = new HashSet<>();
    Tensor cycles = Tensors.empty();
    for (Entry<Integer, Integer> entry : map.entrySet()) {
      int seed = entry.getKey();
      int next = entry.getValue();
      if (!visited.contains(seed)) {
        List<Integer> list = new ArrayList<>();
        while (visited.add(seed)) {
          list.add(seed);
          visited.add(seed);
          seed = next;
          next = map.get(next);
        }
        cycles.append(Tensors.vector(list));
      }
    }
    return Cycles.normal(cycles);
  }

  public Cycles product(Cycles cycles) {
    Map<Integer, Integer> b = cycles.map();
    Map<Integer, Integer> c = new HashMap<>();
    for (Entry<Integer, Integer> e : map.entrySet()) {
      int prev = e.getKey();
      int next = e.getValue();
      int foll = b.containsKey(next) ? b.get(next) : next;
      c.put(prev, foll);
    }
    for (Entry<Integer, Integer> e : b.entrySet()) {
      int prev = e.getKey();
      int next = e.getValue();
      if (!c.containsKey(prev))
        c.put(prev, next);
    }
    return of(c);
  }

  public Cycles inverse() {
    Map<Integer, Integer> flip = new HashMap<>();
    for (Entry<Integer, Integer> entry : map.entrySet())
      flip.put(entry.getValue(), entry.getKey());
    return new Cycles(flip);
  }

  @Override // from Object
  public boolean equals(Object object) {
    if (object instanceof Cycles) {
      Cycles cycles = (Cycles) object;
      return map.equals(cycles.map);
    }
    return false;
  }

  @Override // from Object
  public int hashCode() {
    return map.hashCode();
  }

  @Override // from Object
  public String toString() {
    return toTensor().toString();
  }
}
