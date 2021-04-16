package hr.fer.oprpp1.hw1;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * This class models complex numbers, each consisting of real and imaginary part.
 *
 * @author Korina Šimičević
 */
public class ComplexNumber {

    private final double real;
    private final double imaginary;

    /**
     * Creates a new complex number.
     *
     * @param real      real component of complex number
     * @param imaginary imaginary component of complex number
     */
    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    /**
     * Creates a new complex number from given real number with it's imaginary component being equal to 0.
     *
     * @param real number to be made into new complex number
     * @return new complex number made from the real component
     */
    public static ComplexNumber fromReal(double real) {
        return new ComplexNumber(real, 0);
    }

    /**
     * Creates a new complex number from given imaginary number with it's real component being equal to 0.
     *
     * @param imaginary number to be made into new complex number
     * @return new complex number made from the real component
     */
    public static ComplexNumber fromImaginary(double imaginary) {
        return new ComplexNumber(0, imaginary);
    }

    /**
     * Creates a new complex number from given magnitude and angle.
     *
     * @param magnitude distance from (0, 0) in coordinate plane
     * @param angle     between complex number drawn in coordinate plane and x-axis
     * @return new complex number from given magnitude and angle
     */
    public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
        double resReal = magnitude * Math.cos(angle);
        double resImaginary = magnitude * Math.sin(angle);

        return new ComplexNumber(resReal, resImaginary);
    }

    /**
     * Takes the string in form of real + imaginary * i and creates a new complex number representing it.
     *
     * @param s string to be parse into complex number
     * @return new complex number parsed from the string
     * @throws IllegalArgumentException if string cannot be parsed
     */
    public static ComplexNumber parse(String s) {
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

            double resReal = Double.parseDouble(real);
            double resImaginary = Double.parseDouble(imaginary);

            return new ComplexNumber(resReal, resImaginary);
        }
    }

    /**
     * Returns the real component of complex number.
     *
     * @return real component
     */
    public double getReal() {
        return real;
    }

    /**
     * Returns the imaginary component of complex number.
     *
     * @return imaginary component
     */
    public double getImaginary() {
        return imaginary;
    }

    /**
     * Calculates the distance between the tip of complex number and it's starting point at (0, 0) in coordinate plane.
     *
     * @return distance of complex number from starting point
     */
    public double getMagnitude() {
        return Math.hypot(this.real, this.imaginary);
    }

    /**
     * Calculates the angle which complex number drawn in coordinate plane loses with x-axis.
     *
     * @return angle between complex number and x-axis
     */
    public double getAngle() {
        double angle = Math.atan(this.imaginary / this.real);

        if (this.real >= 0 && this.imaginary >= 0) {
            return angle;
        } else if (this.real < 0) {
            return angle + Math.PI;
        } else {
            return angle + Math.PI * 2;
        }
    }


    /**
     * Adds two complex numbers and returns the result in the form of new complex number.
     *
     * @param c complex number to be added to the first one
     * @return new complex number that is the result of the operation
     */
    public ComplexNumber add(ComplexNumber c) {
        return new ComplexNumber(this.real + c.real, this.imaginary + c.imaginary);
    }

    /**
     * Subtracts two complex numbers and returns the result in the form of new complex number.
     *
     * @param c complex number to be subtracted from the first one
     * @return new complex number that is the result of the operation
     */
    public ComplexNumber sub(ComplexNumber c) {
        return new ComplexNumber(this.real - c.real, this.imaginary - c.imaginary);
    }

    /**
     * Multiplies two complex numbers and returns the result in the form of new complex number.
     *
     * @param c complex number to be multiplied with the first one
     * @return new complex number that is the result of the operation
     */
    public ComplexNumber mul(ComplexNumber c) {
        double resReal = this.real * c.real - this.imaginary * c.imaginary;
        double resImaginary = this.real * c.imaginary + c.real * this.imaginary;

        return new ComplexNumber(resReal, resImaginary);

    }

    /**
     * Divides two complex numbers and returns the result in the form of new complex number.
     *
     * @param c complex number to be divided from the first one
     * @return new complex number that is the result of the operation
     */
    public ComplexNumber div(ComplexNumber c) {
        double resReal = (this.real * c.real + this.imaginary * c.imaginary) / (c.real * c.real + c.imaginary * c.imaginary);
        double resImaginary = (c.real * this.imaginary - this.real * c.imaginary) / (c.real * c.real + c.imaginary * c.imaginary);

        return new ComplexNumber(resReal, resImaginary);
    }

    /**
     * Rises the complex number to the power of n.
     *
     * @param n power to which the complex number will be risen to
     * @return new complex number that is the result of the operation
     * @throws IllegalArgumentException if n is negative
     */
    public ComplexNumber power(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Cannot rise the complex number to negative power.");
        }

        double resMagnitude = Math.pow(this.getMagnitude(), n);
        double resAngle = n * this.getAngle();

        double resReal = resMagnitude * Math.cos(resAngle);
        double resImaginary = resMagnitude * Math.sin(resAngle);

        return new ComplexNumber(resReal, resImaginary);
    }

    /**
     * Finds all n roots of complex number.
     *
     * @param n number of roots to be found
     * @return array of complex numbers that are the result of the operation
     * @throws IllegalArgumentException if n is negative
     */
    public ComplexNumber[] root(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N in Nth root must be a positive integer.");
        }

        ComplexNumber[] result = new ComplexNumber[n];

        double resMagnitude = Math.pow(this.getMagnitude(), 1.0 / n);
        double angle = this.getAngle();

        double argument, resReal, resImaginary;
        for (int i = 0; i < n; i++) {
            argument = (angle + 2 * i * Math.PI) / n;
            resReal = resMagnitude * Math.cos(argument);
            resImaginary = resMagnitude * Math.sin(argument);

            result[i] = new ComplexNumber(resReal, resImaginary);
        }

        return result;
    }

    /**
     * Returns the string form of object.
     *
     * @return string representing complex number
     */
    @Override
    public String toString() {
        if (this.imaginary >= 0) {
            return this.real + " + " + this.imaginary + "i";
        } else {
            return this.real + " - " + Math.abs(this.imaginary) + "i";
        }
    }

    /**
     * Returns true if two objects are equal.
     * @param obj that we are checking if it's equal
     *
     * @return true if two objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof ComplexNumber) {
            ComplexNumber c = (ComplexNumber) obj;

            MathContext context = new MathContext(2);

            BigDecimal real1 = new BigDecimal(this.real).round(context);
            BigDecimal img1 = new BigDecimal(this.imaginary).round(context);

            BigDecimal real2 = new BigDecimal(c.real).round(context);
            BigDecimal img2 = new BigDecimal(c.imaginary).round(context);

            return real1.compareTo(real2) == 0 && img1.compareTo(img2) == 0;
        }

        return false;
    }

}
