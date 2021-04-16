package hr.fer.zemris.math;

import java.util.List;

public class ComplexRootedPolynomial {
    private Complex constant;
    private List<Complex> roots;

    public ComplexRootedPolynomial(Complex constant, Complex... roots) {
        this.constant = constant;
        this.roots = List.of(roots);
    }

    public ComplexRootedPolynomial(Complex constant, List<Complex> roots) {
        this.constant = constant;
        this.roots = roots;
    }

    /**
     * Computes polynomial value at given point z
     *
     * @param z point at which polynomial value is computed
     * @return result of computing
     */
    public Complex apply(Complex z) {
        Complex result = constant;

        for (Complex root : roots) {
            result = result.multiply(z.sub(root));
        }

        return result;
    }

    /**
     * Finds and returns the index of closest root for given number z that is within treshold.
     *
     * @param z        complex number around which the method is looking for the closest root
     * @param treshold within complex number z
     * @return index of closest root or -1 if such doesn't exist
     */
    public int indexOfClosestRootFor(Complex z, double treshold) {
        int index = -1;
        double currentMin = Double.MAX_VALUE;
        double currentDistance;

        for (int i = 0; i < roots.size(); i++) {
            currentDistance = z.sub(roots.get(i)).module();
            if (treshold >= currentDistance && currentDistance < currentMin) {
                currentMin = currentDistance;
                index = i;
            }
        }

        return index;
    }
    /**
     * Converts a rooted polynomial to standard form.
     *
     * @return standard form of polynomial
     */
    public ComplexPolynomial toComplexPolynom() {
        ComplexPolynomial polynomial = new ComplexPolynomial(constant);

        for (Complex root : roots) {
            polynomial = polynomial.multiply(new ComplexPolynomial(root.negate(), Complex.ONE));
        }

        return polynomial;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String mul = " * ";

        sb.append("(").append(constant).append(")").append(mul);

        for (int i = 0; i < roots.size(); i++) {
            sb.append("(").append("z - (").append(roots.get(i)).append("))");

            if (i != roots.size() - 1) {
                sb.append(mul);
            }
        }

        return sb.toString();
    }

    public static void main(String... args) {
        ComplexRootedPolynomial crp = new ComplexRootedPolynomial(
                new Complex(2, 0), Complex.ONE, Complex.ONE_NEG, Complex.IM, Complex.IM_NEG
        );
        ComplexPolynomial cp = crp.toComplexPolynom();
        System.out.println(crp);
        System.out.println(cp);
        System.out.println(cp.derive());

    }
}
