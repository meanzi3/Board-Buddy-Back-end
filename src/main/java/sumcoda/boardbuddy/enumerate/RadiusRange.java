package sumcoda.boardbuddy.enumerate;

public enum RadiusRange {
    FAR_DONG(10),
    LITTLE_FAR_DONG(7),
    LITTLE_CLOSE_DONG(5),
    CLOSE_DONG(2);

    private final int radius;

    RadiusRange(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }
}