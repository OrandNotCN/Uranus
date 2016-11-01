package gaia.uranus.pojo;

/**
 * creator：OrandNot on 2016/2/22 15:53
 * email：zhoulianchun@foxmail.com
 */
public class MapMarker {
    private String contactorId;
    private String remark;
    private double longitude;
    private double latitude;
    private String relationship;

    public String getContactorId() {
        return contactorId;
    }

    public void setContactorId(String contactorId) {
        this.contactorId = contactorId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
