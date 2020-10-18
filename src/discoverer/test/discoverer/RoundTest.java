package discoverer;

import java.util.*;
import static org.junit.Assert.*;
import org.junit.*;

public class RoundTest {
    private Map<Example, Ball> roundStore = new HashMap<Example, Ball>();
    private static final boolean debugEnabled  = true;

    @Before
    public void initRandom() {
        Global.rg = new Random(1L);
    }

    @Test
    public void test() {
        String[] rules = {
            "l21(X) :- atom(X,cl), atom(X,cl).",
            "l22(X) :- atom(X,br).",
            "l23(X) :- atom(X,na).",
            "l24(X) :- atom(X,f).",

            "0.1 k21(X) :- l21(X).",
            "0.1 k22(X) :- l21(X).",
            "0.1 k22(X) :- l22(X).",
            "0.1 k22(X) :- l23(X).",
            "0.1 k23(X) :- l23(X).",
            "0.1 k23(X) :- l24(X).",

            "l11(X) :- k21(X), k22(Y).",
            "l12(X) :- k21(X), k22(Y).",
            "l13(X) :- k22(X), k23(Y).",

            "0.1 k11(X) :- l11(X).",
            "0.1 k11(X) :- l12(X).",
            "0.9 k12(X) :- l11(X).",
            "0.10 k12(X) :- l12(X).",
            "0.11 k12(X) :- l13(X).",
            "0.12 k13(X) :- l11(X).",
            "0.13 k13(X) :- l12(X).",
            "0.14 k13(X) :- l13(X).",
        };

        String[] ex = {
            "1.0 b(a,b), b(b,c), b(c,a), b(c,d), b(c,e), atom(a,c), atom(b,c), atom(c,c), atom(d,cl), atom(d,br).",
            "1.0 blb(a,b).",
            "1.0 b(a,b), b(b,c), b(c,a), b(c,d), b(c,e), atom(a,c), atom(b,c), atom(c,c), atom(d,cl), atom(d,br).",
            "1.0 b(a,b), b(b,c), b(c,a), b(c,d), b(c,e), atom(a,c), atom(b,c), atom(c,c), atom(d,cl), atom(d,br).",
        };

        NetFactory nf = new NetFactory();
        KL last = nf.construct(rules);

        ExampleFactory eFactory = new ExampleFactory();
        for (int i = 0; i < ex.length; i++) {
            Example e = eFactory.construct(ex[i]);
            Ball b = Solvator.solve(last,e);
            roundStore.put(e, b);
            System.out.println("Original output #" + i + "\t" + b.val);
        }

        while (true) {
            for (Map.Entry<Example, Ball> entry: roundStore.entrySet()) {
                Example e = entry.getKey();
                Ball b = Solvator.solve(last,e);
                roundStore.put(e, b);
                System.out.println("New subs #" + "\t" + b.val);
            }

            for (int i = 0; i < 5; i++) {
                for (Map.Entry<Example, Ball> entry: roundStore.entrySet()) {
                    Example e = entry.getKey();
                    Ball b = entry.getValue();

                    Weights w = Backpropagator.computeNewWeights((Kappa) last,b,e);

                    for (Tuple<KappaRule, Double> t: w.getWeights()) {
                        if (debugEnabled) System.out.println("Changing rule weights\t" + t.x + "\t" + t.x.getWeight() + "\t->\t" + t.y + "\t(" + (t.x.getWeight() - t.y) + ")");
                        t.x.setWeight(t.y);
                    }

                    for (Tuple<Kappa, Double> t: w.getBias()) {
                        if (debugEnabled) System.out.println("Changing bias weights\t\t" + t.x + "\t" + t.x.bias + "\t->\t" + t.y + "\t(" + (t.x.bias - t.y) + ")");
                        t.x.setBias(t.y);
                    }

                    double out = Evaluator.eval(last,b);
                    b.val = out;
                    System.out.println("Learned output:\t" + out);
                }
            }
            break;
        }
    }
}
