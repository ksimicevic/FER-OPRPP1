package hr.fer.zemris.fractals;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class NewtonParallel {

    /**
     * This class models a job that each worker will execute.
     */
    public static class CalculateJob implements Runnable {

        public static final CalculateJob NO_JOB = new CalculateJob();

        ComplexRootedPolynomial polynomial;
        double reMin;
        double reMax;
        double imMin;
        double imMax;
        int width;
        int height;
        int yMin;
        int yMax;
        int maxIter;
        short[] data;
        AtomicBoolean cancel;

        public CalculateJob(ComplexRootedPolynomial polynomial, double reMin, double reMax, double imMin, double imMax, int width, int height, int yMin, int yMax, int maxIter, short[] data, AtomicBoolean cancel) {
            this.polynomial = polynomial;
            this.reMin = reMin;
            this.reMax = reMax;
            this.imMin = imMin;
            this.imMax = imMax;
            this.width = width;
            this.height = height;
            this.yMin = yMin;
            this.yMax = yMax;
            this.maxIter = maxIter;
            this.data = data;
            this.cancel = cancel;
        }

        public CalculateJob() {
        }

        @Override
        public void run() {
            NewtonParallel.calculate(polynomial, reMin, reMax, imMin, imMax, width, height, maxIter, yMin, yMax, data, cancel);
        }
    }


    /**
     * Method that all workers will execute.
     *
     * @param polynomial
     * @param reMin
     * @param reMax
     * @param imMin
     * @param imMax
     * @param width
     * @param height
     * @param maxIter
     * @param ymin
     * @param ymax
     * @param data
     * @param cancel
     */
    public static void calculate(ComplexRootedPolynomial polynomial, double reMin, double reMax, double imMin, double imMax, int width, int height, int maxIter, int ymin, int ymax, short[] data, AtomicBoolean cancel) {
        ComplexPolynomial polynomialStandard = polynomial.toComplexPolynom();
        ComplexPolynomial derived = polynomialStandard.derive();

        int offset = ymin * width;
        double module;
        Complex zn, znold, numerator, denominator, fraction;

        for (int y = ymin; y <= ymax; y++) {
            if (cancel.get()) break;

            for (int x = 0; x < width; x++) {
                int iter = 0;

                double cre = (double) x / ((double) width - 1.0D) * (reMax - reMin) + reMin;
                double cim = (double) (height - 1 - y) / ((double) height - 1.0D) * (imMax - imMin) + imMin;

                zn = new Complex(cre, cim);

                do {
                    numerator = polynomial.apply(zn);
                    denominator = derived.apply(zn);
                    znold = zn;
                    fraction = numerator.divide(denominator);
                    zn = zn.sub(fraction);
                    module = znold.sub(zn).module();
                    iter++;
                } while (module > ParallelProducer.CONVERGANCE_TRESHOLD && iter < maxIter);

                int index = polynomial.indexOfClosestRootFor(zn, ParallelProducer.ROOT_TRESHOLD);
                data[offset] = (short) (index + 1);
                ++offset;
            }
        }
    }


    /**
     * Unlike in sequential mode, ParallelProducer will produce workers and assign them jobs.
     */
    public static class ParallelProducer implements IFractalProducer {
        private ComplexRootedPolynomial polynomial;

        private int numberOfWorkers;
        private int numberOfTracks;

        public static final double CONVERGANCE_TRESHOLD = 0.001;
        public static final double ROOT_TRESHOLD = 0.002;

        public ParallelProducer(ComplexRootedPolynomial polynomial, int numberOfWorkers, int numberOfTracks) {
            this.polynomial = polynomial;
            this.numberOfTracks = numberOfTracks;
            this.numberOfWorkers = numberOfWorkers;
        }

        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                            int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
            int maxIter = 16 * 16 * 16;

            if (numberOfTracks > height) {
                numberOfTracks = height;
            }

            int yPerTrack = height / numberOfTracks;

            short[] data = new short[width * height];

            final BlockingQueue<CalculateJob> jobs = new LinkedBlockingQueue<>();

            Thread[] workers = new Thread[numberOfWorkers];

            for (int i = 0; i < workers.length; i++) {
                workers[i] = new Thread(() -> {
                    while (true) {
                        CalculateJob job = null;
                        try {
                            job = jobs.take();

                            if (job == CalculateJob.NO_JOB) break;

                        } catch (InterruptedException ignored) {
                            //CONTINUE
                        }
                        assert job != null;
                        job.run();
                    }
                });
            }

            for (Thread worker : workers) {
                worker.start();
            }

            for (int i = 0; i < numberOfTracks; i++) {
                int yMin = i * yPerTrack;
                int yMax = (i + 1) * yPerTrack - 1;

                if (i == numberOfTracks - 1) {
                    yMax = height - 1;
                }

                CalculateJob job = new CalculateJob(polynomial, reMin, reMax, imMin, imMax, width, height, yMin, yMax, maxIter, data, cancel);

                while (true) {
                    try {
                        jobs.put(job);
                        break;
                    } catch (InterruptedException ignored) {
                        //CONTINUE
                    }
                }
            }

            for (int i = 0; i < workers.length; i++) {
                while (true) {
                    try {
                        jobs.put(CalculateJob.NO_JOB);
                        break;
                    } catch (InterruptedException ignored) {
                        //CONTINUE
                    }
                }
            }

            for (Thread worker : workers) {
                while (true) {
                    try {
                        worker.join();
                        break;
                    } catch (InterruptedException e) {
                        //CONTINUE
                    }
                }
            }

            observer.acceptResult(data, (short) (polynomial.toComplexPolynom().order() + 1), requestNo);
        }
    }

    public static void main(String... args) {
        int numberOfWorkers, numberOfTracks;

        numberOfWorkers = Runtime.getRuntime().availableProcessors();
        numberOfTracks = numberOfWorkers * 4;


        if (args.length != 0) {
            String firstParameter = args[0].trim();
            if (firstParameter.startsWith("--")) {
                int number = Integer.parseInt(firstParameter.substring(firstParameter.indexOf("=") + 1));
                if (firstParameter.substring(2, firstParameter.indexOf('=')).equals("workers")) {
                    numberOfWorkers = number;
                } else {
                    numberOfTracks = number;
                }
            } else {
                if (firstParameter.charAt(1) == 't') {
                    numberOfTracks = firstParameter.charAt(2);
                } else {
                    numberOfWorkers = firstParameter.charAt(2);
                }
            }

            if (args.length == 2) {
                String secondParameter = args[1].trim();

                if (firstParameter.startsWith("--")) {
                    int number = Integer.parseInt(firstParameter.substring(firstParameter.indexOf("=") + 1));
                    if (firstParameter.substring(2, firstParameter.indexOf('=')).equals("workers")) {
                        numberOfWorkers = number;
                    } else {
                        numberOfTracks = number;
                    }
                } else {
                    if (firstParameter.charAt(1) == 't') {
                        numberOfTracks = firstParameter.charAt(2);
                    } else {
                        numberOfWorkers = firstParameter.charAt(2);
                    }
                }
            }
        }

        System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.\n" +
                "Please enter at least two roots, one root per line. Enter 'done' when done.\n");
        System.out.println("NOTE: Valid form of string representing a complex numbe is: (real) + (imaginary)i\n");

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
        FractalViewer.show(new ParallelProducer(polynomial, numberOfWorkers, numberOfTracks));
    }
}
