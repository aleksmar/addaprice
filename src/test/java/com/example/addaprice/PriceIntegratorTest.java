package com.example.addaprice;

import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.example.addaprice.PriceIntegrator.addPrice;
import static com.example.addaprice.Util.endDay;
import static com.example.addaprice.Util.startDay;
import static java.time.Month.*;
import static org.junit.Assert.assertEquals;

public class PriceIntegratorTest {

    @Test
    public void whenEmptyHistory_thenReturnOnePrice() {
        Price newPrice = new Price.Builder().build();

        List<Price> newPrices = addPrice(new ArrayList<>(), newPrice);

        assertEquals(1, newPrices.size());
        assertEquals(newPrice, newPrices.get(0));
    }

    @Test
    public void whenPriceWithSameDates_thenChangePrice() {
        List<Price> priceHistory = onePriceHistory();
        Price newPrice = new Price.Builder()
                .begin(startDay(2013, JANUARY, 1))
                .end(endDay(2013, JANUARY, 31))
                .value(2000)
                .build();

        List<Price> prices = addPrice(priceHistory, newPrice);

        assertEquals(1, prices.size());
        assertEquals(2000, prices.get(0).getValue());
    }

    @Test
    public void whenNoPriceIntersection_thenAddNewPrice() {
        List<Price> priceHistory = onePriceHistory();
        Price newPrice = new Price.Builder()
                .begin(startDay(2012, DECEMBER, 1))
                .end(endDay(2012, DECEMBER, 31))
                .value(2000)
                .build();

        List<Price> newPrices = addPrice(priceHistory, newPrice);

        assertEquals(2, newPrices.size());
    }

    @Test
    public void whenSameValuePricesIntersect_thanExpandPricePeriod() {
        List<Price> priceHistory = onePriceHistory();
        Price newPrice = new Price.Builder()
                .begin(startDay(2013, JANUARY, 10))
                .end(endDay(2013, FEBRUARY, 20))
                .value(1000)
                .build();

        List<Price> newPrices = addPrice(priceHistory, newPrice);

        assertEquals(1, newPrices.size());
        assertEquals(1000, newPrices.get(0).getValue());
        assertEquals(startDay(2013, JANUARY, 1), newPrices.get(0).getBegin());
        assertEquals(endDay(2013, FEBRUARY, 20), newPrices.get(0).getEnd());
    }

    @Test
    public void whenDiffValuePriceIntersectBefore_thenSplitPrices() {
        List<Price> priceHistory = onePriceHistory();
        Price newPrice = new Price.Builder()
                .begin(startDay(2012, DECEMBER, 1))
                .end(endDay(2013, JANUARY, 20))
                .value(2000)
                .build();

        List<Price> newPrices = addPrice(priceHistory, newPrice);
        assertEquals(2, newPrices.size());
        assertEquals(2000, newPrices.get(1).getValue());
        assertEquals(startDay(2013, JANUARY, 21), newPrices.get(0).getBegin());
        assertEquals(endDay(2013, JANUARY, 20), newPrices.get(1).getEnd());
    }

    @Test
    public void whenDiffValuePricesIntersectAfter_thenSplitPrices() {
        List<Price> priceHistory = onePriceHistory();
        Price newPrice = new Price.Builder()
                .begin(startDay(2013, JANUARY, 10))
                .end(endDay(2013, FEBRUARY, 20))
                .value(2000)
                .build();


        List<Price> newPrices = addPrice(priceHistory, newPrice);
        assertEquals(2, newPrices.size());
        assertEquals(1000, newPrices.get(0).getValue());
        assertEquals(2000, newPrices.get(1).getValue());
        assertEquals(startDay(2013, JANUARY, 1), newPrices.get(0).getBegin());
        assertEquals(endDay(2013, JANUARY, 9), newPrices.get(0).getEnd());
        assertEquals(startDay(2013, JANUARY, 10), newPrices.get(1).getBegin());
        assertEquals(endDay(2013, FEBRUARY, 20), newPrices.get(1).getEnd());
    }

    @Test
    public void whenNewPriceInsideOld_thenInsertNewPriceInto() {
        List<Price> priceHistory = onePriceHistory();
        Price newPrice = new Price.Builder()
                .begin(startDay(2013, JANUARY, 10))
                .end(endDay(2013, JANUARY, 20))
                .build();

        List<Price> newPrices = addPrice(priceHistory, newPrice);

        assertEquals(3, newPrices.size());
        assertEquals(endDay(2013, JANUARY, 9), newPrices.get(0).getEnd());
        assertEquals(startDay(2013, JANUARY, 10), newPrices.get(1).getBegin());
        assertEquals(endDay(2013, JANUARY, 20), newPrices.get(1).getEnd());
        assertEquals(startDay(2013, JANUARY, 21), newPrices.get(2).getBegin());
    }

    @Test
    public void example2Test() {
        // old prices
        List<Price> priceHistory = new ArrayList<>();
        priceHistory.add(new Price.Builder()
                .begin(startDay(2013, JANUARY, 1))
                .end(endDay(2013, JANUARY, 31))
                .value(100)
                .build());
        priceHistory.add(new Price.Builder()
                .begin(startDay(2013, FEBRUARY, 1))
                .end(endDay(2013, FEBRUARY, 20))
                .value(120)
                .build());

        // price to import
        Price newPrice = new Price.Builder()
                .begin(startDay(2013, JANUARY, 20))
                .end(endDay(2013, FEBRUARY, 10))
                .value(110)
                .build();

        List<Price> newPrices = addPrice(priceHistory, newPrice);
        assertEquals(3, newPrices.size());
        assertEquals(endDay(2013, JANUARY, 19), newPrices.get(0).getEnd());
        assertEquals(100, newPrices.get(0).getValue());
        assertEquals(startDay(2013, FEBRUARY, 11), newPrices.get(1).getBegin());
        assertEquals(startDay(2013, JANUARY, 20), newPrices.get(2).getBegin());
    }

    @Test
    public void example3Test() {
        // old prices
        List<Price> priceHistory = new ArrayList<>();
        priceHistory.add(new Price.Builder()
                .begin(startDay(2013, JANUARY, 1))
                .end(endDay(2013, JANUARY, 15))
                .value(80)
                .build());
        priceHistory.add(new Price.Builder()
                .begin(startDay(2013, JANUARY, 16))
                .end(endDay(2013, JANUARY, 31))
                .value(87)
                .build());
        priceHistory.add(new Price.Builder()
                .begin(startDay(2013, FEBRUARY, 1))
                .end(endDay(2013, FEBRUARY, 20))
                .value(90)
                .build());

        // prices to import
        Price firstPrice = new Price.Builder()
                .begin(startDay(2013, JANUARY, 10))
                .end(endDay(2013, JANUARY, 20))
                .value(80)
                .build();
        Price secondPrice = new Price.Builder()
                .begin(startDay(2013, JANUARY, 21))
                .end(endDay(2013, FEBRUARY, 15))
                .value(85)
                .build();

        List<Price> tempPrices = addPrice(priceHistory, firstPrice);
        List<Price> result = addPrice(tempPrices, secondPrice);

        assertEquals(3, result.size());
        assertEquals(endDay(2013, JANUARY, 20), result.get(0).getEnd());
        assertEquals(80, result.get(0).getValue());
        assertEquals(startDay(2013, FEBRUARY, 16), result.get(1).getBegin());
        assertEquals(90, result.get(1).getValue());
        assertEquals(startDay(2013, JANUARY, 21), result.get(2).getBegin());
        assertEquals(85, result.get(2).getValue());
    }

    private List<Price> onePriceHistory() {
        List<Price> priceHistory = new ArrayList<>();
        Price price = new Price.Builder()
                .begin(startDay(2013, JANUARY, 1))
                .end(endDay(2013, JANUARY, 31))
                .value(1000)
                .build();
        priceHistory.add(price);
        return priceHistory;
    }

}