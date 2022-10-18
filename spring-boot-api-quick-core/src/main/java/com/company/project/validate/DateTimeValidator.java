package com.company.project.validate;

import com.company.project.utils.DateUtils;
import com.company.project.validate.rule.DateTimeStr;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.format.DateTimeFormatter;

public class DateTimeValidator implements ConstraintValidator<DateTimeStr, String> {

    private DateTimeStr dateTimeStr;
    private DateTimeFormatter formatter;

    @Override
    public void initialize(DateTimeStr dateTimeStr) {
        this.dateTimeStr = dateTimeStr;
        this.formatter = DateTimeFormatter.ofPattern(dateTimeStr.format());
    }


    /***
     *
     * @param value
     * @param context
     * @return true 检验通过
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        try {
            String format = dateTimeStr.format();
            DateUtils.parseDate(value, format);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
