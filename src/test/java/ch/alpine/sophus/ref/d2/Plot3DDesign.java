// code by jph
package ch.alpine.sophus.ref.d2;

import java.io.IOException;

import ch.alpine.sophus.hs.r3s2.R3S2Geodesic;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.api.TensorScalarFunction;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.io.Put;
import ch.alpine.tensor.lie.Cross;
import ch.alpine.tensor.nrm.Normalize;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.red.Nest;
import ch.alpine.tensor.sca.AbsSquared;
import ch.alpine.tensor.sca.tri.Sin;

/* package */ class Plot3DDesign {
  private static final Scalar DT = RealScalar.of(0.01);
  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Vector2Norm::of);
  // ---
  private final TensorScalarFunction tensorScalarFunction;

  public Plot3DDesign(TensorScalarFunction tensorScalarFunction) {
    this.tensorScalarFunction = tensorScalarFunction;
  }

  public Tensor at(Number x, Number y) {
    return at(RealScalar.of(x), RealScalar.of(y));
  }

  public Tensor at(Scalar x, Scalar y) {
    Scalar fv = tensorScalarFunction.apply(Tensors.of(x, y));
    Tensor p = Tensors.of(x, y, fv).unmodifiable();
    Scalar fx = tensorScalarFunction.apply(Tensors.of(x.add(DT), y));
    Scalar fy = tensorScalarFunction.apply(Tensors.of(x, y.add(DT)));
    Tensor dx = Tensors.of(DT, RealScalar.ZERO, fx.subtract(fv));
    Tensor dy = Tensors.of(RealScalar.ZERO, DT, fy.subtract(fv));
    Tensor normal = NORMALIZE.apply(Cross.of(dx, dy));
    return Tensors.of(p, normal);
  }

  private static Scalar sin_xy2(Tensor xy) {
    return Sin.FUNCTION.apply(xy.Get(0).add(AbsSquared.FUNCTION.apply(xy.Get(1))));
  }

  public static void main(String[] args) throws IOException {
    Plot3DDesign plot3dDesign = new Plot3DDesign(Plot3DDesign::sin_xy2);
    Tensor matrix = Tensors.empty();
    for (Tensor x : Subdivide.of(0, 4, 12)) {
      Tensor row = Tensors.empty();
      for (Tensor y : Subdivide.of(0, 2, 8)) {
        row.append(plot3dDesign.at((Scalar) x, (Scalar) y));
      }
      matrix.append(row);
    }
    Put.of(HomeDirectory.file("sinxy2in.mathematica"), matrix);
    GeodesicCatmullClarkSubdivision catmullClarkSubdivision = new GeodesicCatmullClarkSubdivision(R3S2Geodesic.INSTANCE);
    Tensor tensor = Nest.of(catmullClarkSubdivision::refine, matrix, 3);
    Put.of(HomeDirectory.file("sinxy2.mathematica"), tensor);
  }
}
