/**
 * Copyright (c) 2013 LEAP Encryption Access Project and contributers
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package se.leap.bitmaskclient;

import android.content.res.*;

import java.util.*;

public class UserSessionStatus extends Observable {
    public static String TAG = UserSessionStatus.class.getSimpleName();
    private static UserSessionStatus current_status;
    private static Resources resources;

    public enum SessionStatus {
        LOGGED_IN,
        LOGGED_OUT,
        NOT_LOGGED_IN,
        DIDNT_LOG_OUT,
        LOGGING_IN,
        LOGGING_OUT,
        SIGNING_UP
    }

    private static SessionStatus session_status = SessionStatus.NOT_LOGGED_IN;

    public static UserSessionStatus getInstance(Resources resources) {
        if (current_status == null) {
            current_status = new UserSessionStatus(resources);
        }
        return current_status;
    }

    private UserSessionStatus(Resources resources) {
        UserSessionStatus.resources = resources;
    }

    private void sessionStatus(SessionStatus session_status) {
        this.session_status = session_status;
    }

    public SessionStatus sessionStatus() {
        return session_status;
    }

    public boolean inProgress() {
        return session_status == SessionStatus.LOGGING_IN
                || session_status == SessionStatus.LOGGING_OUT;
    }

    public static void updateStatus(SessionStatus session_status, Resources resources) {
        current_status = getInstance(resources);
        current_status.sessionStatus(session_status);
        current_status.setChanged();
        current_status.notifyObservers();
    }

    @Override
    public String toString() {
        String username = User.userName();

        return username + " " + conjugateToBe(username) + " "
                + session_status.toString().toLowerCase().replaceAll("_", " ");
    }

    private String conjugateToBe(String subject) {
        String conjugation = "";
        String[] personal_pronouns = resources.getStringArray(R.array.personal_pronouns);
        String[] verb_to_be = resources.getStringArray(R.array.verb_to_be);
        for (int i = 0; i < personal_pronouns.length ; i++)
            if(subject.equalsIgnoreCase(personal_pronouns[i])) {
                conjugation = verb_to_be[i];
                break;
            }
        if(conjugation.isEmpty()) conjugation = verb_to_be[User.DEFAULT_CONJUGATION_PERSON];

        return conjugation;
    }
}
