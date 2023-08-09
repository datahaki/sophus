// code by jph
package ch.alpine.sophus.gbc.amp;

import ch.alpine.bridge.fig.Plot;
import ch.alpine.bridge.fig.Show;
import ch.alpine.bridge.fig.ShowDialog;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

enum SmoothRampDemo {
  ;
  public static Show various() {
    Show show = new Show();
    {
      Clip clip = Clips.absolute(10);
      show.add(Plot.of(new SmoothRamp(RealScalar.of(0.1)), clip)).setLabel("0.1");
      show.add(Plot.of(new SmoothRamp(RealScalar.of(0.3)), clip)).setLabel("0.3");
      show.add(Plot.of(new SmoothRamp(RealScalar.of(1.0)), clip)).setLabel("1.0");
    }
    return show;
  }

  public static void main(String[] args) {
    ShowDialog.of(various());
  }
}
