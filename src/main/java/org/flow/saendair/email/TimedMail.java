package org.flow.saendair.email;

import java.awt.*;
import java.io.IOException;
import java.util.TimerTask;

import static org.flow.saendair.SaendAir.generateAndSendMail;
import static org.flow.saendair.SaendAir.parseJson;

public class TimedMail {
    private int countDown;
    Toolkit toolkit;
    java.util.Timer timer;

    public TimedMail(int countDown) {
        this.countDown = countDown;
        toolkit = Toolkit.getDefaultToolkit();
        timer = new java.util.Timer();
        timer.schedule(new TimingTask(),
                0, 1000);
    }

    class TimingTask extends TimerTask {
        public void run() {
            if (countDown > 0) {
                System.out.println(String.valueOf(countDown));
                countDown--;
            } else {
                Mails generatedMails = null;
                try {
                    generatedMails = parseJson("./emails.json");
                } catch (IOException io) {
                    io.printStackTrace();
                }

                generateAndSendMail(generatedMails);
                System.out.println("Email sent.");
                //timer.cancel();
                System.exit(0); //Stops the AWT thread and anything else.
            }
        }
    }
}
