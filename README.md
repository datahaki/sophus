![alpine_877](https://user-images.githubusercontent.com/4012178/116814864-1b1a1580-ab5b-11eb-97e6-1441af4ececa.png)

# ch.alpine.sophus

Library for non-linear geometry computations in Java.

![](https://github.com/datahaki/sophus/actions/workflows/mvn_test.yml/badge.svg)

The library was developed with the following objectives in mind
* trajectory design and motion planning for autonomous robots
* suitable for use in safety-critical real-time systems
* implementation of theoretical concepts with high level of abstraction

## Manifolds

##### Homogeneous Spaces

* n-dimensional sphere $S^n$
* n-dimensional hyperbolic space $H^n$
* Grassmann manifold `Gr(n,k)`
* n-dimensional projective space `RP^n`
* symmetric positive definite matrices `SPD(n)`
* Stiefel manifold `St(n,k)`

##### Lie Groups

* Euclidean space $R^n$
* Heisenberg group `He(n)`
* Special orthogonal group `SO(n)`
* Special orthogonal group `SL(n)`
* Special Euclidean groups `SE(2)`, `SE(2)` covering, `SE(n)`


## Contributors

Jan Hakenberg, Oliver Brinkmann, Joel Gächter

## References

* *Bi-invariant Means in Lie Groups. Application to Left-invariant Polyaffine Transformations.* by Vincent Arsigny, Xavier Pennec, Nicholas Ayache
* *Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups* by Xavier Pennec, Vincent Arsigny
* *Lie Groups for 2D and 3D Transformations* by Ethan Eade
* *Manifold-valued subdivision schemes based on geodesic inductive averaging* by Nira Dyn, Nir Sharon
* *Power Coordinates: A Geometric Construction of Barycentric Coordinates on Convex Polytopes* by Max Budninskiy, Beibei Liu, Yiying Tong, Mathieu Desbrun
* *Generalized Barycentric Coordinates in Computer Graphics and Computational Mechanics* by Kai Hormann, N. Sukumar 
* *Barycentric Subspace Analysis on Manifolds* by Xavier Pennec
* *A matrix-algebraic algorithm for the Riemannian logarithm on the Stiefel manifold under the canonical metric* by Ralf Zimmermann, 2017, https://arxiv.org/pdf/1604.05054.pdf
