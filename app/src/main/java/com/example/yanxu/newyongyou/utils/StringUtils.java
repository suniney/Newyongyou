package com.example.yanxu.newyongyou.utils;

/**
 * @author yanxu
 * @date 2016/5/23.
 */
public class StringUtils {
    /**
     * case insensitive removal of a substring if it is at the end of a source string,
     * otherwise returns the source string.
     * <p/>
     * a <code>null</code> source string will return <code>null</code>.
     * an empty ("") source string will return the empty string.
     * a <code>null</code> search string will return the source string.
     * <p/>
     * <pre>
     * stringutils.removeend(null, *)      = null
     * stringutils.removeend("", *)        = ""
     * stringutils.removeend(*, null)      = *
     * stringutils.removeend("www.domain.com", ".com.")  = "www.domain.com."
     * stringutils.removeend("www.domain.com", ".com")   = "www.domain"
     * stringutils.removeend("www.domain.com", "domain") = "www.domain.com"
     * stringutils.removeend("abc", "")    = "abc"
     * </pre>
     *
     * @param str    the source string to search, may be null
     * @param remove the string to search for (case insensitive) and remove, may be null
     * @return the substring with the string removed if found,
     * <code>null</code> if null string input
     * @since 2.4
     */
    public static String removeendignorecase(String str, String remove) {
        if (isempty(str) || isempty(remove)) {
            return str;
        }
        if (endswithignorecase(str, remove)) {
            return str.substring(0, str.length() - remove.length());
        }
        return str;
    }

    /**
     * case insensitive check if a string ends with a specified suffix.
     * <p/>
     * <code>null</code>s are handled without exceptions. two <code>null</code>
     * references are considered to be equal. the comparison is case insensitive.
     * <p/>
     * <pre>
     * stringutils.endswithignorecase(null, null)      = true
     * stringutils.endswithignorecase(null, "abcdef")  = false
     * stringutils.endswithignorecase("def", null)     = false
     * stringutils.endswithignorecase("def", "abcdef") = true
     * stringutils.endswithignorecase("def", "abcdef") = false
     * </pre>
     *
     * @param str    the string to check, may be null
     * @param suffix the suffix to find, may be null
     * @return <code>true</code> if the string ends with the suffix, case insensitive, or
     * both <code>null</code>
     * @since 2.4
     */
    public static boolean endswithignorecase(String str, String suffix) {
        return endswith(str, suffix, true);
    }

    /**
     * check if a string ends with a specified suffix (optionally case insensitive).
     *
     * @param str        the string to check, may be null
     * @param suffix     the suffix to find, may be null
     * @param ignorecase inidicates whether the compare should ignore case
     *                   (case insensitive) or not.
     * @return <code>true</code> if the string starts with the prefix or
     * both <code>null</code>
     */
    private static boolean endswith(String str, String suffix, boolean ignorecase) {
        if (str == null || suffix == null) {
            return (str == null && suffix == null);
        }
        if (suffix.length() > str.length()) {
            return false;
        }
        int stroffset = str.length() - suffix.length();
        return str.regionMatches(ignorecase, stroffset, suffix, 0, suffix.length());
    }
    // empty checks
    //-----------------------------------------------------------------------

    /**
     * checks if a string is empty ("") or null.
     * <p/>
     * <pre>
     * stringutils.isempty(null)      = true
     * stringutils.isempty("")        = true
     * stringutils.isempty(" ")       = false
     * stringutils.isempty("bob")     = false
     * stringutils.isempty("  bob  ") = false
     * </pre>
     * <p/>
     * note: this method changed in lang version 2.0.
     * it no longer trims the string.
     * that functionality is available in isblank().
     *
     * @param str the string to check, may be null
     * @return <code>true</code> if the string is empty or null
     */
    public static boolean isempty(String str) {
        return str == null || str.length() == 0;
    }
}
