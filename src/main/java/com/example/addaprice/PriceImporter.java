package com.example.addaprice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class PriceImporter {
    public Collection<Price> importPrices(Collection<Price> oldPrices, Collection<Price> importPrices) {
        // group new prices by product, number, department
        Map<Tuple, List<Price>> importGroups = importPrices.stream()
                .collect(groupingBy(p -> new Tuple(p.getProductCode(), p.getNumber(), p.getDepart())));

        List<Price> combinedResult = new ArrayList<>();
        // import prices for each group
        for (Map.Entry<Tuple, List<Price>> entry : importGroups.entrySet()) {
            List<Price> filteredPrices = oldPrices.stream()
                    .filter(toEqual(entry.getKey()))
                    .collect(toList());

            for (Price price : entry.getValue())
                PriceIntegrator.addPrice(filteredPrices, price);

            combinedResult.addAll(filteredPrices);
        }
        return combinedResult;
    }

    private Predicate<Price> toEqual(Tuple key) {
        return p -> p.getProductCode().equals(key.productCode)
                && p.getNumber() == key.number
                && p.getDepart() == key.depart;
    }

    private static class Tuple {
        private String productCode;
        private int number;
        private int depart;

        Tuple(String productCode, int number, int depart) {
            this.productCode = productCode;
            this.number = number;
            this.depart = depart;
        }
    }
}

