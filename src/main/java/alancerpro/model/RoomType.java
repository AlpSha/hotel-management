package alancerpro.model;

public enum RoomType {
    singleStandard, singleDeluxe, doubleStandard, doubleDeluxe;

    public double getRoomPrice() {
        switch (this) {
            case singleStandard:
                return 20;
            case singleDeluxe:
                return 40;
            case doubleStandard:
                return 35;
            case doubleDeluxe:
                return 60;
        }
        return 0;
    }
}
