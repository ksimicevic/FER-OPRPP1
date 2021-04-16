package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;

public class ComplexPolynomial {
    private List<Complex> factors;

    public ComplexPolynomial(Complex... factors) {
        this.factors = List.of(factors);
    }

    public ComplexPolynomial(List<Complex> factors) {
        this.factors = factors;
    }

    /**
     * Returns the order of polynomial.
     *
     * @return order of polynomial
     */
    public short order() {
        return (short) (factors.size() - 1);
    }

    /**
     * Computes polynomial value at given point z
     *
     * @param z point at which polynomial value is computed
     * @return result of computing
     */
    public Complex apply(Complex z) {
        Complex result = factors.get(0);

        for (int i = 0; i < factors.size(); i++) {
            if (i != 0) {
                result = result.add(factors.get(i).multiply(z.power(i)));
            }
        }

        return result;
    }

    /**
     * Multiplies two polynomials and returns the result of multiply operation.
     *
     * @param p other polynomial to multiply
     * @return result of multiply operation
     */
    public ComplexPolynomial multiply(ComplexPolynomial p) {
        int newOrder = this.order() + p.order();

        List<Complex> newFactors = new ArrayList<>();
        List<Complex> pFactors = p.getFactors();

        for (int i = 0; i <= newOrder; i++) {
            newFactors.add(i, Complex.ZERO);
        }

        for (int i = 0; i <= this.order(); i++) {
            for (int j = 0; j <= p.order(); j++) {
                newFactors.set(i + j, newFactors.get(i + j).add(factors.get(i).multiply(pFactors.get(j))));
            }
        }

        return new ComplexPolynomial(newFactors);
    }


    /**
     * Derives this complex polynomial.
     *
     * @return derive of polynomial
     */
    public ComplexPolynomial derive() {
        List<Complex> newFactors = new ArrayList<>();

        for (int i = 1; i <= this.order(); i++) {
            newFactors.add(i - 1, factors.get(i).multiply(new Complex(i, 0)));
        }

        return new ComplexPolynomial(newFactors);
    }


    public List<Complex> getFactors() {
        return factors;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = factors.size() - 1; i >= 0; i--) {
            sb.append("(").append(factors.get(i)).append(")*z^").append(i);

            if (i != 0) {
                sb.append(" + ");
            }
        }

        return sb.toString();
    }
}
