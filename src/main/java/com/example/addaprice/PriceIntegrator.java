package com.example.addaprice;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.addaprice.Util.minusMillis;
import static com.example.addaprice.Util.plusMillis;

// integrate prices with same product, number and department
public class PriceIntegrator {

    public static List<Price> addPrice(List<Price> allPrices, Price newPrice) {
        if (allPrices.isEmpty()) {
            allPrices.add(newPrice);
            return allPrices;
        }

        List<Price> intersected = allPrices.stream()
                .filter(p -> !noIntersect(p, newPrice))
                .collect(Collectors.toList());

        // if no intersection with old prices - simply add new price
        if (intersected.isEmpty()) {
            allPrices.add(newPrice);
            return allPrices;
        }

        boolean isExpand = false;
        for (Price oldPrice : intersected) {
            if (onSameDates(oldPrice, newPrice)) {
                oldPrice.setValue(newPrice.getValue());
                return allPrices;
            }

            if (isInsideOldPrice(oldPrice, newPrice)) {
                // split old price by new price
                Price oldPriceTail = new Price.Builder()
                        .productCode(oldPrice.getProductCode())
                        .number(oldPrice.getNumber())
                        .depart(oldPrice.getDepart())
                        .begin(plusMillis(newPrice.getEnd(), 1))
                        .end(oldPrice.getEnd())
                        .value(oldPrice.getValue())
                        .build();
                oldPrice.setEnd(minusMillis(newPrice.getBegin(), 1));
                allPrices.add(newPrice);
                allPrices.add(oldPriceTail);
                return allPrices;
            }

            // not same dates and not inside
            if (oldPrice.getValue() == newPrice.getValue()) {
                // expand old price
                if (oldPrice.getBegin().after(newPrice.getBegin())) {
                    oldPrice.setBegin(newPrice.getBegin());
                }
                if (oldPrice.getEnd().before(newPrice.getEnd())) {
                    oldPrice.setEnd(newPrice.getEnd());
                }
                isExpand = true;
            } else {
                // trim head
                if (oldPrice.getBegin().after(newPrice.getBegin())) {
                    oldPrice.setBegin(plusMillis(newPrice.getEnd(), 1));
                // trim tail
                } else if (oldPrice.getEnd().before(newPrice.getEnd())) {
                    oldPrice.setEnd(minusMillis(newPrice.getBegin(), 1));
                }
            }
        }

        // old prices was expanded - no need to add new price again
        if (!isExpand) {
            allPrices.add(newPrice);
        }

        // remove prices which was eliminated after integration of new prices
        return allPrices.stream()
                .filter(price -> price.getBegin().before(price.getEnd()))
                .collect(Collectors.toList());
    }

    private static boolean isInsideOldPrice(Price oldPrice, Price newPrice) {
        return oldPrice.getBegin().before(newPrice.getBegin()) &&
                oldPrice.getEnd().after(newPrice.getEnd());
    }

    private static boolean noIntersect(Price oldPrice, Price newPrice) {
        return oldPrice.getBegin().after(newPrice.getEnd()) ||
                oldPrice.getEnd().before(newPrice.getBegin());
    }

    private static boolean onSameDates(Price oldPrice, Price newPrice) {
        return oldPrice.getBegin().equals(newPrice.getBegin())
                && oldPrice.getEnd().equals(newPrice.getEnd());
    }
}
