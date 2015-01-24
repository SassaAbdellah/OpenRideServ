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

package de.fhg.fokus.openride.helperclasses.converter;


import com.jhlabs.map.proj.Projection;
import com.jhlabs.map.proj.ProjectionFactory;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;
import org.postgresql.geometric.PGpoint;

import java.util.logging.Logger;
import org.postgis.Point;
import java.awt.geom.Point2D;

/**
 * Converter for Eclipselink JPA. Gives lon/lat points to
 * java and projected x/y points to database. Uses library:
 * 'javaproj-1.0.6'
 * 
 * projection params can be changed in String[] PROJ_SPEC_GERMANY.
 *
 * @author fvi
 */
public class ProjPointConverter implements Converter {
    private static final long serialVersionUID = -5938037316595234421L;
    private static final String[] PROJ_SPEC_GERMANY = new String[]  {
        "+proj=cass",
        "+lat_0=52.41864827777778",
        "+lon_0=13.62720366666667",
        "+x_0=40000",
        "+y_0=10000",
        "+ellps=bessel",
        "+datum=potsdam",
        "+units=m",
        "+no_defs"
    };
    static final Logger log = Logger.getLogger(PointConverter.class.getName());

    private final Projection proj;

    public ProjPointConverter(){
        this.proj = ProjectionFactory.fromPROJ4Specification(PROJ_SPEC_GERMANY);
    }

    public Point convertDataValueToObjectValue(Object dataValue, Session
        session) {
        if (dataValue == null) {
            return null;
        }
        else if (dataValue instanceof PGpoint) {
            System.out.println("PGPopint: " + dataValue);
            Point2D.Double point = new Point2D.Double(
                ((PGpoint)dataValue).x,
                 ((PGpoint)dataValue).y
            );
            proj.inverseTransform(point, point);

            return new Point(point.x, point.y);
        }
        else {
              log.severe("dataValue not instance of PGpoint");
              return null;
        }
    }

    public PGpoint convertObjectValueToDataValue(Object objectValue, Session session) {
        if (objectValue == null) {
              // can't return null here it will attempt to store as varchar - this results in a POINT(0,0), which is incorrect
                // init changed -> should be working!
              return null;
        }
        else if (objectValue instanceof Point) {
            Point2D.Double point = new Point2D.Double(
                ((Point) objectValue).x,
                ((Point) objectValue).y
            );
            proj.transform(point, point);
            return new PGpoint(point.x, point.y);
        }
        else {
              log.severe("objectValue not instance of Point");
              return new PGpoint();
        }
    }

    public void initialize(DatabaseMapping dm, Session session) {
        dm.getField().setSqlType(java.sql.Types.OTHER);
    }


    public boolean isMutable() {
        return false;
    }

    public static void main(String[] args){
        PGpoint p = new PGpoint(12, 13);
        System.out.print(p);
    }
}


