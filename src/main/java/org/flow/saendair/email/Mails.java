package org.flow.saendair.email;

import java.util.Arrays;

public class Mails {
    private Email[] mails;

    public Mails() {
    }

    public Mails(Email[] mails) {
        this.mails = mails;
    }

    public Email[] getMails() {
        return mails;
    }

    public void setMails(Email[] mails) {
        this.mails = mails;
    }

    @Override
    public String toString() {
        return "Mails{" +
                "mails=" + Arrays.toString(mails) +
                '}';
    }
}
