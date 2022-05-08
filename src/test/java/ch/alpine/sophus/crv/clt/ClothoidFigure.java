// code by jph
package ch.alpine.sophus.crv.clt;

import java.io.IOException;

import ch.alpine.tensor.Parallelize;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.ext.HomeDirectory;
import ch.alpine.tensor.img.ColorDataGradients;
import ch.alpine.tensor.img.Raster;
import ch.alpine.tensor.io.Export;

/* package */ class ClothoidFigure {
  private static final ClothoidBuilder CLOTHOID_BUILDER = ClothoidBuilders.SE2_ANALYTIC.clothoidBuilder();
  private static final int RES = 192;
  private static final Tensor RE = Subdivide.of(-1, +1, RES - 1);
  private static final Tensor IM = Subdivide.of(+0.1, +2.1, RES - 1);
  // ---
  private final Scalar angle;

  public ClothoidFigure(Scalar angle) {
    this.angle = angle;
  }

  private Scalar function(int y, int x) {
    Tensor q = Tensors.of(RE.Get(x), IM.Get(y), angle);
    LagrangeQuadraticD headTailInterface = CLOTHOID_BUILDER.curve(q.map(Scalar::zero), q).curvature();
    return headTailInterface.maxAbs().reciprocal();
  }

  public static void main(String[] args) throws IOException {
    ClothoidFigure newtonDemo = new ClothoidFigure(RealScalar.of(2.6));
    Tensor matrix = Parallelize.matrix(newtonDemo::function, RES, RES);
    Tensor image = Raster.of(matrix, ColorDataGradients.SUNSET);
    Export.of(HomeDirectory.Pictures(ClothoidFigure.class.getSimpleName() + ".png"), image);
  }
}
