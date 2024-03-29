// code by jph
package ch.alpine.sophus.ref.d1;

import java.util.ArrayList;
import java.util.List;

import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.chq.ScalarQ;
import ch.alpine.tensor.ext.Integers;

public class EightPointCurveSubdivision extends BSpline1CurveSubdivision {
  private static final Scalar PQ = RationalScalar.of(49, 44);
  private static final Scalar _R = RationalScalar.of(245, 201);
  private static final Scalar _S = RationalScalar.of(1225, 1024);
  // ---
  private final GeodesicSpace geodesicSpace;

  /** @param geodesicSpace */
  public EightPointCurveSubdivision(GeodesicSpace geodesicSpace) {
    super(geodesicSpace);
    this.geodesicSpace = geodesicSpace;
  }

  @Override // from CurveSubdivision
  public Tensor cyclic(Tensor tensor) {
    ScalarQ.thenThrow(tensor);
    int length = tensor.length();
    List<Tensor> list = new ArrayList<>(2 * length);
    for (int index = 0; index < length; ++index) {
      int first = Math.floorMod(index - 3, length);
      Tensor p = tensor.get((first + 0) % length);
      Tensor q = tensor.get((first + 1) % length);
      Tensor r = tensor.get((first + 2) % length);
      Tensor s = tensor.get(index);
      Tensor t = tensor.get((first + 4) % length);
      Tensor u = tensor.get((first + 5) % length);
      Tensor v = tensor.get((first + 6) % length);
      Tensor w = tensor.get((first + 7) % length);
      list.add(s);
      list.add(center(p, q, r, s, t, u, v, w));
    }
    Integers.requireEquals(list.size(), 2 * length);
    return Unprotect.using(list);
  }

  private Tensor center(Tensor p, Tensor q, Tensor r, Tensor s, Tensor t, Tensor u, Tensor v, Tensor w) {
    Tensor pq = geodesicSpace.split(p, q, PQ);
    Tensor _r = geodesicSpace.split(pq, r, _R);
    Tensor _s = geodesicSpace.split(_r, s, _S);
    // ---
    Tensor wv = geodesicSpace.split(w, v, PQ);
    Tensor _u = geodesicSpace.split(wv, u, _R);
    Tensor _t = geodesicSpace.split(_u, t, _S);
    return midpoint(_s, _t);
  }

  @Override // from CurveSubdivision
  public Tensor string(Tensor tensor) {
    throw new UnsupportedOperationException();
  }
}
