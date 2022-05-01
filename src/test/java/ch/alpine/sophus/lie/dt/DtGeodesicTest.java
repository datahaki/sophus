// code by ob
package ch.alpine.sophus.lie.dt;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

class DtGeodesicTest {
  @Test
  public void testSt1Simple() {
    Tensor split = DtGeodesic.INSTANCE.split(Tensors.vector(5, 1), Tensors.vector(10, 0), RealScalar.of(0.7));
    Chop._13.requireClose(split, Tensors.vector(8.122523963562355, 0.37549520728752905));
  }

  @Test
  public void testSt1Zero() {
    Tensor split = DtGeodesic.INSTANCE.split(Tensors.vector(5, 1), Tensors.vector(10, 0), RealScalar.of(0));
    Chop._13.requireClose(split, Tensors.vector(5, 1));
  }

  @Test
  public void testSt1One() {
    Tensor split = DtGeodesic.INSTANCE.split(Tensors.vector(5, 1), Tensors.vector(10, 0), RealScalar.of(1));
    Chop._13.requireClose(split, Tensors.vector(10, 0));
  }

  @Test
  public void testSt1General() {
    Tensor p = Tensors.vector(3, 6);
    Tensor q = Tensors.vector(4, 10);
    Clip clip_l = Clips.interval(3, 4);
    Clip clip_t = Clips.interval(6, 10);
    for (Tensor x : Subdivide.of(0, 1, 20)) {
      Tensor split = DtGeodesic.INSTANCE.split(p, q, (Scalar) x);
      clip_l.requireInside(split.Get(0));
      clip_t.requireInside(split.Get(1));
    }
  }

  @Test
  public void testSimple() {
    Tensor p = Tensors.of(RealScalar.of(5), Tensors.vector(1, 0, 2));
    Tensor q = Tensors.of(RealScalar.of(10), Tensors.vector(7, -3, 2));
    Tensor split = DtGeodesic.INSTANCE.split(p, q, RealScalar.of(0.7));
    Chop._13.requireClose(split, Tensors.of(RealScalar.of(8.122523963562355), Tensors.vector(4.747028756274826, -1.8735143781374128, 2.0)));
  }

  @Test
  public void testZero() {
    Tensor p = Tensors.of(RealScalar.of(5), Tensors.vector(1, 0, 2));
    Tensor q = Tensors.of(RealScalar.of(10), Tensors.vector(7, -3, 2));
    Tensor split = DtGeodesic.INSTANCE.split(p, q, RealScalar.of(0));
    Chop._13.requireClose(split, p);
  }

  @Test
  public void testOne() {
    Tensor p = Tensors.of(RealScalar.of(5), Tensors.vector(1, 0, 2));
    Tensor q = Tensors.of(RealScalar.of(10), Tensors.vector(7, -3, 2));
    Tensor split = DtGeodesic.INSTANCE.split(p, q, RealScalar.of(1));
    Chop._13.requireClose(split, q);
  }

  @Test
  public void testGeneral() {
    Tensor p = Tensors.of(RealScalar.of(3), Tensors.vector(6, -2));
    Tensor q = Tensors.of(RealScalar.of(4), Tensors.vector(10, 3));
    Clip clip_l = Clips.interval(3, 4);
    Clip clip_t1 = Clips.interval(6, 10);
    Clip clip_t2 = Clips.interval(-2, 3);
    for (Tensor x : Subdivide.of(0, 1, 20)) {
      Tensor split = DtGeodesic.INSTANCE.split(p, q, (Scalar) x);
      clip_l.requireInside(split.Get(0));
      clip_t1.requireInside((split.get(1)).Get(0));
      clip_t2.requireInside(split.Get(1, 1));
    }
  }

  @Test
  public void testBiinvariantMean() {
    Tensor p = Tensors.fromString("{1, {2, 3}}");
    Tensor q = Tensors.fromString("{2, {3, 1}}");
    Tensor domain = Subdivide.of(0, 1, 10);
    Tensor st1 = domain.map(DtGeodesic.INSTANCE.curve(p, q));
    ScalarTensorFunction mean = //
        w -> DtBiinvariantMean.INSTANCE.mean(Tensors.of(p, q), Tensors.of(RealScalar.ONE.subtract(w), w));
    Tensor st2 = domain.map(mean);
    Chop._12.requireClose(st1, st2);
  }
}
