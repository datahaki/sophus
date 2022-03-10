// code by jph
package ch.alpine.sophus.lie.dt;

import java.io.Serializable;
import java.util.Random;

import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;

// TODO SOPHUS DtRandomSample
public class DtRandomSample implements RandomSampleInterface, Serializable {
  private final int n;
  private final Distribution l;
  private final Distribution t;

  public DtRandomSample(int n, Distribution l, Distribution t) {
    this.n = n;
    this.l = l;
    this.t = t;
  }
  // static Tensor spawn_St(Random random, int n) {
  // return Tensors.of(RandomVariate.of(DISTRIBUTION_L, random), RandomVariate.of(DISTRIBUTION_T, random, n));
  // }
  //
  // static Tensor spawn_st(int n) {
  // return Tensors.of(RandomVariate.of(DISTRIBUTION_T), RandomVariate.of(DISTRIBUTION_T, n));
  // }
  //
  // static Tensor spawn_St1() {
  // return Tensors.of(RandomVariate.of(DISTRIBUTION_L), RandomVariate.of(DISTRIBUTION_T));
  // }
  //
  // static Tensor spawn_st1() {
  // return Tensors.of(RandomVariate.of(DISTRIBUTION_T), RandomVariate.of(DISTRIBUTION_T));
  // }

  @Override
  public Tensor randomSample(Random random) {
    return Tensors.of(RandomVariate.of(l, random), RandomVariate.of(t, random, n));
  }
}
