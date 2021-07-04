package dev.futurex.futurex.util.sec;

import dev.futurex.futurex.FutureX;

import java.net.URL;
import java.util.Base64;
import java.util.Scanner;

public class HWIDCheck {

    public static boolean checkHWID() {
        try {
            String list = (new Scanner((new URL(new String(Base64.getDecoder().decode("aHR0cHM6Ly9yYXcuZ2l0aHVidXNlcmNvbnRlbnQuY29tL0Z1dHVyZVhDbGllbnQvRnV0dXJlWC1yZXNvdXJjZXMvbWFzdGVyL2h3aWQudHh0")))).openStream(), "UTF-8")).useDelimiter("\\A").next();
            return list.contains(HWIDUtil.getHWID());
        } catch (Exception var1) {
            return false;
        }
    }

}
