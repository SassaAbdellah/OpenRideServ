/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhg.fokus.openride.routing.graphhopper.routing;

import de.fhg.fokus.openride.routing.graphhopper.configuration.GraphHopperConfiguration;

/**
 * Utility c
 *
 *
 *
 * @author jochen
 */
public class GraphHopperRoutingGraphCreator {

    public static void main(String args[]) {


        GraphHopperConfiguration gc = new GraphHopperConfiguration();



        System.out.println("Trying to create graph from PBF : " + gc.getGHRoutePBF());
        System.out.println("Graphhopper stuff will go too   : " + gc.getGHRouteBasedirFQN());
        new GraphhopperRouter();

        System.out.println("Graph created.");
        System.out.println("OpenRideShare is ready. Enjoy!");
    }
}
