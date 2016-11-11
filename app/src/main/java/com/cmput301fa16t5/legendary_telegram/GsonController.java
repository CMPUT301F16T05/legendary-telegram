package com.cmput301fa16t5.legendary_telegram;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;

/**
 * Gson controller class.
 * All methods are static; no objects needed, no constructor.
 * Contains:
 * A method to check if a file exists on disk.
 * A method to load a User Object from disk.
 * A method to save a User Object to disk.
 * @author kgmills
 */
public class GsonController {

    /**
     * Checks if a file corresponding to a given username exists.
     * Used simply to check if a username is unique, locally.
     * @param userName: The username as a string.
     * @param context: Needed to get file list.
     * @return True of the file exists. False otherwise.
     */
    public static boolean checkIfExists(String userName, Context context) {
        if (userName.equals("") || userName == null) {
            return false;
        }

        for (String s: context.fileList()) {
            if ((userName + ".sav").equals(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes an old user name.
     * @param oldName: String of the old user.
     * @param context: Needed to use Gson.
     */
    public static void deleteOldUserName(String oldName, Context context) {
        context.deleteFile(oldName + ".sav");
    }

    /**
     * Uses GSON to load a user object related to a given
     * username from disk.
     * @param userName: The username as a string.
     * @param context: Context needed to use GSON.
     * @return The User object, if successful. Null otherwise.
     */
    public static User loadUserInfo(String userName, Context context) {
        // Taken from LonelyTwitter and assignment 1
        try {
            FileInputStream fis = context.getApplicationContext().openFileInput(userName + ".sav");
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            Type userType = new TypeToken<User>() {
            }.getType();

            return gson.fromJson(in, userType);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    /**
     * Uses GSON to save a User object to disk
     * @param user: The User object to be saved.
     * @param context: Context needed to use GSON.
     */
    public static void saveUserToDisk(User user, Context context) {
        // Taken from LonelyTwitter and assignment 1
        try {
            FileOutputStream fos = context.openFileOutput(user.getUserName() + ".sav", 0);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(user, out);
            out.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }

    }
}

