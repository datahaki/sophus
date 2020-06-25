// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.hs.spd.SpdMetric;
import ch.ethz.idsc.sophus.krg.Mahalanobis.Form;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Frobenius;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** for Rn and Sn the frobenius distance results in identical coordinates as the 2-norm distance
 * 
 * however, for SE(2) the frobenius and 2-norm coordinates do not match! */
// FIXME implementation is only valid for Rn!!!
// TODO move to test area since distances are not useful!!!
public abstract class PortableDistances implements TensorUnaryOperator {
  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return */
  public static PortableDistances frobenius(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return new PortableDistances(vectorLogManifold, variogram, sequence) {
      @Override
      protected Scalar distance(Tensor x, Tensor projection) {
        return Frobenius.between(x, projection);
      }
    };
  }

  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return */
  public static PortableDistances norm2(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return new PortableDistances(vectorLogManifold, variogram, sequence) {
      @Override
      protected Scalar distance(Tensor x, Tensor form) {
        return Norm._2.ofMatrix(x.subtract(form));
      }
    };
  }

  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return */
  public static PortableDistances geodesic(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return new PortableDistances(vectorLogManifold, variogram, sequence) {
      @Override
      protected Scalar distance(Tensor x, Tensor form) {
        return SpdMetric.INSTANCE.distance(x, form);
      }
    };
  }

  /***************************************************/
  private final Mahalanobis mahalanobis;
  private final ScalarUnaryOperator variogram;
  private final Tensor sequence;
  private final Tensor forms;

  /** @param vectorLogManifold
   * @param variogram
   * @param sequence */
  private PortableDistances(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    mahalanobis = new Mahalanobis(vectorLogManifold);
    this.variogram = variogram;
    this.sequence = sequence;
    forms = Tensor.of(sequence.stream() //
        .map(point -> mahalanobis.new Form(sequence, point)) //
        .map(Form::sigma_inverse));
  }

  @Override
  public Tensor apply(Tensor point) {
    Tensor form = mahalanobis.new Form(sequence, point).sigma_inverse();
    return Tensor.of(forms.stream().map(x -> distance(x, form)).map(variogram)); //
  }

  protected abstract Scalar distance(Tensor x, Tensor form);
}
