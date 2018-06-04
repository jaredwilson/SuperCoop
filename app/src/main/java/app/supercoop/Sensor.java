package app.supercoop;

public class Sensor {
    public String temp;
    public String humid;
    public String ammonia;

    public Sensor() {
        temp = "70";
        humid = "50";
        ammonia = "5";
    }

    public String getAmmonia() {
        return ammonia;
    }

    public String getHumid() {
        return humid;
    }

    public String getTemp() {
        return temp;
    }

    public void setAmmonia(String ammonia) {
        this.ammonia = ammonia;
    }

    public void setHumid(String humid) {
        this.humid = humid;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}
