package hr.fer.zemris.math;

import java.util.LinkedList;
import java.util.List;

/**
 * This class models complex numbers, each consisting of real and imaginary part.
 *
 * @author Korina Šimičević
 */
public class Complex {

    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex ONE = new Complex(1, 0);
    public static final Complex ONE_NEG = new Complex(-1, 0);
    public static final Complex IM = new Complex(0, 1);
    public static final Complex IM_NEG = new Complex(0, -1);

    private double re, im;

    /**
     * Creates a new complex number.
     *
     * @param re real component of complex number
     * @param im imaginary component of complex number
     */
    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public Complex() {
    }


    /**
     * Calculates the distance between the tip of complex number and it's starting point at (0, 0) in coordinate plane.
     *
     * @return distance of complex number from starting point
     */
    public double module() {
        return Math.hypot(re, im);
    }

    /**
     * Multiplies two complex numbers and returns the result in the form of new complex number.
     *
     * @param c complex number to be multiplied with the first one
     * @return new complex number that is the result of the operation
     */
    public Complex multiply(Complex c) {
        double resReal = this.re * c.re - this.im * c.im;
        double resImaginary = this.re * c.im + c.re * this.im;

        return new Complex(resReal, resImaginary);
    }

    /**
     * Divides two complex numbers and returns the result in the form of new complex number.
     *
     * @param c complex number to be divided from the first one
     * @return new complex number that is the result of the operation
     */
    public Complex divide(Complex c) {
        double resReal = (this.re * c.re + this.im * c.im) / (c.re * c.re + c.im * c.im);
        double resImaginary = (c.re * this.im - this.re * c.im) / (c.re * c.re + c.im * c.im);

        return new Complex(resReal, resImaginary);
    }

    /**
     * Adds two complex numbers and returns the result in the form of new complex number.
     *
     * @param c complex number to be added to the first one
     * @return new complex number that is the result of the operation
     */
    public Complex add(Complex c) {
        return new Complex(this.re + c.re, this.im + c.im);
    }

    /**
     * Subtracts two complex numbers and returns the result in the form of new complex number.
     *
     * @param c complex number to be subtracted from the first one
     * @return new complex number that is the result of the operation
     */
    public Complex sub(Complex c) {
        return new Complex(this.re - c.re, this.im - c.im);
    }

    /**
     * Negates a complex number.
     *
     * @return new complex number representing -this
     */
    public Complex negate() {
        return new Complex(-this.re, -this.im);
    }

    /**
     * Calculates the angle which complex number drawn in coordinate plane loses with x-axis.
     *
     * @return angle between complex number and x-axis
     */
    public double getAngle() {
        double angle = Math.atan(this.im / this.re);

        if (this.re >= 0 && this.im >= 0) {
            return angle;
        } else if (this.re < 0) {
            return angle + Math.PI;
        } else {
            return angle + Math.PI * 2;
        }
    }

    /**
     * Rises the complex number to the power of n.
     *
     * @param n power to which the complex number will be risen to
     * @return new complex number that is the result of the operation
     * @throws IllegalArgumentException if n is negative
     */
    public Complex power(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Cannot rise the complex number to negative power.");
        }

        double resMagnitude = Math.pow(this.module(), n);
        double resAngle = n * this.getAngle();

        double resReal = resMagnitude * Math.cos(resAngle);
        double resImaginary = resMagnitude * Math.sin(resAngle);

        return new Complex(resReal, resImaginary);
    }

    /**
     * Finds all n roots of complex number.
     *
     * @param n number of roots to be found
     * @return list of complex numbers that are the result of the operation
     * @throws IllegalArgumentException if n is negative
     */
    public List<Complex> root(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N in Nth root must be a positive integer.");
        }

        LinkedList<Complex> result = new LinkedList<>();

        double resMagnitude = Math.pow(this.module(), 1.0 / n);
        double angle = this.getAngle();

        double argument, resReal, resImaginary;
        for (int i = 0; i < n; i++) {
            argument = (angle + 2 * i * Math.PI) / n;
            resReal = resMagnitude * Math.cos(argument);
            resImaginary = resMagnitude * Math.sin(argument);

            result.add(new Complex(resReal, resImaginary));
        }

        return result;
    }

    /**
     * Takes the string in form of real + imaginary * i and creates a new complex number representing it.
     *
     * @param s string to be parse into complex number
     * @return new complex number parsed from the string
     * @throws IllegalArgumentException if string cannot be parsed
     */
    public static Complex parse(String s) {
        if (!s.matches("(.*)i")) {
            try {
                double number = Double.parseDouble(s);
                return fromReal(number);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Invalid format of string.");
            }
        } else {
            int indOfPlus = s.lastIndexOf("+");
            int indOfMin = s.lastIndexOf("-");

            String real = "0", imaginary = "0";
            if (indOfPlus > 0) {
                real = s.substring(0, indOfPlus);
                imaginary = s.substring(indOfPlus + 1, s.length() - 1).trim();
            } else if (indOfMin > 0) {
                real = s.substring(0, indOfMin);
                imaginary = "-" + s.substring(indOfMin + 1, s.length() - 1).trim();
            } else {
                imaginary = s.substring(0, s.length() - 1).trim();
            }

            if (imaginary.isEmpty()) {
                imaginary = "1";
            } else if (imaginary.equals("-")) {
                imaginary = "-1";
            }

            double resReal = Double.parseDouble(real);
            double resImaginary = Double.parseDouble(imaginary);

            return new Complex(resReal, resImaginary);
        }
    }

    /**
     * Creates a new complex number from given real number with it's imaginary component being equal to 0.
     *
     * @param real number to be made into new complex number
     * @return new complex number made from the real component
     */
    public static Complex fromReal(double real) {
        return new Complex(real, 0);
    }

    /**
     * Returns the string form of object.
     *
     * @return string representing complex number
     */
    @Override
    public String toString() {
        if (this.im >= 0) {
            return this.re + " + " + this.im + "i";
        } else {
            return this.re + " - " + Math.abs(this.im) + "i";
        }
    }

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }
}
