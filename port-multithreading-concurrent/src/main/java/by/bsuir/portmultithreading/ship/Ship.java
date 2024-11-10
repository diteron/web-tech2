package by.bsuir.portmultithreading.ship;

import java.util.Random;

public class Ship implements Comparable<Ship> {
    private static int shipsCounter = 0;

    public enum Operation {
        LOADING("loading"),
        UNLOADING("unloading");

        private final String OPERATION;

        Operation(String string) {
            OPERATION = string;
        }

        @Override
        public String toString() {
            return this.OPERATION;
        }
    }

    private int id;

    private final static int MIN_PRIORITY = 0;
    private final static int MAX_PRIORITY = 5;

    /**
     * Ship priority ({@code 0-10})
     */
    private int priority;

    /**
     * Ship cargo priority ({@code 0-10}). The cargo priority is more important
     * compared to the ship priority
     */
    private int cargoPriority;

    private Operation operation;
    private boolean isWorking = false;

    private final static int MAX_NUMBER_OF_CONTAINERS = 20000;
    private final static int MIN_NUMBER_OF_CONTAINERS = 15000;
    private final static int TIME_FOR_ONE_CONTAINER_US = 150;   // in us
    private int numberOfCargo;

    private final static int BERTHING_TIME_EPSILON_US = 100;    // in us
    private long specifiedBerthingTime;
    private long actualBerthingTime;

    private final static Random RANDOM_GENERATOR = new Random();

    public Ship() {
        id = ++shipsCounter;
        setRandomPriority();
        setRandomCargoPriority();
    }

    private void setRandomPriority() {
        priority = RANDOM_GENERATOR.nextInt(MAX_PRIORITY + 1);
    }

    public void createRandomTask() {
        setRandomOperation();
        setRandomCargoPriority();
        setRandomNumberOfCargo();
        specifiedBerthingTime = numberOfCargo * TIME_FOR_ONE_CONTAINER_US;
        actualBerthingTime = createActualBerthingTime();
    }

    private void setRandomOperation() {
        boolean tmp = RANDOM_GENERATOR.nextBoolean();
        operation = tmp == true ? Operation.LOADING : Operation.UNLOADING;
    }

    private void setRandomCargoPriority() {
        cargoPriority = RANDOM_GENERATOR.nextInt(MAX_PRIORITY + 1);
    }

    private void setRandomNumberOfCargo() {
        numberOfCargo = MIN_NUMBER_OF_CONTAINERS +
                RANDOM_GENERATOR.nextInt((MAX_NUMBER_OF_CONTAINERS - MIN_NUMBER_OF_CONTAINERS) + 1);
    }

    private long createActualBerthingTime() {
        long berthingEpsilon = RANDOM_GENERATOR.nextLong(BERTHING_TIME_EPSILON_US) - BERTHING_TIME_EPSILON_US / 2;
        return numberOfCargo * (TIME_FOR_ONE_CONTAINER_US + berthingEpsilon);
    }

    public int getId() {
        return id;
    }

    public int getPriority() {
        return priority;
    }

    public int getCargoPriority() {
        return cargoPriority;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public Operation getOperation() {
        return operation;
    }

    public int getNumberOfCargo() {
        return numberOfCargo;
    }

    public int getNeededGoods() {
        return numberOfCargo;
    }

    /**
     * @return specified berthing time in ms
     */
    public long getSpecifiedBerthingTime() {
        return specifiedBerthingTime / 1000;
    }

    /**
     * @return actual berthing time in ms
     */
    public long getActualBerthingTime() {
        return actualBerthingTime / 1000;
    }

    public void startWorking() {
        isWorking = true;
    }

    public void finishWorking() {
        isWorking = false;
    }

    public void increasePriority() {
        if (priority < MAX_PRIORITY) {
            ++priority;
        }
    }

    public void decreasePriority() {
        if (priority > MIN_PRIORITY) {
            --priority;
        }
    }

    @Override
    public int compareTo(Ship other) {
        return Integer.compare(this.cargoPriority + this.priority, other.cargoPriority + other.priority);
    }
}
