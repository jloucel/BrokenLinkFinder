package com.brokenlinkfinder.utils;

import java.net.MalformedURLException;
import java.net.URL;
/**
 * Attempts to create a URL object from a string,
 * returns true if no errors are returned
 *
 * Created by loucelj on 4/19/2017.
 */
public class isValidURL {
    public static boolean check(String url){
        try {
            new URL(url);
            return true;
        }
        catch (MalformedURLException e) {
            return false;
        }
    }
}
