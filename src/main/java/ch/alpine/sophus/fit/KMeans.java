// code by jph
package ch.alpine.sophus.fit;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.ext.ArgMin;

public class KMeans {
  private final Sedarim sedarim;
  private final BiinvariantMean biinvariantMean;
  private final Tensor sequence;
  // ---
  private Tensor seeds;
  private Tensor partition;

  public KMeans( //
      Sedarim sedarim, //
      BiinvariantMean biinvariantMean, //
      Tensor sequence) {
    this.sedarim = sedarim;
    this.biinvariantMean = biinvariantMean;
    this.sequence = sequence;
  }

  public void setSeeds(List<Integer> list) {
    this.seeds = Tensor.of(list.stream().map(sequence::get));
  }

  public void setSeeds(Tensor seeds) {
    this.seeds = seeds.copy();
  }

  public void iterate() {
    Tensor tensor = Transpose.of(Tensor.of(seeds.stream().map(sedarim::sunder)));
    List<Integer> list = tensor.stream().map(ArgMin::of).collect(Collectors.toList());
    partition = Array.of(i -> Tensors.empty(), seeds.length());
    AtomicInteger atomicInteger = new AtomicInteger();
    sequence.stream().forEach(point -> partition.set(entry -> entry.append(point), list.get(atomicInteger.getAndIncrement())));
    seeds = Tensor.of(partition.stream() //
        .filter(subset -> 0 < subset.length()) //
        .map(subset -> biinvariantMean.mean(subset, ConstantArray.of(RationalScalar.of(1, subset.length()), subset.length()))));
  }

  public Tensor partition() {
    return partition;
  }

  public Tensor seeds() {
    return seeds.unmodifiable();
  }
}
