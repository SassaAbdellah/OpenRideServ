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
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;
import org.postgresql.geometric.PGpoint;

import java.util.logging.Logger;
import org.postgis.Point;

/**
 * Eclipselink converter to/from postgis Point
 *
 * @author pab
 *
 */
public class PointConverter implements Converter {
  private static final long serialVersionUID = -5938037316595234421L;
  static Logger log = Logger.getLogger(PointConverter.class.getName());

  public Point convertDataValueToObjectValue(Object dataValue, Session
session) {
    if (dataValue == null) {
      return null;
    }
    else if (dataValue instanceof PGpoint) {
      return new Point(((PGpoint)dataValue).x, ((PGpoint)dataValue).y);
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
      return new PGpoint(((Point)objectValue).getX(),
((Point)objectValue).getY());
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
}

