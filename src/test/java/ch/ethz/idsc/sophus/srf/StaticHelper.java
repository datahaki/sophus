// code by jph
package ch.ethz.idsc.sophus.srf;

import java.io.File;
import java.io.IOException;

import ch.ethz.idsc.tensor.ext.HomeDirectory;
import ch.ethz.idsc.tensor.img.ArrayPlot;
import ch.ethz.idsc.tensor.img.ColorDataGradient;
import ch.ethz.idsc.tensor.io.Export;

/* package */ enum StaticHelper {
  ;
  private static final int GALLERY_RES = 720; // 128 + 64;
  private static final File DIRECTORY = HomeDirectory.file("Projects", "latex", "images", "tensor");
  static {
    DIRECTORY.mkdirs();
  }

  static File image(Class<?> cls) {
    return new File(DIRECTORY, cls.getSimpleName() + ".png");
  }

  public static void export(BivariateEvaluation bivariateEvaluation, Class<?> cls, ColorDataGradient colorDataGradient) throws IOException {
    Export.of(image(cls), ArrayPlot.of(bivariateEvaluation.image(GALLERY_RES), colorDataGradient));
  }
}
