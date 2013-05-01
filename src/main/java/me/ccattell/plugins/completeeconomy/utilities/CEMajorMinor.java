/*
 *  Copyright 2013 eccentric_nz.
 */
package me.ccattell.plugins.completeeconomy.utilities;

import me.ccattell.plugins.completeeconomy.CompleteEconomy;

/**
 *
 * @author eccentric_nz
 */
public class CEMajorMinor {

    String major[] = CompleteEconomy.plugin.getConfig().getString("System.Currency.Major").split(":");
    String majorSingle = major[0];
    String majorPlural = major[1];

    String minor[] = CompleteEconomy.plugin.getConfig().getString("System.Currency.Minor").split(":");
    String minorSingle = major[0];
    String minorPlural = major[1];
    
    String format = CompleteEconomy.plugin.getConfig().getString("System.Currency.Formatting");

    public void CEMajorMinor() {
    }

    public String getFormat(float pay_amount) {
        String s;
        String major;
        String minor;
        long major_amt;
        long minor_amt;
        double m;
        if (format.equalsIgnoreCase("combined")) {
            if (pay_amount == 1) {
                major = majorSingle;
            } else {
                major = majorPlural;
            }
            s = pay_amount + " " + major;
        } else {
            s = "";
            major_amt = (long) pay_amount;
            m = pay_amount - major_amt;
            m = m * 100;
            minor_amt = (long) m;
            if (major_amt > 0) {
                if (major_amt == 1) {
                    major = majorSingle;
                } else {
                    major = majorPlural;
                }
                s = s + major_amt + " " + major + " ";
            }
            if (minor_amt > 0) {
                if (minor_amt == 1) {
                    minor = minorSingle;
                } else {
                    minor = minorPlural;
                }
                s = s + minor_amt + " " + minor;
            }
        }
        return s;
    }
}
