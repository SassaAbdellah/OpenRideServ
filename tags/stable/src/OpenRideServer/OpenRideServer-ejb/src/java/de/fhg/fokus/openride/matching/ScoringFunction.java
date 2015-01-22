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

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * This class defines an interface for a scoring function
 * and provides one implementation called simpleScoreFunction.
 * The simpleScoreFunction computes a weighted score based on
 * detour and distance.
 * @author fvi
 */
abstract class ScoringFunction {

    private final static ScoringFunction simpleScoreFunction = new ScoringFunction() {
        
        /* all weights should sum up to 1 */
        private final double WEIGHT_DETOUR = 0.6;
        private final double WEIGHT_DISTANCE = 0.4;

        @Override
        public double getScore(MatchEntity m) {
            double normalizedScoreDetour = 1.0d - (m.getMatchDetourMeters() / m.getMatchSharedDistancEmeters());
            double normalizedScoreDistance = m.getMatchSharedDistancEmeters() / m.getMatchDriveRemainingDistanceMeters();
            return WEIGHT_DETOUR * normalizedScoreDetour
                    + WEIGHT_DISTANCE * normalizedScoreDistance;
        }
    };

    /**
     * The currently used scoring function.
     * Change this method to switch between different scoring functions.
     * @return
     */
    public static final ScoringFunction getInstance() {
        return simpleScoreFunction;
    }

    /**
     * Compare matches using the coring function.
     */
    private final Comparator<MatchEntity> comparator = new Comparator<MatchEntity>() {
        public int compare(MatchEntity o1, MatchEntity o2) {
            double score1 = getScore(o1);
            double score2 = getScore(o2);
            if(score1 > score2) {
                return 1;
            } else if(score1 < score2) {
                return -1;
            } else {
                return 0;
            }
        }
    };

    /**
     * Sort the Matches by score, best first.
     * @param matches to be sorted.
     */
    public final void sortDescending(MatchEntity[] matches) {
        Arrays.sort(
                matches,
                comparator
        );
    }

    /**
     * sorts the matches by score, best first.
     * @param matches to be sorted.
     */
    public final void sortDescending(List<MatchEntity> matches) {
        MatchEntity[] array = new MatchEntity[matches.size()];
        matches.toArray(array);
        sortDescending(array);
        matches.clear();
        for(MatchEntity m : array) {
            matches.add(m);
        }
    }

    /**
     * Compute a score for the match.
     * The better the match, the higher the returned score.
     * @param m
     * @return score value.
     */
    public abstract double getScore(MatchEntity m);
}
