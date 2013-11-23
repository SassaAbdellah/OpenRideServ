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

package de.fhg.fokus.openride.customerprofile.helperclasses;



import de.fhg.fokus.openride.customerprofile.CustomerEntity;
import de.fhg.fokus.openride.matching.Constants;
import de.fhg.fokus.openride.routing.Coordinate;
import de.fhg.fokus.openride.routing.RoutePoint;
import de.fhg.fokus.openride.routing.Router;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import org.postgis.Point;

/**
 * This class contains some informations that can be used to generate random userprofiles.
 * @author pab
 */
public class RandomInfo {
    
    private final static int GENERATE_COORDINATE_MAX_TRIES = 10000;

    private final static String SQL_SELECT_COUNT_VERTICES =
            "SELECT COUNT(*) AS count FROM vertices_tmp;";
    private final static String SQL_SELECT_VERTEX_COORDINATES =
            "SELECT x(v.the_geom) as lon, y(v.the_geom) as lat FROM vertices_tmp v;";
    private final Point[] coordinates;

    private final String[] custFirstName = {"Horst", "Peter", "Ursula", "Anna", "Robert", "Annika", "Berbel"};
    private final String[] custLastname = {"Sch\00E4fer", "Abromeit", "Scholz", "Meier", "Rieux", "Brand", "Kamm"};
    private final String[] custNickname = {"Pipo1", "HyperMax", "SeifenUrsel", "AnnaMontana", "Funnika", "Hiro"};
    private final String[] custPasswd = {"123456789"};
    private final Character[] custGender = {'m','f'};
    private final String[] custMobilPhoneNumber = {"01789795028","01765584656","0165214563","0163254565","015054865","015489554","0123456789"};
    private final String[] custEmail = {"P.Abromeit@gmx.de","Sch\00E4fer@gmx.de","Scholz@gmx.de","Meier@gmx.de","Rieux@gmx.de","SeifenUrsel@gmail.de","Funnika@gmx.de","Spass@gmx.de","BillClinton@web.de"};
    private final String[] custStreet = {"Holzhauserallee", "Schnepfenreuther weg", "Kaiserin-Augusta-Allee","Kurf\00FCstenstrasse", "Warschauerstrasse"};
    private final Integer[] custZipCode = {12345,4321,56654,98733, 901232};
    private final Character[] custRiderPrefGender = {
            CustomerEntity.PREF_GENDER_DONT_CARE,
            CustomerEntity.PREF_GENDER_DONT_CARE, 
            CustomerEntity.PREF_GENDER_GIRLS_ONLY
    };
    private final Character[] custRiderPrefSmoker = {
        CustomerEntity.PREF_SMOKER_DESIRED,
        CustomerEntity.PREF_SMOKER_DONT_CARE,
        CustomerEntity.PREF_SMOKER_DONT_CARE,
        CustomerEntity.PREF_SMOKER_DONT_CARE,
        CustomerEntity.PREF_SMOKER_NOT_DESIRED
    };
    private final Character[] custDriverPrefGender = {
            CustomerEntity.PREF_GENDER_DONT_CARE,
            CustomerEntity.PREF_GENDER_DONT_CARE,
            CustomerEntity.PREF_GENDER_GIRLS_ONLY
    };
    private final Character[] custDriverPrefSmoker = {
        CustomerEntity.PREF_SMOKER_DESIRED,
        CustomerEntity.PREF_SMOKER_DONT_CARE,
        CustomerEntity.PREF_SMOKER_DONT_CARE,
        CustomerEntity.PREF_SMOKER_DONT_CARE,
        CustomerEntity.PREF_SMOKER_NOT_DESIRED
    };
    private final Integer[] custRiderPrefAge = {
        10, 20, 30, 40, 50
    };
    private final Integer[] custDriverPrefAge = {
        11, 22, 33, 44, 55
    };
    private final Integer[] custBankAccount = {1646475837, 1463728173, 1102983746};
    private final Integer[] custBankCode = {10050090, 10050023};
    private final String[] custCity = {"Berlin", "M\00FCnchen", "Hong Kong","New York", "Kuala Lumpur", "Beijing"};

    private final Double[] amounts = {12d,12345d,543d,234d,20d,40d};
    private final String[] carbrands = {"Ford", "Nissan", "Ferrari", "Fiat", "Mercedes Benz", "Chevrolet", "VW"};
    private final String[] driveName = {"PartyRide", "Schneller gehts nich!", "Ruhige Fahrt.", "Steig ein.", "Riding with the king"};
    private final Integer[] frequencies = {1,2,3,4,5,20,30,13};
    private final String[] favpoints = {"K\00F6lner Dom", "Q-Damm", "Frauenkirche", "Brandenburger Tor", "L\'arc de Triumph"};
    private final Integer[] rideNoPassengers = {1,1,1,1,1,1,1,1,2,2,3};
    private final String[] carColors = {"red", "blue", "silver", "gold", "green", "black", "yellow", "white"};
    private final String[] driveComment = {"bitte p\00FCnklich!", "bringt musik mit!", "heizen", "-", "t\00E4gliche fahrt", "kommerzielle fahrt"};
    private final Integer[] driveOfferedSeats = {3,3,3,3,3,3,3,3,4,4,4,2,2,2,1,1};

    private final Random rnd;
    private final Connection conn;
    private final Router router;

    public RandomInfo(Connection conn, Router router) throws SQLException {
        this.rnd = new Random();
        this.conn = conn;
        this.coordinates = getCoordinates();
        this.router = router;
    }

    public static Connection getNewConnection()throws ClassNotFoundException, SQLException {
       Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://"
                + Constants.PGROUTING_DB_HOST
                + ":" + Constants.PGROUTING_DB_PORT
                + "/" + Constants.PGROUTING_DB_NAME;
        Connection conn = DriverManager.getConnection(
                url,
                Constants.PGROUTING_DB_USER,
                Constants.PGROUTING_DB_PASS
        );
        return conn;
    }

    public Connection getConnection() {
        return conn;
    }
    
    public RandomCustomer getRandomCustomer() {
        return new RandomCustomer(
            randomArrayItem(custNickname) + "_" + randomInt(1000, 10000) + randomString(1),                  //nickname
            randomArrayItem(custPasswd),
            randomArrayItem(custFirstName),
            randomArrayItem(custLastname),
            randomArrayItem(custMobilPhoneNumber),
            randomArrayItem(custStreet),
            randomArrayItem(custCity),
            new Date(randomInt(1940, 1996), randomInt(1, 12), randomInt(0, 28)),    //date of birth
            randomArrayItem(custGender),                                            //gender
            randomBoolean(0.33),                                                    //is smoker
            randomBoolean(0.2),                                                     //has post identified
            "dummyZipcode",
            randomArrayItem(custEmail),
            randomArrayItem(custRiderPrefGender),
            randomArrayItem(custRiderPrefSmoker),
            randomArrayItem(custDriverPrefGender),
            randomArrayItem(custDriverPrefSmoker),
            randomArrayItem(custRiderPrefAge),
            randomArrayItem(custDriverPrefAge),
            randomArrayItem(custBankAccount),
            randomArrayItem(custBankCode)
        );
    }

    public RandomCar getRandomCar() {
        return new RandomCar(
                randomArrayItem(carbrands),                                                          //brand
                (short) randomInt(1950, 2009),                                                          //build year
                randomArrayItem(carColors),                                                          //color
                randomInt(100000, 1000000) + "-" + randomInt(1000, 10000) + "-" + randomInt(10, 100) //plateNo
        );
    }

    public RandomDrive getRandomDrive(double minDistanceMeters, double maxDistanceMeters, Date minStartTime, Date maxStartTime, int minDetourPercent, int maxDetourPercent) {
        Point startPt = randomArrayItem(coordinates);
        Point endPt = randomPointWithinDistance(startPt, minDistanceMeters, maxDistanceMeters);
        Date startTime = randomDate(minStartTime, maxStartTime);
        RoutePoint[] routePoints = getDecomposedRoute(startPt, endPt, dateToTimestamp(startTime));
        if(routePoints != null) {
            double tTimeMillis = routePoints[routePoints.length - 1].getTimeAt().getTime() - routePoints[0].getTimeAt().getTime();
            double tTimeMinutes = tTimeMillis / 60000;
            
            //generate detours
            double percent = randomInt(minDetourPercent, maxDetourPercent);
            Integer detourMin, detourKm, detourPercent;
            detourMin = detourKm = detourPercent = null;
            if(randomBoolean(0.6)) {
                detourMin = (int)(tTimeMinutes * (percent / 100d));
            }
            if(randomBoolean(0.6)) {
                detourKm = (int)Math.rint(
                        (routePoints[routePoints.length - 1].getDistance() / 1000)
                        * (percent / 100)
                );
            }
            if(randomBoolean(0.6) || (detourKm == null && detourMin == null)) {
                detourPercent = (int)percent;
            }

            return new RandomDrive(
                randomArrayItem(driveName) + "_" + randomString(8), //drive name
                randomArrayItem(driveComment),          //drive comment
                startPt,                                //drive startPt
                endPt,                                  //drive endPt
                randomDate(minStartTime, maxStartTime), //drive start Time
                detourMin,                              //drive detour minutes
                detourKm,                               //detour in km
                detourPercent,                          //drive detour in percent
                randomArrayItem(driveOfferedSeats),     //drive offered Seats Number
                routePoints                             //dirve eqi distance route points
            );
        } else {
            return getRandomDrive(
                    minDistanceMeters,
                    maxDistanceMeters,
                    minStartTime,
                    maxStartTime,
                    minDetourPercent,
                    maxDetourPercent
            );
        }


    }

    public RandomRide getRandomRide(double minDistanceMeters, double maxDistanceMeters,
            java.util.Date minStartTime, java.util.Date maxStartTime,
            int minWaitTimeMinutes, int maxWaitTimeMinutes, double pricePerKm) {
        Point startPt = randomArrayItem(coordinates);
        Point endPt = randomPointWithinDistance(startPt, minDistanceMeters, maxDistanceMeters);
        Date startTimeEarly = randomDate(minStartTime, maxStartTime);
        Date startTimeLate = new Date(
                startTimeEarly.getTime()
                + (randomInt(minWaitTimeMinutes, maxWaitTimeMinutes) * 1000 * 60)
        );
        return new RandomRide(
            startTimeEarly,
            startTimeLate,
            randomArrayItem(rideNoPassengers),
            startPt,
            endPt,
            (Coordinate.distanceSphere(
                startPt.y,
                startPt.x,
                endPt.y,
                endPt.x
            ) / 1000d) * pricePerKm,              //price by air distance
            randomBoolean(0.95)          //is active
        );
    }

    public RandomFavoritePoint getRandomFavoritePoint() {
        return new RandomFavoritePoint(
                randomArrayItem(frequencies),
                randomArrayItem(favpoints)
        );
    }
    
    //CUSTOMER
//    public String getCustFirstName(){return randomArrayItem(custFirstName);}
//    public String getCustLastName(){return randomArrayItem(custLastname);}
//    public String getCustNickName(){return randomArrayItem(custNickname) + "_" + randomString(5);}
//    public String getCustPassword(){return randomArrayItem(custPasswd);}
//    public Date getCustDateOfBirth(){return new Date(randomInt(1940, 1996), randomInt(1, 12), randomInt(0, 28));}
//    public Character getCustGender(){return randomArrayItem(custGender);}
//    public String getCustMobilePhoneNumber(){return randomArrayItem(custMobilPhoneNumber);}
//    public String getCustEmail(){return randomArrayItem(custEmail);}
//    public Boolean getCustIsSmoker(){return randomBoolean(0.33);}
//    public Boolean getCustPostident(){return randomBoolean(0.2);}
//    public String getCustStreet(){return randomArrayItem(custStreet);}
//    public Integer getCustZipcode(){return randomArrayItem(custZipCode);}
//    public String getCustCity(){return randomArrayItem(custCity);}

    // ACCOUNTHistory
    public java.sql.Date getAcchTimeStamp(){
        return new java.sql.Date(
                new Date(
                    randomInt(2009, 2010),
                    randomInt(1, 12),
                    randomInt(1, 28),
                    randomInt(0, 23),
                    randomInt(0, 60),
                    randomInt(0, 60)
                ).getTime()
        )
    ;}
    public Double getAcchAmount(){return randomArrayItem(amounts);}

    //CARDETAILS
//    public String getCarBrand(){return randomArrayItem(carbrands);}
//    public String getCarBuildYear(){return randomInt(1950, 2009) + "";}
//    public String getCarColor(){return randomArrayItem(carColors);}
//    public String getCarPlateNo(){return randomInt(100000, 1000000) + "-" + randomInt(1000, 10000) + "-" + randomInt(10, 100);}

    //DRIVERUNDERTAKESRIDE
//    public String getDriveName(){return randomArrayItem(driveName);}
//    public Date getDriveStartTime(){return randomArrayItem(starttimes);}
//    public Double getDrivePricePerKm(){return randomArrayItem(drivePricePerKm);}
//    public Point getDriveStartpoint() {return randomArrayItem(coordinates);}
//    public Point getDriveEndpoint(Point startPt, double minDistanceMeters, double maxDistanceMeters) {
//        return randomPointWithinDistance(startPt, minDistanceMeters, maxDistanceMeters);
//    }
//    public Point getDriveEndpoint(Point startPt) {
//        return randomPointWithinDistance(startPt, DRIVE_MIN_DISTANCE_METERS, DRIVE_MAX_DISTANCE_METERS);
//    }
//    public java.sql.Date getDriveStartTime(Date min, Date max) {
//        return randomDate(min, max);
//    }

    
    
//    public String getDriveComment(){return randomArrayItem(driveComment);}
//    public Integer getDriveOfferedSeats(){return randomArrayItem(driveOfferedSeats);}
    
    // FAVOURITEPOINTS



    //RIDERUNDERTAKESRIDE
    //TODO: Why is this of type Boolean?
//    public int getRideNoPassengers(){return randomArrayItem(rideNoPassengers);}
//    public Double getRidePrice(){return randomArrayItem(prices);}
    //SEARCHAGENT
//    public Boolean getIsActive(){return randomBoolean(0.95);}
//    public Integer getWantedSeats(){return randomArrayItem(wantedSeats);}
//    public Double getPriceLimit(){return randomArrayItem(pricelimits);}
//    public Point getRideStartPt(){return randomArrayItem(coordinates);}
//
//    public Point getRideEndPt(Point startPt, double minDistanceMeters, double maxDistanceMeters){
//        return randomPointWithinDistance(startPt, minDistanceMeters, maxDistanceMeters);
//    }
//    public Point getRideEndPt(Point startPt){
//        return randomPointWithinDistance(startPt, RIDE_MIN_DISTANCE_METERS, RIDE_MAX_DISTANCE_METERS);
//    }

    private <T> T randomArrayItem(T[] array) {
        return array[rnd.nextInt(array.length)];
    }

    public int randomInt(int min, int max) {
        return rnd.nextInt(max - min + 1) + min;
    }

    private Timestamp randomTimestamp(Timestamp min, Timestamp max){
        return new Timestamp(randomLong(min.getTime(), max.getTime()));
    }

    private long randomLong(long min, long max){
        return rnd.nextLong() % (max - min + 1) + min;
    }

    private String randomString(int len) {
        byte[] b = new byte[len];
        for(int i=0;i<b.length;i++) {
            b[i]= (byte)randomInt(65, 90);
        }
        return new String(b);
    }

    private java.sql.Date randomDate(Date min, Date max) {
        long a = min.getTime();
        long b = max.getTime();
        return new java.sql.Date(randomLong(a, b));
    }

    private boolean randomBoolean(double p) {
        double d = rnd.nextDouble();
        return d < p;
    }

    private double randomDouble(double min, double max) {
        double d = rnd.nextDouble();
        return (d * (max - min + 1)) + min;
    }

    private Point randomPointWithinDistance(Point p, double minDistanceMeters, double maxDistanceMeters) {
        Point endPt = randomArrayItem(coordinates);
        double distance = Coordinate.distanceSphere(p.x, p.y, endPt.x, endPt.y);
        int tries = 0;
        while(distance < minDistanceMeters || distance > maxDistanceMeters && tries < GENERATE_COORDINATE_MAX_TRIES) {
            endPt = randomArrayItem(coordinates);
            distance = Coordinate.distanceSphere(p.x, p.y, endPt.x, endPt.y);
            tries++;
        }
        if(distance >= minDistanceMeters && distance <= maxDistanceMeters) {
            return endPt;
        } else {
            return null;
        }
    }

    /* ROUTER STUPH */

    private RoutePoint[] getDecomposedRoute(Point startPt, Point endPt, Timestamp startTime) {
        return router.getEquiDistantRoutePoints(
                new Coordinate[]{
                        new Coordinate(startPt.y, startPt.x),
                        new Coordinate(endPt.y, endPt.x)
                },
                startTime,
                true,
                Constants.ROUTER_NEAREST_NEIGHBOR_THRESHOLD,
                Constants.MATCHING_MAX_ROUTE_POINT_DISTANCE
        );
    }

//    private RoutePoint[] getOriginalRoute(Point startPt, Point endPt, Timestamp startTime) {
//        return router.findRoute(
//                new Coordinate(startPt.y, startPt.x),
//                new Coordinate(endPt.y, endPt.x),
//                startTime,
//                true,
//                Constants.ROUTER_NEAREST_NEIGHBOR_THRESHOLD
//        );
//    }

    /* COORDINATE STUPH */

    private Point[] getCoordinates() throws SQLException{
        Statement stmt = conn.createStatement();
        Point[] c = new Point[numVertices()];
        ResultSet rs = stmt.executeQuery(SQL_SELECT_VERTEX_COORDINATES);
        int i = 0;
        while(rs.next()){
            c[i++] = new Point(
                    rs.getDouble("lon"),
                    rs.getDouble("lat")
            );
        }
        rs.close();
        stmt.close();
        return c;
    }

    private int numVertices() throws SQLException{
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(SQL_SELECT_COUNT_VERTICES);
        int count;
        if(rs.next()){
            count = rs.getInt("count");
        }
        else {
            count = -1;
        }
        rs.close();
        stmt.close();
        return count;
    }

    private Timestamp dateToTimestamp(java.util.Date d) {
        return new Timestamp(d.getTime());
    }

    public class RandomCustomer {
        private final String nickName, password, firstName, lastName, mobilPhoneNumber, street, city, zipCode, email;
        private final java.util.Date dateOfBirth;
        private final char gender;
        private final boolean isSmoker, postIdent;
        private final char riderPrefGender, riderPrefSmoker, driverPrefGender, driverPrefSmoker;
        private final int riderPrefAge, driverPrefAge;
        private final int bankAccount, bankCode;

        public RandomCustomer(String nickName, String password, String firstName, 
                String lastName, String mobilPhoneNumber, String street, String city,
                Date dateOfBirth, char gender, boolean isSmoker,
                boolean postIdent, String zipCode, String email, char riderPrefGender,
                char riderPrefSmoker, char driverPrefGender, char driverPrefSmoker,
                int riderPrefAge, int driverPrefAge, int bankAccout, int bankCode) {
            this.nickName = nickName;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.mobilPhoneNumber = mobilPhoneNumber;
            this.street = street;
            this.city = city;
            this.dateOfBirth = dateOfBirth;
            this.gender = gender;
            this.isSmoker = isSmoker;
            this.postIdent = postIdent;
            this.zipCode = zipCode;
            this.email = email;
            this.riderPrefGender = riderPrefGender;
            this.riderPrefSmoker = riderPrefSmoker;
            this.driverPrefGender = driverPrefGender;
            this.driverPrefSmoker = driverPrefSmoker;
            this.riderPrefAge = riderPrefAge;
            this.driverPrefAge = driverPrefAge;
            this.bankAccount = bankAccout;
            this.bankCode = bankCode;
        }

        public String getCity() {
            return city;
        }

        public Date getDateOfBirth() {
            return dateOfBirth;
        }

        public String getFirstName() {
            return firstName;
        }

        public char getGender() {
            return gender;
        }

        public boolean isIsSmoker() {
            return isSmoker;
        }

        public String getLastName() {
            return lastName;
        }

        public String getMobilPhoneNumber() {
            return mobilPhoneNumber;
        }

        public String getPassword() {
            return password;
        }

        public boolean isPostIdent() {
            return postIdent;
        }

        public String getStreet() {
            return street;
        }

        public String getZipCode() {
            return zipCode;
        }

        public String getNickName() {
            return nickName;
        }

        public String getEmail() {
            return email;
        }

        public char getDriverPrefGender() {
            return driverPrefGender;
        }

        public char getDriverPrefSmoker() {
            return driverPrefSmoker;
        }

        public char getRiderPrefGender() {
            return riderPrefGender;
        }

        public char getRiderPrefSmoker() {
            return riderPrefSmoker;
        }

        public int getDriverPrefAge() {
            return driverPrefAge;
        }

        public int getRiderPrefAge() {
            return riderPrefAge;
        }

        public int getBankAccount() {
            return bankAccount;
        }

        public int getBankCode() {
            return bankCode;
        }
    }

    public class RandomCar {
        
        private final String brand, color, plateNo;
        private final Short buildYear;

        public RandomCar(String brand, Short buildYear, String color, String plateNo) {
            this.brand = brand;
            this.buildYear = buildYear;
            this.color = color;
            this.plateNo = plateNo;
        }

        public String getBrand() {
            return brand;
        }

        public Short getBuildYear() {
            return buildYear;
        }

        public String getColor() {
            return color;
        }

        public String getPlateNo() {
            return plateNo;
        }
    }

    public class RandomDrive {
        
        private final String name, comment;
        private final Point startPt, endPt;
        private final java.sql.Date startTime;
        private final Integer detourMinutes, detourKm, detourPercent;
        private final int offeredSeatsNo;
        private final RoutePoint[] routePoints;

        public RandomDrive(String name, String comment, Point startPt, Point endPt, java.sql.Date startTime, Integer detourMinutes, Integer detourKm, Integer detourPercent, int offeredSeatsNo, RoutePoint[] routePoints) {
            this.name = name;
            this.comment = comment;
            this.startPt = startPt;
            this.endPt = endPt;
            this.startTime = startTime;
            this.detourMinutes = detourMinutes;
            this.detourKm = detourKm;
            this.detourPercent = detourPercent;
            this.offeredSeatsNo = offeredSeatsNo;
            this.routePoints = routePoints;
        }

        public String getComment() {
            return comment;
        }

        public Integer getDetourMinutes() {
            return detourMinutes;
        }

        public Integer getDetourKm() {
            return detourKm;
        }

        public Integer getDetourPercent() {
            return detourPercent;
        }

        public Point getEndPt() {
            return endPt;
        }

        public String getName() {
            return name;
        }

        public int getOfferedSeatsNo() {
            return offeredSeatsNo;
        }

        public Point getStartPt() {
            return startPt;
        }

        public java.sql.Date getStartTime() {
            return startTime;
        }

        public RoutePoint[] getRoutePoints() {
            return routePoints;
        }
    }

    public class RandomRide {
        private final java.util.Date startTimeEarlies, startTimeLatest;
        private final int noPassengers;
        private final Point startPt, endPt;
        private final double price;
        private final boolean isActive;

        public RandomRide(java.util.Date startTimeEarlies, java.util.Date startTimeLatest, int noPassengers, Point startPt, Point endPt, double price, boolean isActive) {
            this.startTimeEarlies = startTimeEarlies;
            this.startTimeLatest = startTimeLatest;
            this.noPassengers = noPassengers;
            this.startPt = startPt;
            this.endPt = endPt;
            this.price = price;
            this.isActive = isActive;
        }

        public Point getEndPt() {
            return endPt;
        }

        public int getNoPassengers() {
            return noPassengers;
        }

        public double getPrice() {
            return price;
        }

        public Point getStartPt() {
            return startPt;
        }

        public java.util.Date getStartTimeEarlies() {
            return startTimeEarlies;
        }

        public java.util.Date getStartTimeLatest() {
            return startTimeLatest;
        }

        public boolean isActive() {
            return isActive;
        }
    }

    public class RandomFavoritePoint {
        private final int frequency;
        private final String name;

        public RandomFavoritePoint(int frequency, String name) {
            this.frequency = frequency;
            this.name = name;
        }

        public int getFrequency() {
            return frequency;
        }

        public String getName() {
            return name;
        }
    }

    private static class ReverseCoder {

        private String getUrl(double lon, double lat) {
            return "http://maps.google.com/maps/api/geocode/xml?latlng=" + lat + "," + lon + "&sensor=false";
        }



    }

    private class Address {
        Integer houseNo;
        String streetName;
        String city;

        public Address(Integer houseNo, String streetName, String city) {
            this.houseNo = houseNo;
            this.streetName = streetName;
            this.city = city;
        }
    }
}
