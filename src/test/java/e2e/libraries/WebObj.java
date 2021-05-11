package e2e.libraries;

import org.openqa.selenium.By;

public class WebObj {

    public By by;
    public String desc;
    public String value;

    public WebObj(By by, String desc) {
        this.by = by;
        this.desc = desc;
        this.value = "";
    }

}
