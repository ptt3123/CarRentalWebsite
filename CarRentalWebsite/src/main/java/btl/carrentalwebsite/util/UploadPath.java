package btl.carrentalwebsite.util;

public enum UploadPath {
    UPLOAD_DIR("D:\\MyProjects\\Java\\CarRentalWebsite\\uploads\\"),
    BROKEN_REPORT_IMG_DIR("Broken-Report-IMG\\"),
    CAR_AVT_DIR("Car-AVT\\"),
    CAR_IMG_DIR("Car-IMG\\"),
    CAR_VID_DIR("Car-VID\\"),
    COLLATERAL_IMG_DIR("Collateral-IMG\\"),
    USER_AVT_DIR("User-AVT\\");

    private final String path;

    UploadPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}