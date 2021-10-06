// code by jph
package ch.alpine.sophus.ref.d1h;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.math.Exponential;
import ch.alpine.sophus.math.TensorIteration;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.ext.Integers;

/**  */
public class Hermite3Filter implements HermiteFilter {
  private final LieGroup lieGroup;
  private final Exponential exponential;
  private final BiinvariantMean biinvariantMean;

  /** @param lieGroup
   * @param exponential
   * @param biinvariantMean
   * @throws Exception if either parameters is null */
  public Hermite3Filter(LieGroup lieGroup, Exponential exponential, BiinvariantMean biinvariantMean) {
    this.lieGroup = Objects.requireNonNull(lieGroup);
    this.exponential = Objects.requireNonNull(exponential);
    this.biinvariantMean = Objects.requireNonNull(biinvariantMean);
  }

  @Override // from HermiteFilter
  public TensorIteration string(Scalar delta, Tensor control) {
    return new Control(delta, control).new StringIteration();
  }

  // BiinvariantMeans
  private static final Tensor CGW = Tensors.fromString("{1/128, 63/64, 1/128}");
  private static final Tensor CVW = Tensors.fromString("{-1/16, 3/4, -1/16}");

  private class Control {
    private Tensor control;
    private Scalar cgk;
    private Scalar cvk;

    private Control(Scalar delta, Tensor control) {
      this.control = control;
      cgk = RealScalar.of(256).divide(delta);
      cvk = RationalScalar.of(3, 16).divide(delta);
    }

    private Tensor center(Tensor p, Tensor q, Tensor r) {
      Tensor pg = p.get(0);
      Tensor pv = p.get(1);
      Tensor qg = q.get(0);
      Tensor qv = q.get(1);
      Tensor rg = r.get(0);
      Tensor rv = r.get(1);
      Tensor cg1 = biinvariantMean.mean(Unprotect.byRef(pg, qg, rg), CGW);
      Tensor cg2 = pv.subtract(rv).divide(cgk);
      Tensor cg = lieGroup.element(cg1).combine(cg2);
      Tensor log = exponential.log(lieGroup.element(pg).inverse().combine(rg)); // r - p
      Tensor cv1 = log.multiply(cvk);
      Tensor cv2 = CVW.dot(Unprotect.byRef(pv, qv, rv));
      Tensor cv = cv1.add(cv2);
      return Tensors.of(cg, cv);
    }

    private class StringIteration implements TensorIteration {
      @Override // from TensorIteration
      public Tensor iterate() {
        int length = control.length();
        List<Tensor> list = new ArrayList<>(length);
        Tensor p = control.get(0);
        list.add(p); // interpolation
        Tensor q = control.get(1);
        for (int index = 2; index < length; ++index) {
          Tensor r = control.get(index);
          list.add(center(p, q, r));
          p = q;
          q = r;
        }
        list.add(q);
        Integers.requireEquals(length, list.size());
        return control = Unprotect.using(list);
      }
    }
  }
}
