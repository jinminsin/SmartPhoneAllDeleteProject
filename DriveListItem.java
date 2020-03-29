package com.koia.smartphonealldelete;

public class DriveListItem {
    private String driveName;
    private String drivePath;
    private String driveFullSize;
    private String driveFreeSize;

    public DriveListItem() {
    }

    public String getDriveName() {
        return driveName;
    }

    public void setDriveName(String driveName) {
        this.driveName = driveName;
    }

    public String getDrivePath() {
        return drivePath;
    }

    public void setDrivePath(String drivePath) {
        this.drivePath = drivePath;
    }


    public String getDriveFullSize() {
        return driveFullSize;
    }

    public void setDriveFullSize(String driveFullSize) {
        this.driveFullSize = driveFullSize;
    }

    public String getDriveFreeSize() {
        return driveFreeSize;
    }

    public void setDriveFreeSize(String driveFreeSize) {
        this.driveFreeSize = driveFreeSize;
    }
}
