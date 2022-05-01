// code by jph
package ch.alpine.sophus.lie.he;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.d.DiscreteUniformDistribution;

class HeBarycenterTest {
  @Test
  public void test3dim() {
    Tensor p = Tensors.fromString("{{1}, {4}, 5}");
    Tensor q = Tensors.fromString("{{-3}, {6}, -9}");
    Tensor r = Tensors.fromString("{{2}, {1}, 8}");
    Tensor s = Tensors.fromString("{{-5}, {7}, -6}");
    HeBarycenter heBarycenter = new HeBarycenter(Tensors.of(p, q, r, s));
    assertEquals(heBarycenter.apply(p), UnitVector.of(4, 0));
    assertEquals(heBarycenter.apply(q), UnitVector.of(4, 1));
    assertEquals(heBarycenter.apply(r), UnitVector.of(4, 2));
    assertEquals(heBarycenter.apply(s), UnitVector.of(4, 3));
  }

  @Test
  public void test5dim() {
    Distribution distribution = DiscreteUniformDistribution.of(-20000, 20000);
    Tensor sequence = Tensors.vector(l -> Tensors.of(RandomVariate.of(distribution, 2), RandomVariate.of(distribution, 2), RandomVariate.of(distribution)), 6);
    HeBarycenter heBarycenter = new HeBarycenter(sequence);
    for (int count = 0; count < sequence.length(); ++count) {
      Tensor tensor = heBarycenter.apply(sequence.get(count));
      assertEquals(tensor, UnitVector.of(6, count));
      ExactTensorQ.require(tensor);
    }
  }
}
