package com.example.addaprice;

import org.junit.Test;

import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.example.addaprice.Util.endDay;
import static com.example.addaprice.Util.startDay;
import static java.time.Month.FEBRUARY;
import static java.time.Month.JANUARY;
import static org.junit.Assert.*;

public class PriceImporterTest {
    @Test
    public void importInvoke() {
        PriceImporter pi = new PriceImporter();
        Collection<Price> newPrices = pi.importPrices(oldPrices(), toImport());

        assertEquals(6, newPrices.size());
    }

    private Collection<Price> oldPrices() {
        List<Price> oldPrices = new ArrayList<>();
        Price.Builder pb = new Price.Builder();
        oldPrices.add(pb
                .productCode("122856").number(1).depart(1)
                .begin(startDay(2013, JANUARY, 1))
                .end(endDay(2013, JANUARY, 31))
                .value(11000)
                .build());

        oldPrices.add(pb
                .productCode("122856").number(2).depart(1)
                .begin(startDay(2013, JANUARY, 10))
                .end(endDay(2013, JANUARY, 20))
                .value(99000)
                .build());

        oldPrices.add(pb
                .productCode("6654").number(1).depart(2)
                .begin(startDay(2013, JANUARY, 1))
                .end(endDay(2013, JANUARY, 30))
                .value(5000)
                .build());

        return oldPrices;
    }

    private Collection<Price> toImport() {
        List<Price> toImport = new ArrayList<>();
        Price.Builder pb = new Price.Builder();

        toImport.add(pb
                .productCode("122856").number(1).depart(1)
                .begin(startDay(2013, JANUARY, 20))
                .end(endDay(2013, FEBRUARY, 20))
                .value(11000)
                .build());

        toImport.add(pb
                .productCode("122856").number(2).depart(1)
                .begin(startDay(2013, JANUARY, 15))
                .end(endDay(2013, JANUARY, 25))
                .value(92000)
                .build());

        toImport.add(pb
                .productCode("6654").number(1).depart(2)
                .begin(startDay(2013, JANUARY, 12))
                .end(endDay(2013, JANUARY, 12))
                .value(4000)
                .build());

        return toImport;
    }

}