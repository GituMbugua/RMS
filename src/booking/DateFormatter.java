/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package booking;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFormattedTextField.AbstractFormatter;

/**
 *
 * @author Gitu
 */
public class DateFormatter extends AbstractFormatter {
    private String datePattern = "yyyy-MM-dd";
    private String dateTimePattern = "yyyy-MM-dd HH:mm";
    private String instantPattern = "yyyy-MM-dd HH:mm:ss.SSSXXX";
    private SimpleDateFormat simpleDateFormatter; 
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);

    
    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parse(text);
    }
    @Override
    public String valueToString(Object value) throws ParseException {
        simpleDateFormatter = new SimpleDateFormat(datePattern);
        if (value != null) {
            Calendar cal = (Calendar) value;
            String formatted = simpleDateFormatter.format(cal.getTime());
            LocalDate date =  LocalDate.parse(formatted);
            //LocalDate date =  convertToLocalDate(formatted);
            return date.format(dateFormatter);
        }
        return "";
    }
    public Instant stringToInstant(String date) throws ParseException, NullPointerException {
     
            simpleDateFormatter = new SimpleDateFormat(instantPattern);
            Date formatted = simpleDateFormatter.parse(date);
            //LocalDate d = LocalDate.parse(formatted);
            return formatted.toInstant();
    
    }
    public LocalDate stringToLocalDate(String date) throws ParseException, NullPointerException {
            simpleDateFormatter = new SimpleDateFormat(datePattern);
            Date formatted = simpleDateFormatter.parse(date);
            //LocalDate d = LocalDate.parse(formatted);
            return formatted.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            //return dateFormatter.format(d);
            //return d;
    }
    public LocalDateTime stringToLocalDateTime(String date) throws ParseException, NullPointerException {
            simpleDateFormatter = new SimpleDateFormat(dateTimePattern);
            Date formatted = simpleDateFormatter.parse(date);
            //LocalDate d = LocalDate.parse(formatted);
            return formatted.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            //return dateFormatter.format(d);
            //return d;
    }

    public String stringDate(String date) throws ParseException{
        simpleDateFormatter = new SimpleDateFormat(datePattern);
        if (date != null) {
            //Date formatted = simpleDateFormatter.parse(date);
            return simpleDateFormatter.format(date);
        }
        return "";
    }
    public String stringDateTime(Date date) throws ParseException{
        simpleDateFormatter = new SimpleDateFormat(dateTimePattern);
        if (date != null) {
            
            String formatted = simpleDateFormatter.format(date);
            return simpleDateFormatter.format(formatted);
        }
        return "";
    }
    public String stringInstant(String date) throws ParseException{
        simpleDateFormatter = new SimpleDateFormat(datePattern);
        if (date != null) {
            //Date formatted = simpleDateFormatter.parse(date);
            return simpleDateFormatter.format(date);
        }
        return "";
    }
    public LocalDate convertToLocalDate(Date dateToConvert) {
    return dateToConvert.toInstant()
      .atZone(ZoneId.systemDefault())
      .toLocalDate();
    }
    public LocalDateTime convertToLocalDateTime(Date dateToConvert) {
    return dateToConvert.toInstant()
      .atZone(ZoneId.systemDefault())
      .toLocalDateTime();
    }
    
    
}
