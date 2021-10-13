// code by jph
package ch.alpine.sophus.crv.d2;

import ch.alpine.sophus.ref.d1.CurveSubdivision;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;

/* package */ enum ControlLr2 implements CurveSubdivision {
  INSTANCE;

  private static final Scalar _1_4 = RationalScalar.of(1, 4);

  @Override // from CurveSubdivision
  public Tensor cyclic(Tensor tensor) {
    if (Tensors.isEmpty(tensor))
      return Tensors.empty();
    Tensor result = Tensors.reserve(tensor.length());
    int n = tensor.length();
    for (int index = 0; index < n; ++index) {
      Tensor p = tensor.get(Math.floorMod(index - 1, n));
      Tensor q = tensor.get(Math.floorMod(index, n));
      Tensor r = tensor.get(Math.floorMod(index + 1, n));
      result.append(p.add(q).add(q).add(r).multiply(_1_4));
    }
    return result;
  }

  @Override // from CurveSubdivision
  public Tensor string(Tensor tensor) {
    throw new RuntimeException();
  }
}
