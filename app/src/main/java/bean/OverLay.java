package bean;

import java.io.Serializable;

/**
 * Created by 孵孵 on 2018/4/15 0015.
 */

public class OverLay implements Serializable{
    public boolean showInfoWindow=false;
    double lat,lng;
    String name,type,id;

    public OverLay(){}

    public OverLay(double lat, double lng, String name, String type) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OverLay overLay = (OverLay) o;

        if (Double.compare(overLay.lat, lat) != 0) return false;
        if (Double.compare(overLay.lng, lng) != 0) return false;
        if (name != null ? !name.equals(overLay.name) : overLay.name != null) return false;
        return type != null ? type.equals(overLay.type) : overLay.type == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(lat);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lng);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
