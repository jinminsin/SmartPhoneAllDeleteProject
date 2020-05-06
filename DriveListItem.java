package com.koia.smartphonealldelete;

import java.io.Serializable;
import java.text.DecimalFormat;

public class DriveListItem implements Serializable {
    private String driveName;
    private String drivePath;
    private long driveFullSize;
    private long driveFreeSize;

    public DriveListItem() {
    }

    public static String getFileSize(long size) {
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log(size) / Math.log(1024));
        return new DecimalFormat("#,###.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
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


    public long getDriveFullSize() {
        return driveFullSize;
    }

    public void setDriveFullSize(long driveFullSize) {
        this.driveFullSize = driveFullSize;
    }

    public long getDriveFreeSize() {
        return driveFreeSize;
    }

    public void setDriveFreeSize(long driveFreeSize) {
        this.driveFreeSize = driveFreeSize;
    }
}
