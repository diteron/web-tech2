package by.bsuir.portmultithreading.ship;

import java.util.Random;

public class Ship implements Comparable<Ship> {
    private static Integer shipsCounter = 0;

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

    private Integer id;

    private final static Integer MIN_PRIORITY = 0;
    private final static Integer MAX_PRIORITY = 9;

    /**
     * Ship priority ({@code 0-10})
     */
    private Integer priority;

    /**
     * Ship cargo priority ({@code 0-10}). The cargo priority is more important
     * compared to the ship priority
     */
    private Integer cargoPriority;

    private Operation operation;
    private boolean isWorking = false;

    private final static Integer MAX_NUMBER_OF_CONTAINERS = 20000;
    private final static Integer MIN_NUMBER_OF_CONTAINERS = 10000;
    private final static Integer TIME_FOR_ONE_CONTAINER_US = 150;   // in us
    private Integer numberOfCargo;

    private final static Integer BERTHING_TIME_EPSILON_US = 100;    // in us
    private Long specifiedBerthingTime;
    private Long actualBerthingTime;

    private final static Random RANDOM_GENERATOR = new Random();

    private static final Object LOCK = new Object();

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
        specifiedBerthingTime = Long.valueOf(numberOfCargo * TIME_FOR_ONE_CONTAINER_US);
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

    private Long createActualBerthingTime() {
        Long berthingEpsilon = RANDOM_GENERATOR.nextLong(BERTHING_TIME_EPSILON_US) - BERTHING_TIME_EPSILON_US / 2;
        return numberOfCargo * (TIME_FOR_ONE_CONTAINER_US + berthingEpsilon);
    }

    public Integer getId() {
        return id;
    }

    public Integer getPriority() {
        return priority;
    }

    public Integer getCargoPriority() {
        return cargoPriority;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public Operation getOperation() {
        return operation;
    }

    public Integer getNumberOfCargo() {
        return numberOfCargo;
    }

    public Integer getNeededGoods() {
        return numberOfCargo;
    }

    /**
     * @return specified berthing time in ms
     */
    public Long getSpecifiedBerthingTime() {
        return specifiedBerthingTime / 1000;
    }

    /**
     * @return actual berthing time in ms
     */
    public Long getActualBerthingTime() {
        return actualBerthingTime / 1000;
    }

    public void startWorking() {
        synchronized (LOCK) {
            isWorking = false;
        }
    }

    public void finishWorking() {
        synchronized (LOCK) {
            isWorking = false;
        }
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
        if (this.cargoPriority.equals(other.cargoPriority)) {
            return Integer.compare(this.priority, other.priority);
        }
        else {
            return Integer.compare(this.cargoPriority, other.cargoPriority); 
        }
    }
}
