/*
    OpenRide -- Car Sharing 2.0
    Copyright (C) 2010  Fraunhofer Institute for Open Communication Systems (FOKUS)

    Fraunhofer FOKUS
    Kaiserin-Augusta-Allee 31
    10589 Berlin
    Tel: +49 30 3463-7000
    info@fokus.fraunhofer.de

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License Version 3 as
    published by the Free Software Foundation.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package de.fhg.fokus.openride.matching;

/**
 * This class provides an interface for computing prices and
 * servers a first implementation called 'simplePriceCalculator'.
 * This class computes price proposals based on the detour and
 * the shared distance. The price per km changes with increasing shared distance,
 * and can be configure via the variables 'priceMatrix' and 'detourKmPrice'.
 * @author fvi
 */
abstract class PriceCalculator {

    private static final PriceCalculator STEPPED_PRICE_CALCULATOR = new PriceCalculator() {

    final double[][] priceMatrix = {
            //km offset (sorted asc)        cents
            {0d,                            20}, // 20 cents per km from km 0
            {10d,                            10},// 10 cents per km from km 10
            {50d,                           5}   // 5 cents per km from km 50
        };

    final double detourKmPrice = 35;            // 35 cents per km of detour

        @Override
        public int getPriceCents(double sharedDistanceMeters, double detourMeters) {
            detourMeters = Math.max(detourMeters, 0); // allow only detour >= 0, note it can really be negativ.
            double sharedDistanceKm = sharedDistanceMeters / 1000d;
            double cents = 0d;
            for(int j=priceMatrix.length - 1; j>=0; j--) {
                if(sharedDistanceKm > priceMatrix[j][0]) {
                    cents += (sharedDistanceKm - priceMatrix[j][0]) * priceMatrix[j][1];
                    sharedDistanceKm = priceMatrix[j][0];
                }
            }
            cents += detourKmPrice * (detourMeters / 1000);
            return (int)Math.ceil(Math.max(cents, 0));
        }
    };
    
    private static final PriceCalculator LINEAR_PRICE_CALCULATOR = new PriceCalculator() {

        final int CENT_PER_KM = 25;
        final int MIN_CENT = 50;
        final int MAX_CENT = 300;

        @Override
        public int getPriceCents(double sharedDistanceMeters, double detourMeters) {
            detourMeters = Math.max(detourMeters, 0) / 1000; // allow only detour >= 0, note it can really be negativ.
            double sharedDistanceKm = sharedDistanceMeters / 1000d;

            long completeDistanceKm = Math.round(detourMeters + sharedDistanceKm);

            int cents = 0;

            for (int i = 0; (cents < MAX_CENT) && (i < completeDistanceKm); i++) {
                    cents = cents + CENT_PER_KM;
            }

            if ((cents == 0) || (cents < MIN_CENT)) {
                cents = MIN_CENT;
            }
            
            return cents;
        }
    };

    public static final PriceCalculator getInstance() {
        return LINEAR_PRICE_CALCULATOR;
    }

    /**
     * Compute the price based on shared distance and detour.
     * Maybe this be extended to include the number of persons to pick up.
     * @param sharedDistanceMeters
     * @param detourMeters
     * @return price in euro cents.
     */
    public abstract int getPriceCents(double sharedDistanceMeters, double detourMeters);
}
