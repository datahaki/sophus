// adapted from chatgpt
// code by jph
package ch.alpine.sophus.rsm;

import java.util.Objects;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.ext.SwapRemoveList;
import ch.alpine.tensor.nrm.Vector2NormSquared;
import ch.alpine.tensor.opt.nd.BoxRandomSample;
import ch.alpine.tensor.opt.nd.CoordinateBoundingBox;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.sca.Ceiling;
import ch.alpine.tensor.sca.Floor;
import ch.alpine.tensor.sca.pow.Sqrt;

/** specialized to 2D */
public class PoissonDiskSampling {
  private final CoordinateBoundingBox cbb;
  private final Tensor min_negate;
  private final Scalar r2;
  private final Scalar cellSize;
  private final int attempts;
  private final RingRandomSample ringRandomSample;

  public PoissonDiskSampling(CoordinateBoundingBox cbb, Scalar r, int attempts) {
    this.cbb = cbb;
    this.min_negate = cbb.min().negate();
    this.r2 = r.multiply(r);
    cellSize = r.divide(Sqrt.FUNCTION.apply(RealScalar.TWO));
    this.attempts = Integers.requirePositive(attempts);
    ringRandomSample = new RingRandomSample(2, r, r.add(r));
  }

  public Tensor generate(RandomGenerator randomGenerator) {
    return new Inner().generate(randomGenerator);
  }

  public class Inner {
    private final SwapRemoveList<Tensor> swapRemoveList = new SwapRemoveList<>();
    private final int cols;
    private final int rows;
    private final Tensor[][] grid;
    int added = 0;

    public Inner() {
      // Step 0: Initialize the background grid
      cols = Ceiling.intValueExact(cbb.clip(0).length().divide(cellSize));
      rows = Ceiling.intValueExact(cbb.clip(1).length().divide(cellSize));
      grid = new Tensor[cols][rows];
    }

    public Tensor generate(RandomGenerator randomGenerator) {
      // Step 1: Select initial sample
      {
        BoxRandomSample boxRandomSample = new BoxRandomSample(cbb);
        Tensor point = RandomSample.of(boxRandomSample, randomGenerator);
        Tensor index = point.add(min_negate).divide(cellSize);
        int col = Floor.intValueExact(index.Get(0));
        int row = Floor.intValueExact(index.Get(1));
        addPoint(col, row, point);
      }
      // Step 2: Process active list
      while (!swapRemoveList.isEmpty()) {
        int index = randomGenerator.nextInt(swapRemoveList.size());
        Tensor ref = swapRemoveList.get(index);
        boolean found = RandomSample.stream(ringRandomSample, randomGenerator) //
            .limit(attempts) //
            .map(ref::add) //
            .anyMatch(this::register); // relies on quick exit
        if (!found)
          swapRemoveList.removeByIndex(index);
      }
      return Tensor.of(Stream.of(grid).flatMap(Stream::of).filter(Objects::nonNull), added);
    }

    private boolean register(Tensor point) {
      if (cbb.test(point)) {
        Tensor index = point.add(min_negate).divide(cellSize);
        int col = Floor.intValueExact(index.Get(0));
        int row = Floor.intValueExact(index.Get(1));
        // Check neighboring 5x5 cells in the grid
        for (int i = Math.max(0, col - 2); i <= Math.min(cols - 1, col + 2); i++)
          for (int j = Math.max(0, row - 2); j <= Math.min(rows - 1, row + 2); j++) {
            Tensor neighbor = grid[i][j];
            if (neighbor != null) {
              Scalar d = Vector2NormSquared.between(point, neighbor);
              if (Scalars.lessThan(d, r2))
                return false;
            }
          }
        addPoint(col, row, point);
        return true;
      }
      return false;
    }

    private void addPoint(int col, int row, Tensor point) {
      swapRemoveList.add(point);
      grid[col][row] = point;
      ++added;
    }
  }
}
