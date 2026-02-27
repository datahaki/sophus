// code by jph
package ch.alpine.sophus.rsm;

import java.io.Serializable;
import java.util.List;
import java.util.random.RandomGenerator;

import ch.alpine.sophus.api.TangentSpace;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.mat.pi.LinearSubspace;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

/** samples around origin of given tangent space with standard deviation sigma
 * and maps back to manifold */
public class LocalRandomSample implements RandomSampleInterface, Serializable {
  public static RandomSampleInterface of(TangentSpace tangentSpace, Scalar sigma) {
    return new LocalRandomSample(tangentSpace, sigma);
  }

  public static RandomSampleInterface of(TangentSpace tangentSpace, Number sigma) {
    return new LocalRandomSample(tangentSpace, RealScalar.of(sigma));
  }

  // ---
  private final TangentSpace tangentSpace;
  private final Distribution distribution;
  private final LinearSubspace linearSubspace;
  private final int n;

  private LocalRandomSample(TangentSpace tangentSpace, Scalar sigma) {
    this.tangentSpace = tangentSpace;
    List<Integer> list = Dimensions.of(tangentSpace.log(tangentSpace.basePoint()));
    distribution = NormalDistribution.of(sigma.zero(), sigma);
    linearSubspace = LinearSubspace.of(tangentSpace.isTangentQ()::defect, list);
    n = linearSubspace.dimensions();
  }

  @Override
  public Tensor randomSample(RandomGenerator randomGenerator) {
    return tangentSpace.exp(linearSubspace.apply(RandomVariate.of(distribution, randomGenerator, n)));
  }
}
