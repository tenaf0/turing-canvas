package turing;

import java.util.Random;

public class Turing {
    private static final Random random = new Random();
    private final int alphabetNo;
    private final int stateNo;
    private final Tape tape;

    public Turing(int alphabetNo, int stateNo, Tape tape) {
        this.alphabetNo = alphabetNo;
        this.stateNo = stateNo;
        this.tape = tape;

        tapeLoc = new Tape.Coord(tape.width / 2, tape.height / 2);

        stateTransitions = new StateTransition[alphabetNo * stateNo];
        for (int l = 0; l < alphabetNo; l++) {
            for (int s = 0; s < stateNo; s++) {
                stateTransitions[l * stateNo + s] = new StateTransition(
                        random.nextInt(stateNo),
                        (byte) random.nextInt(alphabetNo),
                        Tape.Direction.randomDir(random)
                );
            }
        }

        for (var t : stateTransitions) {
            System.out.println(t);
        }
    }

    private final StateTransition[] stateTransitions;
    private int state = 0;
    private final Tape.Coord tapeLoc;

    public void step() {
        try {
            byte value = tape.readAt(tapeLoc.x, tapeLoc.y);
            var transition = stateTransitions[value * stateNo + state];

            tape.writeAt(tapeLoc.x, tapeLoc.y, transition.newLetter);
            state = transition.newState;

            tape.move(tapeLoc, transition.direction);
        } catch (Exception e) {
            throw new RuntimeException(String.format("tapeLoc: %s", tapeLoc), e);
        }

    }

    private record StateTransition(int newState, byte newLetter, Tape.Direction direction) {}
}
