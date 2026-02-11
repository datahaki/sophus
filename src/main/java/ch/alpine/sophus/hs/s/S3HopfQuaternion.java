// code by jph
package ch.alpine.sophus.hs.s;

import java.io.Serializable;

import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.lie.rot.Quaternion;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.sca.pow.Sqrt;

public record S3HopfQuaternion(Quaternion q) implements Serializable {
  public S3HopfQuaternion {
    Tolerance.CHOP.requireClose(q.abs(), RealScalar.ONE);
  }

  public static S3HopfQuaternion of(Tensor xyzw) {
    return new S3HopfQuaternion(Quaternion.of(xyzw.Get(3), xyzw.extract(0, 3)));
  }

  public static final Quaternion I = Quaternion.of(0, 1, 0, 0);

  public static S3HopfQuaternion northernHemisphereGauge(Tensor xyz) {
    Scalar x = xyz.Get(0);
    Quaternion P = Quaternion.of(RealScalar.ZERO, xyz);
    Scalar num = RealScalar.ONE.add(P.multiply(I));
    Scalar den = Sqrt.FUNCTION.apply(RealScalar.ONE.add(x).multiply(RealScalar.TWO));
    IO.println(num);
    IO.println(den);
    return new S3HopfQuaternion((Quaternion) num.divide(den));
  }

  public Tensor project() {
    Quaternion p = q.multiply(I).divide(q);
    Tolerance.CHOP.requireZero(p.w());
    Tolerance.CHOP.requireClose(Vector2Norm.of(p.xyz()), RealScalar.ONE);
    return p.xyz();
  }

  public Tensor lift(Scalar angle) {
    Quaternion r = q.multiply(ComplexScalar.unit(angle));
    return Join.of(Tensors.of(r.w()), r.xyz());
  }
}
