package ch.alpine.sophus.hs.s;

import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.num.ReIm;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.AbsSquared;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.Conjugate;

public class S3Hopf {
  public static Tensor hopf(Scalar z1, Scalar z2) {
    Scalar a1 = AbsSquared.FUNCTION.apply(z1);
    Scalar a2 = AbsSquared.FUNCTION.apply(z2);
    Tolerance.CHOP.requireClose(a1.add(a2), RealScalar.ONE);
    Scalar f1 = z1.multiply(Conjugate.FUNCTION.apply(z2));
    ReIm reIm = ReIm.of(f1.add(f1));
    Tensor hopf = reIm.vector().append(a1.subtract(a2));
    Scalar norm = Vector2Norm.of(hopf);
    Tolerance.CHOP.requireClose(norm, RealScalar.ONE);
    return hopf;
  }

  static void main() {
    Sphere sphere = new Sphere(3);
    Tensor xyza = RandomSample.of(sphere);
    Scalar z1 = ComplexScalar.of(xyza.Get(0), xyza.Get(1));
    Scalar z2 = ComplexScalar.of(xyza.Get(2), xyza.Get(3));
    Tensor h1 = hopf(z1, z2);
    IO.println(h1);
    Scalar expith = ComplexScalar.unit( //
        RandomVariate.of(UniformDistribution.of(Clips.absolute(Pi.VALUE))));
    Tensor h2 = hopf(z1.multiply(expith), z2.multiply(expith));
    IO.println(h2);
    Tolerance.CHOP.requireClose(h1, h2);
  }
}
