package com.automation.platform.shared.services.utils;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class StringUtils {

    private final static Logger LOGGER = Logger.getLogger(StringUtils.class.getName());

    public static List<String> manipulateListOfString(String inputStr){
        if (!inputStr.isEmpty()) {
            List<String> strList = Arrays.asList(inputStr.split(","));

            for (String element : strList) {
                LOGGER.info("String List element="+element);
            }
            return strList;
        }
        return null;
    }
}
