package me.ccattell.plugins.cnu.utilities;

import static me.ccattell.plugins.cnu.CompleteNovusUtilities.plugin;

;

/**
 *
 * @author Charlie
 */
public class MajorMinor {

    String[] major = plugin.getConfig().getString("System.Currency.Major").split(":");
    String majorSingle = major[0];
    String majorPlural = major[1];
    String[] minor = plugin.getConfig().getString("System.Currency.Minor").split(":");
    String minorSingle = major[0];
    String minorPlural = major[1];
    String format = plugin.getConfig().getString("System.Currency.Formatting");

    public void CNUMajorMinor() {
    }

    public String getFormat(float pay_amount) {
        String s;
        String major_str;
        String minor_str;
        long major_amt;
        long minor_amt;
        double m;
        if (format.equalsIgnoreCase("combined")) {
            if (pay_amount == 1) {
                major_str = majorSingle;
            } else {
                major_str = majorPlural;
            }
            s = pay_amount + " " + major_str;
        } else {
            s = "";
            major_amt = (long) pay_amount;
            m = pay_amount - major_amt;
            m = m * 100;
            minor_amt = (long) m;
            if (major_amt > 0) {
                if (major_amt == 1) {
                    major_str = majorSingle;
                } else {
                    major_str = majorPlural;
                }
                s = s + major_amt + " " + major_str + " ";
            }
            if (minor_amt > 0) {
                if (minor_amt == 1) {
                    minor_str = minorSingle;
                } else {
                    minor_str = minorPlural;
                }
                s = s + minor_amt + " " + minor_str;
            }
        }
        return s;
    }
}
