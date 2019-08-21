package org.mavriksc.messin;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class FlySwatter {
    private static final String FILE_NAME = "C-sample.in";
    private static List<Case> cases = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        getInput();
        List<String> answers = new ArrayList<>();
        cases.forEach(c -> answers.add(String.format("Case #%d: %.6f", cases.indexOf(c) + 1, simulateCase(c))));
        answers.forEach(System.out::println);
    }

    private static double simulateCase(Case c) {
        double p = 2, lastP;
        double epsilon = 0.00000000001;
        int count = 0;
        int MAX_SIMS = 1_000_000;
        do {
            lastP = p;
            count++;
            double ans = runSimsParallel(c);
            //System.out.println(String.format("Case #%d,trial #%d p: %.6f", cases.indexOf(c) + 1, count, ans));
            p += (ans - p) / count;
        } while (count < MAX_SIMS && Math.abs(p - lastP) > epsilon);
        System.out.println(String.format("Count needed:%d Last adjustment size : %.15f", count, Math.abs(p - lastP)));
        return p;
    }

    private static double runSims(Case c) {
        int numTrials = 10_000_000;
        int hits = 0;
        for (int i = 0; i < numTrials; i++) {

            hits += didItHit(c) ? 1 : 0;
        }
        return (double) hits / numTrials;
    }

    private static double runSimsParallel(Case c) {
        int numTrials = 10_000_000;
        long hits =  IntStream.range(0,numTrials).parallel()
                .filter(i->didItHit(c))
                .count();
        return hits /(double) numTrials;
    }

    private static boolean didItHit(Case c) {
        double angle = ThreadLocalRandom.current().nextDouble(Math.PI / 2);
        //double mag = ThreadLocalRandom.current().nextDouble(c.R);
        double mag = Math.sqrt(ThreadLocalRandom.current().nextDouble()) * c.R;
        return hitsFrame(mag, c) || hitsStrings(angle, mag, c);
    }

    private static boolean hitsStrings(double angle, double mag, Case c) {
        double x = mag * Math.cos(angle);
        double y = mag * Math.sin(angle);
        double pitch = 2 * c.r + c.g;
        int cellX = (int) (x / pitch);
        int cellY = (int) (y / pitch);
        double tx = x - cellX * pitch;
        double ty = y - cellY * pitch;
        double thickness = c.r + c.f;
        return (tx < thickness || tx > pitch - thickness || ty < thickness || ty > pitch - thickness);
    }

    private static boolean hitsFrame(double mag, Case c) {
        return mag + c.f + c.t >= c.R;
    }

    private static void getInput() throws Exception {
        // instantiate input file
        java.io.File file = new java.io.File(ClassLoader.getSystemResource(FILE_NAME).getFile());
        List<String> lines = Files.readAllLines(file.toPath());
        int n;
        double f, R, t, r, g;
        n = Integer.parseInt(lines.get(0));
        for (int i = 1; i <= n; i++) {
            String[] nums = lines.get(i).split(" ");
            f = Double.parseDouble(nums[0]);
            R = Double.parseDouble(nums[1]);
            t = Double.parseDouble(nums[2]);
            r = Double.parseDouble(nums[3]);
            g = Double.parseDouble(nums[4]);
            cases.add(new Case(f, R, t, r, g));
        }
    }


}

class Case {
    double f, R, t, r, g;

    Case(double f, double R, double t, double r, double g) {
        this.f = f;
        this.R = R;
        this.t = t;
        this.r = r;
        this.g = g;
    }
}