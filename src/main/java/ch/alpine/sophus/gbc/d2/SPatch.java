// code by jph
package ch.alpine.sophus.gbc.d2;

import ch.alpine.sophus.api.Genesis;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.OrderedQ;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.Sort;
import ch.alpine.tensor.alg.Tuples;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.red.FirstPosition;

/** TODO SOPHUS DOC reference Scotts work */
public class SPatch implements TensorUnaryOperator {
  private final int n;
  private final Genesis genesis;
  private final Tensor v;
  private final Tensor ls;

  /** @param n
   * @param d typically 2 */
  public SPatch(int n, int d, Genesis genesis) {
    this.n = n;
    this.genesis = genesis;
    v = CirclePoints.of(n);
    if (d != 2)
      throw new IllegalArgumentException("d=" + d);
    Tensor tensor = Tuples.of(Range.of(0, n), d);
    ls = Tensor.of(tensor.stream().filter(OrderedQ::of));
  }

  /** @param values e.g. (0,2,0,0,0)
   * @return */
  public int basis(Tensor rep) {
    return FirstPosition.of(getLs(), Sort.of(rep)).getAsInt();
  }

  public Tensor embed(Tensor rep) {
    return rep.stream() //
        .map(Scalar.class::cast) //
        .map(Scalar::number) //
        .mapToInt(Number::intValue) //
        .mapToObj(v::get) //
        .reduce(Tensor::add) //
        .orElseThrow().divide(RealScalar.of(rep.length()));
  }

  public Tensor getLs() {
    return ls;
  }

  public Tensor getEmbed() {
    return Tensor.of(ls.stream().map(this::embed));
  }

  @Override
  public Tensor apply(Tensor xy) {
    Tensor levers = Tensor.of(v.stream().map(p -> p.subtract(xy)));
    Tensor coords = genesis.origin(levers);
    Tensor matrix = Array.zeros(n, ls.length());
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < n; ++j) {
        int fj = j;
        Tensor rep = Tensors.vector(i, j);
        int index = basis(rep);
        matrix.set(coords.Get(fj), i, index);
      }
    }
    return coords.dot(matrix);
  }
}
