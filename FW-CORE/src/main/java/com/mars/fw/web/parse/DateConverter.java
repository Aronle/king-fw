package com.mars.fw.web.parse;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description: 全局日期参数统一处理 拦截 参数类型是Date的参数对其做统一的处理
 * @author:dengjinde
 * @date:2020/04/20
 */
@Slf4j
@Service
public class DateConverter implements Converter<String, Date> {


    @Override
    public Date convert(String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }
        if (s.matches(formartEnum.yyyy_MM.getRegex())) {
            return parseDate(s, formartEnum.yyyy_MM.getFormat());
        } else if (s.matches(formartEnum.yyyy_MM_dd.getRegex())) {
            return parseDate(s, formartEnum.yyyy_MM_dd.getFormat());
        } else if (s.matches(formartEnum.yyyy_MM_ddHH.getRegex())) {
            return parseDate(s, formartEnum.yyyy_MM_ddHH.getFormat());
        } else if (s.matches(formartEnum.yyyy_MM_ddHH_mm.getRegex())) {
            return parseDate(s, formartEnum.yyyy_MM_ddHH_mm.getFormat());
        } else if (s.matches(formartEnum.yyyy_MM_ddHH_mm_ss.getRegex())) {
            return parseDate(s, formartEnum.yyyy_MM_ddHH_mm_ss.getFormat());
        } else {
            throw new IllegalArgumentException("Invalid boolean value '" + s + "'");
        }
    }


    /**
     * 格式化日期
     *
     * @param dateStr
     * @param format
     * @return
     */
    private Date parseDate(String dateStr, String format) {
        Date date = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return date;
    }

    private enum formartEnum {
        yyyy_MM("yyyy-MM", "^\\d{4}-\\d{1,2}$"),
        yyyy_MM_dd("yyyy-MM-dd", "^\\d{4}-\\d{1,2}-\\d{1,2}$"),
        yyyy_MM_ddHH("yyyy-MM-dd HH", "^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}"),
        yyyy_MM_ddHH_mm("yyyy-MM-dd HH:mm", "^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$"),
        yyyy_MM_ddHH_mm_ss("yyyy-MM-dd HH:mm:ss", "^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$");

        private String format;
        private String regex;

        private formartEnum(String format, String regex) {
            this.format = format;
            this.regex = regex;
        }

        public String getFormat() {
            return format;
        }

        public String getRegex() {
            return regex;
        }

    }


}
