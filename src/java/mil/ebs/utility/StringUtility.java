package mil.ebs.utility;

public class StringUtility {

    public static boolean containsMatchValue(final String pValue, final String... pMatchList) {
        boolean result = false;

        if (pMatchList != null) {
            for (final String matchItem : pMatchList) {
                if (containsValue(matchItem) && matchItem.equalsIgnoreCase(pValue)) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    public static boolean containsValue(final String pValue) {
        boolean result = false;
        if (pValue != null && !"".equalsIgnoreCase(pValue.trim())) {
            result = true;
        }

        return result;
    }

}
