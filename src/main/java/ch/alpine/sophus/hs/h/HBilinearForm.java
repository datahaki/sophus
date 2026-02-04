package ch.alpine.sophus.hs.h;

import java.io.Serializable;

import ch.alpine.sophus.math.api.BilinearForm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

public record HBilinearForm(HWeierstrassCoordinate hWeierstrassCoordinate) implements BilinearForm, Serializable {
  public static HBilinearForm of(Tensor px) {
    return new HBilinearForm(new HWeierstrassCoordinate(px));
  }

  @Override
  public Scalar formEval(Tensor ux, Tensor vx) {
    return LBilinearForm.INSTANCE.formEval( //
        hWeierstrassCoordinate.toTangent(ux), //
        hWeierstrassCoordinate.toTangent(vx));
  }
}
