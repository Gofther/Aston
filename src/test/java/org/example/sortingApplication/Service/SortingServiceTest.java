package org.example.sortingApplication.Service;

import org.example.sortingApplication.domain.Bus;
import org.example.sortingApplication.service.SortingService;
import org.example.sortingApplication.strategy.SortStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;


class SortingServiceTest {

    private SortingService sortingService;
    private Bus bus1, bus2, bus3;

    @BeforeEach
    void setUp() {
        sortingService = new SortingService();
        bus1 = new Bus("B", "DriverB", 2);
        bus2 = new Bus("A", "DriverA", 1);
        bus3 = new Bus("C", "DriverC", 3);
    }

    @Test
    void testPerformSort_WithoutStrategy() {
        Bus[] buses = {bus1, bus2};

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> sortingService.performSort(buses)
        );

        assertEquals("Стратегия не установлена", exception.getMessage());
    }

    @Test
    void testPerformSort_WithStrategy() {
        SortStrategy mockStrategy = new SortStrategy() {
            @Override
            public Bus[] sort(Bus[] buses) {
                return buses;
            }

            @Override
            public String getDescription() {
                return "Mock strategy that returns array as-is";
            }
        };

        sortingService.setStrategy(mockStrategy);
        Bus[] buses = {bus1, bus2, bus3};

        Bus[] result = sortingService.performSort(buses);

        assertSame(buses, result);
        assertEquals("Mock strategy that returns array as-is", mockStrategy.getDescription());
    }

    @Test
    void testPerformSort_EmptyArray() {
        SortStrategy mockStrategy = new SortStrategy() {
            @Override
            public Bus[] sort(Bus[] buses) {
                return buses;
            }

            @Override
            public String getDescription() {
                return "Empty array handler";
            }
        };

        sortingService.setStrategy(mockStrategy);

        Bus[] emptyBuses = {};
        Bus[] result = sortingService.performSort(emptyBuses);

        assertEquals(0, result.length);
    }

    @Test
    void testPerformSort_NullArray() {
        SortStrategy mockStrategy = new SortStrategy() {
            @Override
            public Bus[] sort(Bus[] buses) {
                return buses;
            }

            @Override
            public String getDescription() {
                return "Null handler";
            }
        };

        sortingService.setStrategy(mockStrategy);

        Bus[] result = sortingService.performSort(null);
        assertNull(result);
    }

    @Test
    void testSetStrategy() {
        SortStrategy strategy1 = new SortStrategy() {
            @Override
            public Bus[] sort(Bus[] buses) {
                return buses;
            }

            @Override
            public String getDescription() {
                return "First strategy";
            }
        };

        SortStrategy strategy2 = new SortStrategy() {
            @Override
            public Bus[] sort(Bus[] buses) {
                return new Bus[0];
            }

            @Override
            public String getDescription() {
                return "Second strategy";
            }
        };

        sortingService.setStrategy(strategy1);
        sortingService.setStrategy(strategy2);

        // Проверяем что вторая стратегия работает
        Bus[] result = sortingService.performSort(new Bus[]{bus1});
        assertEquals(0, result.length);
    }

    @Test
    void testStrategyReplacement() {
        final Bus markerBus = new Bus("MARKER", "Marker", 999);

        SortStrategy firstStrategy = new SortStrategy() {
            @Override
            public Bus[] sort(Bus[] buses) {
                return new Bus[]{markerBus};
            }

            @Override
            public String getDescription() {
                return "Marker strategy";
            }
        };

        SortStrategy secondStrategy = new SortStrategy() {
            @Override
            public Bus[] sort(Bus[] buses) {
                return buses;
            }

            @Override
            public String getDescription() {
                return "Identity strategy";
            }
        };

        sortingService.setStrategy(firstStrategy);
        Bus[] result1 = sortingService.performSort(new Bus[]{bus1});

        sortingService.setStrategy(secondStrategy);
        Bus[] result2 = sortingService.performSort(new Bus[]{bus1});

        assertEquals(markerBus, result1[0]);
        assertEquals(bus1, result2[0]);
    }

    @Test
    void testDifferentStrategiesReturnDifferentResults() {
        // Стратегия, которая переворачивает массив
        SortStrategy reverseStrategy = new SortStrategy() {
            @Override
            public Bus[] sort(Bus[] buses) {
                if (buses == null || buses.length <= 1) {
                    return buses;
                }
                Bus[] reversed = new Bus[buses.length];
                for (int i = 0; i < buses.length; i++) {
                    reversed[i] = buses[buses.length - 1 - i];
                }
                return reversed;
            }

            @Override
            public String getDescription() {
                return "Reverse strategy";
            }
        };

        sortingService.setStrategy(reverseStrategy);
        Bus[] buses = {bus1, bus2, bus3};
        Bus[] result = sortingService.performSort(buses);

        assertEquals(bus3, result[0]);
        assertEquals(bus2, result[1]);
        assertEquals(bus1, result[2]);
        assertEquals("Reverse strategy", reverseStrategy.getDescription());
    }
}
