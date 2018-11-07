package mil.ebs.utility;

import org.apache.poi.ss.usermodel.Cell;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Parser {

    private List<String> parserErrors;


    public Parser() {
        parserErrors = new ArrayList<String>();
    }

    public List<String> getParserErrors() {
        return parserErrors;
    }


    private boolean isPopulatedCell(final Cell pCell) {
        return (pCell != null && !pCell.toString().equals(""));
    }

    private void addError(final int pRow, final String pField, final String pErrorMsg) {
        parserErrors.add(pErrorMsg + " in <b>row</b>: " + pRow + " <b>column</b>: " + pField);
    }


    public Long parseLong(final int pRowNumber, final String pFieldName, final Cell pCell, final boolean pRequired) {
        Long num = null;
        try {
            // retrieves value - numeric cells we throw an exception
            // formula cells returns the pre-calculated value if a string, otherwise an exception.
            num = pCell != null && isPopulatedCell(pCell) ? (long) pCell.getNumericCellValue() : null;
        } catch (Exception e) {
            addError(pRowNumber, pFieldName, e.toString());
        }

        if (num == null && pRequired) {
            addError(pRowNumber, pFieldName, "Missing value");
        }

        return num;
    }

    public int parseInt(final int pRowNumber, final String pFieldName, final Cell pCell, final boolean pRequired) {
        Long num = parseLong(pRowNumber, pFieldName, pCell, pRequired);
        return num != null ? num.intValue() : 0;
    }

    public Double parseDouble(final int pRowNumber, final String pFieldName, final Cell pCell, final boolean pRequired) {
        Double num = null;
        try {
            // retrieves value - numeric cells we throw an exception
            // formula cells returns the pre-calculated value if a string, otherwise an exception.
            num = pCell != null ? pCell.getNumericCellValue() : null;
        } catch (Exception e) {
            addError(pRowNumber, pFieldName, e.toString());
        }

        if (num == null && pRequired) {
            addError(pRowNumber, pFieldName, "Missing value");
        } else {
            num = 0.0;
        }

        return num;
    }

    public Double parseFormattedDouble(final int pRowNumber, final String pFieldName, final Cell pCell, final String pFormat, final boolean pRequired) {
        // retrieves numeric cell's value
        DecimalFormat df = new DecimalFormat(pFormat);
        Double num = parseDouble(pRowNumber, pFieldName, pCell, pRequired);

        // trims the fraction portion of the value according to format
        num = new Double(df.format(num));

        // determine the format's allowable number of digits before the decimal place
        int fInteger = pFormat.split("\\.", 2)[0].length();

        // determine the parsed value's number of digits before the decimal place
        int vInteger =  num.toString().split("\\.", 2)[0].length();

        // if the values number of digits exceeds the allowable number of digits log error
        if (vInteger > fInteger) {
            addError(pRowNumber, pFieldName, "Value does not match expected number format " + pFormat);
        }

        return num;
    }

    public String parseValue(final int pRowNumber, final String pFieldName, final Cell pCell, final boolean pRequired) {
        String value = null;
        try {
            // retrieves value - numeric cells we throw an exception
            // formula cells returns the pre-calculated value if a string, otherwise an exception.
            value = isPopulatedCell(pCell) ? pCell.getRichStringCellValue().getString() : "";
        } catch (Exception e) {
            addError(pRowNumber, pFieldName, e.toString());
        } finally {
            if ((value == null || "".equals(value)) && pRequired) {
                addError(pRowNumber, pFieldName, "Missing value");
            }
        }

        return value;
    }

    public Date parseDate(final int pRowNumber, final String pFieldName, final Cell pCell, final String pAcceptedDateFormat, final boolean pRequired) {
        Date dbDate = null;
        // pAcceptedDateFormat must be an valid date format
        DateFormat df = new SimpleDateFormat(pAcceptedDateFormat);

        try {
            // retrieves date - if date can be parsed
            dbDate = pCell != null ? pCell.getDateCellValue() : null;
            // formats date to remove time portion of date
            if(dbDate != null){
                dbDate = df.parse(df.format(dbDate));
            }
        } catch (ParseException pe) {
            addError(pRowNumber, pFieldName, "Unable to parse date (date format must be \" + pAcceptedDateFormat + \")");
        } catch (NumberFormatException nfe) {
            addError(pRowNumber, pFieldName, "Unable to parse date (Cell must be of type date)");
        } catch (IllegalStateException ise) {
            // cell is CELL_TYPE_STRING - attempt using acceptable format
            try {
                dbDate = df.parse(pCell.getRichStringCellValue().getString());
            } catch (ParseException exception) {
                addError(pRowNumber, pFieldName, "Unable to parse date (date format must be " + pAcceptedDateFormat + ")");
            }
        }

        if (dbDate == null && pRequired) {
            addError(pRowNumber, pFieldName, "Missing value");
        }

        return dbDate;
    }

}
