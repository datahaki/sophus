// code by jph
package ch.alpine.sophus.gbc.amp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import ch.alpine.bridge.fig.Show;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;

class SmoothRampTest {
  @Test
  void testZero() {
    Scalar scalar = new SmoothRamp(RealScalar.of(0.3)).apply(RealScalar.ZERO);
    assertEquals(scalar, RealScalar.ONE);
  }

  @Test
  void testExport(@TempDir File folder) throws IOException {
    Show show = SmoothRampDemo.various();
    show.export(new File(folder, "image.png"), new Dimension(800, 600));
  }
}
