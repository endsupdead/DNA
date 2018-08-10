package cpe.dna;

/**
 * Created by anthony on 2/21/18.
 */

public class Params {

    private String params;
    private String readings;

    public Params(String params, String readings) {
        this.params = params;
        this.readings = readings;
    }

    public String getParams() {
        return params;
    }

    public String getReadings() {
        return readings;
    }
}
