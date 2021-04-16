package hr.fer.zemris.fractals;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Newton {

    /**
     * Class that will compute and produce data that GUI will show.
     */
    public static class SequentialProducer implements IFractalProducer {

        private ComplexRootedPolynomial polynomial;
        private ComplexPolynomial polynomialStandard;
        private ComplexPolynomial derived;

        private static final double CONVERGANCE_TRESHOLD = 0.001;
        private static final double ROOT_TRESHOLD = 0.002;

        public SequentialProducer(ComplexRootedPolynomial polynomial) {
            this.polynomial = polynomial;
            //System.out.println("Polynomial: " + polynomial);
            this.polynomialStandard = polynomial.toComplexPolynom();
            //System.out.println("Polynomial standard : " + polynomialStandard);
            this.derived = this.polynomialStandard.derive();
            //System.out.println("Polynomial derived: " + derived);
        }

        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                            int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
            int maxIter = 16 * 16 * 16;
            int offset = 0;
            double module;
            Complex zn, znold, numerator, denominator, fraction;
            short[] data = new short[width * height];

            for (int y = 0; y < height; y++) {
                if (cancel.get()) break;

                for (int x = 0; x < width; x++) {
                    int iter = 0;

                    double cre = x / (width - 1.0) * (reMax - reMin) + reMin;
                    double cim = (height - 1.0 - y) / (height - 1) * (imMax - imMin) + imMin;

                    zn = new Complex(cre, cim);

                    do {
                        numerator = polynomial.apply(zn);
                        denominator = derived.apply(zn);
                        znold = zn;
                        fraction = numerator.divide(denominator);
                        zn = zn.sub(fraction);
                        module = znold.sub(zn).module();
                        iter++;
                    } while (module > CONVERGANCE_TRESHOLD && iter < maxIter);

                    int index = polynomial.indexOfClosestRootFor(zn, ROOT_TRESHOLD);
                    data[offset++] = (short) (index + 1);
                }
            }
            observer.acceptResult(data, (short) (polynomialStandard.order() + 1), requestNo);
        }
    }


    public static void main(String... args) {
        System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.\n" +
                "Please enter at least two roots, one root per line. Enter 'done' when done.\n");
        System.out.println("NOTE: Valid form of sting representing a complex numbe is: (real) + (imaginary)i\n");

        Scanner sc = new Scanner(System.in);
        int rootCounter = 0;
        ComplexRootedPolynomial polynomial;
        Complex constant = null;
        List<Complex> roots = new ArrayList<>();

        while (true) {
            System.out.print("Root " + (rootCounter + 1) + ": ");
            String root = sc.nextLine().trim();

            if (root.equals("done")) {
                if (rootCounter < 2) {
                    System.err.println("You have entered less than 2 roots");
                }
                polynomial = new ComplexRootedPolynomial(Complex.ONE, roots);
                break;
            } else {
                roots.add(Complex.parse(root));
                rootCounter++;
            }
        }

        System.out.println("Image of fractal will appear shortly. Thank you.");
        FractalViewer.show(new SequentialProducer(polynomial));
    }
}
