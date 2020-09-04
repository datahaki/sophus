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
import java.util.stream.Collectors;

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

  private static Map<Integer, Integer> map(Tensor tensor) {
    Map<Integer, Integer> map = new HashMap<>();
    for (Tensor cycle : tensor) {
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

  /***************************************************/
  private static final Cycles IDENTITY = new Cycles(Collections.emptyMap());

  /** @return */
  public static Cycles identity() {
    return IDENTITY;
  }

  /***************************************************/
  /* package */ static Cycles from(Map<Integer, Integer> map) {
    return new Cycles(map.entrySet().stream() //
        .filter(entry -> !entry.getKey().equals(entry.getValue())) //
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue)));
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
    Set<Integer> set = new HashSet<>();
    Tensor cycles = Tensors.empty();
    for (Entry<Integer, Integer> entry : map.entrySet()) {
      int seed = entry.getKey();
      int next = entry.getValue();
      if (!set.contains(seed)) {
        List<Integer> list = new ArrayList<>();
        while (set.add(seed)) {
          list.add(seed);
          next = map.get(seed = next);
        }
        cycles.append(Tensors.vector(list));
      }
    }
    return normal(cycles);
  }

  /** @param cycles
   * @return */
  public Cycles product(Cycles cycles) {
    Map<Integer, Integer> b_map = cycles.map;
    Map<Integer, Integer> result = new HashMap<>();
    for (Entry<Integer, Integer> entry : map.entrySet()) {
      int next = entry.getValue();
      result.put(entry.getKey(), b_map.containsKey(next) ? b_map.get(next) : next);
    }
    for (Entry<Integer, Integer> entry : b_map.entrySet()) {
      int prev = entry.getKey();
      if (!result.containsKey(prev))
        result.put(prev, entry.getValue());
    }
    return from(result);
  }

  /** @return */
  public Cycles inverse() {
    Map<Integer, Integer> result = new HashMap<>();
    for (Entry<Integer, Integer> entry : map.entrySet())
      result.put(entry.getValue(), entry.getKey());
    return new Cycles(result);
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
