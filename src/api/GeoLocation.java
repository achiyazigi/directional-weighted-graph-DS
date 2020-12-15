package api;

public class GeoLocation implements geo_location {

    private double x, y, z;

    public GeoLocation(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public GeoLocation(geo_location geo) {
        this.x = geo.x();
        this.y = geo.y();
        this.z = geo.z();
    }

    
    /** 
     * get x.
     * @return double
     */
    @Override
    public double x() {
        return x;
    }

    
    /** 
     * get y.
     * @return double
     */
    @Override
    public double y() {
        return y;
    }

    
    /** 
     * get z.
     * @return double
     */
    @Override
    public double z() {
        return z;
    }

    
    /** 
     * compute distance.
     * @param g
     * @return double
     */
    @Override
    public double distance(geo_location g) {

        return (Math.sqrt(Math.pow(x-g.x(),2) + Math.pow(y-g.y(),2) + Math.pow(z-g.z(),2)) );
    }
}
