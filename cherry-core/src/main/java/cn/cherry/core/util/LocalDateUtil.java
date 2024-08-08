package cn.cherry.core.util;

import cn.cherry.core.constant.DatePattern;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 时间处理工具类 支持java.time包下时间处理（LocalDateTime、LocalDate、LocalTime)
 *
 * @author :  sinbad.cheng
 * @since :  2024-08-08 15:43
 */
public final class LocalDateUtil {
    private LocalDateUtil() {}

    /**
     * yyyy-MM-dd HH:mm:ss 时间格式器
     */
    public static DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DatePattern.yyyy_MM_dd_HH_mm_ss);

    /**
     * yyyy-MM-dd 时间格式器
     */
    public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DatePattern.yyyy_MM_dd);

    /**
     * HH:mm:ss 时间格式器
     */
    public static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(DatePattern.HH_mm_ss);

    private static final Map<String, DateTimeFormatter> DATE_TIME_FORMATTER_CACHE = new HashMap<>(16);

    static {
        DATE_TIME_FORMATTER_CACHE.put(DatePattern.yyyy_MM_dd_HH_mm_ss, DATETIME_FORMATTER);
        DATE_TIME_FORMATTER_CACHE.put(DatePattern.yyyy_MM_dd, DATE_FORMATTER);
        DATE_TIME_FORMATTER_CACHE.put(DatePattern.HH_mm_ss, TIME_FORMATTER);
    }

    /**
     * 获取时间格式器
     *
     * @param pattern 格式
     * @return {@link DateTimeFormatter}
     */
    public static DateTimeFormatter getDateTimeFormatter(String pattern) {
        return DATE_TIME_FORMATTER_CACHE.computeIfAbsent(pattern, DateTimeFormatter::ofPattern);
    }

    /**
     * 时间格式化
     */
    public static String format(LocalDateTime dateTime) {
        return dateTime == null ? "" : dateTime.format(DATETIME_FORMATTER);
    }

    /**
     * 时间格式化
     */
    public static String format(LocalDate date) {
        return date == null ? "" : date.format(DATE_FORMATTER);
    }

    /**
     * 时间格式化
     */
    public static String format(LocalDate date, String pattern) {
        return date == null || pattern == null ? "" : date.format(getDateTimeFormatter(pattern));
    }

    /**
     * 时间格式化
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        return dateTime == null || pattern == null ? "" : dateTime.format(getDateTimeFormatter(pattern));
    }

    /**
     * 时间格式化
     */
    public static String format(LocalTime time) {
        return time == null ? "" : time.format(TIME_FORMATTER);
    }

    /**
     * <p>
     * 时间格式化, 不是时间类型直接返回toString值
     * </p>
     *
     * @param obj
     * @return
     */
    public static String format(Object obj) {
        if (obj == null) {
            return "";
        }
        if (obj instanceof LocalDateTime) {
            return format((LocalDateTime) obj);
        }
        if (obj instanceof LocalDate) {
            return format((LocalDate) obj);
        }
        if (obj instanceof LocalTime) {
            return format((LocalTime) obj);
        }
        return obj.toString();
    }

    /**
     * 时间文本解析
     */
    public static LocalDateTime parseDateTime(CharSequence text) {
        return LocalDateTime.parse(text, DATETIME_FORMATTER);
    }

    /**
     * 时间文本解析
     */
    public static LocalDateTime parseDateTime(CharSequence text, String pattern) {
        return LocalDateTime.parse(text, getDateTimeFormatter(pattern));
    }

    /**
     * 时间文本解析
     */
    public static LocalDateTime parseDateTime(CharSequence text, String[] patterns) {
        if (patterns == null || patterns.length == 0) {
            return LocalDateUtil.parseDateTime(text);
        }

        RuntimeException re = null;
        for (String pattern : patterns) {
            try {
                return LocalDateUtil.parseDateTime(text, pattern);
            } catch (RuntimeException e) {
                re = e;
            } catch (Throwable e) {
                re = new RuntimeException(e);
            }
        }

        throw re;
    }

    /**
     * 时间解析
     */
    public static LocalDateTime parseDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 时间解析
     */
    public static LocalDateTime parseDateTime(Date date, ZoneId zoneId) {
        if (date == null) {
            return null;
        }
        if (zoneId == null) {
            zoneId = ZoneId.systemDefault();
        }
        return date.toInstant().atZone(zoneId).toLocalDateTime();
    }

    /**
     * 时间文本解析
     */
    public static LocalDate parseDate(CharSequence text) {
        return LocalDate.parse(text, DATE_FORMATTER);
    }

    /**
     * 时间文本解析
     */
    public static LocalDate parseDate(CharSequence text, String pattern) {
        return LocalDate.parse(text, getDateTimeFormatter(pattern));
    }

    /**
     * 时间文本解析
     */
    public static LocalDate parseDate(CharSequence text, String[] patterns) {
        if (patterns == null || patterns.length == 0) {
            return LocalDateUtil.parseDate(text);
        }

        RuntimeException re = null;
        for (String pattern : patterns) {
            try {
                return LocalDateUtil.parseDate(text, pattern);
            } catch (RuntimeException e) {
                re = e;
            } catch (Throwable e) {
                re = new RuntimeException(e);
            }
        }

        throw re;
    }

    /**
     * 日期解析
     */
    public static LocalDate parseDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 日期解析
     */
    public static LocalDate parseDate(Date date, ZoneId zoneId) {
        if (date == null) {
            return null;
        }
        if (zoneId == null) {
            zoneId = ZoneId.systemDefault();
        }
        return date.toInstant().atZone(zoneId).toLocalDate();
    }

    /**
     * 时间文本解析
     */
    public static LocalTime parseTime(CharSequence text) {
        return LocalTime.parse(text, TIME_FORMATTER);
    }

    /**
     * 时间文本解析
     */
    public static LocalTime parseTime(CharSequence text, String pattern) {
        return LocalTime.parse(text, getDateTimeFormatter(pattern));
    }

    /**
     * 日期解析
     */
    public static LocalTime parseTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    /**
     * 日期解析
     */
    public static LocalTime parseTime(Date date, ZoneId zoneId) {
        if (date == null) {
            return null;
        }
        if (zoneId == null) {
            zoneId = ZoneId.systemDefault();
        }
        return date.toInstant().atZone(zoneId).toLocalTime();
    }

    /**
     * 是否为 LocalDate 时间
     */
    public static boolean isLocalDate(Object obj) {
        return obj instanceof LocalDate || obj instanceof LocalDateTime;
    }

    /**
     * 获取当天最小时间
     */
    public static LocalDateTime withFirstTime(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof LocalDate) {
            LocalDate date = (LocalDate) obj;
            return LocalDateTime.of(date, LocalTime.MIN);
        }
        if (obj instanceof LocalDateTime) {
            LocalDateTime dateTime = (LocalDateTime) obj;
            return LocalDateTime.of(dateTime.toLocalDate(), LocalTime.MIN);
        }
        throw new IllegalArgumentException("Argument type error: " + obj.getClass().getName());
    }

    /**
     * 获取当天最大
     */
    public static LocalDateTime withLastTime(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof LocalDate) {
            LocalDate date = (LocalDate) obj;
            return LocalDateTime.of(date, LocalTime.MAX);
        }
        if (obj instanceof LocalDateTime) {
            LocalDateTime dateTime = (LocalDateTime) obj;
            return LocalDateTime.of(dateTime.toLocalDate(), LocalTime.MAX);
        }
        throw new IllegalArgumentException("Argument type error: " + obj.getClass().getName());
    }

    /**
     * <p>
     * 获取停留时间
     * </p>
     *
     * @param before 之前时间
     * @param now    现在时间
     * @return 停留时间
     */
    public static String getResidenceTime(LocalDateTime before, LocalDateTime now) {
        Duration duration = Duration.between(before, now);
        long days = duration.toDays();
        duration = duration.plusDays(-days);
        long hours = duration.toHours();
        long minutes = duration.plusHours(-hours).toMinutes();
        if (days > 0) {
            return days + "天" + hours + "小时" + minutes + "分钟";
        }
        if (hours > 0) {
            return hours + "小时" + minutes + "分钟";
        }
        return (minutes == 0 ? 1 : minutes) + "分钟";
    }

    /**
     * 获取当前日期字符串
     */
    public static String getNowDateString() {
        return format(LocalDate.now(), DatePattern.yyyyMMdd);
    }

    /**
     * 获取当前时间字符串
     */
    public static String getNowTimeString() {
        return format(LocalDateTime.now(), DatePattern.yyyyMMddHHmmss);
    }

    /**
     * 获取昨天日期
     */
    public static LocalDate getYesterday() {
        return LocalDate.now().minusDays(1);
    }

    /**
     * 获取昨天日期
     */
    public static LocalDate getYesterday(LocalDate date) {
        return date.minusDays(1);
    }

    /**
     * 获取昨天时间
     */
    public static LocalDateTime getYesterdayTime() {
        return LocalDateTime.now().minusDays(1);
    }

    /**
     * 获取昨天时间
     */
    public static LocalDateTime getYesterdayTime(LocalDateTime dateTime) {
        return dateTime.minusDays(1);
    }


    /**
     * <p>
     * 获取相差天数
     * </p>
     *
     * @param before 之前时间
     * @param now    现在时间
     */
    public static long getBetweenDays(LocalDateTime before, LocalDateTime now) {
        Duration duration = Duration.between(before, now);
        return duration.toDays();
    }

    /**
     * <p>
     * 获取相差小时
     * </p>
     *
     * @param before 之前时间
     * @param now    现在时间
     */
    public static long getBetweenHours(LocalDateTime before, LocalDateTime now) {
        Duration duration = Duration.between(before, now);
        return duration.toHours();
    }

    /**
     * <p>
     * 获取相差分钟
     * </p>
     *
     * @param before 之前时间
     * @param now    现在时间
     */
    public static long getBetweenMinutes(LocalDateTime before, LocalDateTime now) {
        Duration duration = Duration.between(before, now);
        return duration.toMinutes();
    }

    /**
     * <p>
     * 获取相差秒
     * </p>
     *
     * @param before 之前时间
     * @param now    现在时间
     */
    public static long getBetweenSeconds(LocalDateTime before, LocalDateTime now) {
        Duration duration = Duration.between(before, now);
        return duration.getSeconds();
    }

    /**
     * <p>
     * 获取相差毫秒
     * </p>
     *
     * @param before 之前时间
     * @param now    现在时间
     */
    public static long getBetweenMillis(LocalDateTime before, LocalDateTime now) {
        Duration duration = Duration.between(before, now);
        return duration.toMillis();
    }

    /**
     * <p>
     * 判断在日期区间内
     * </p>
     *
     * @param date      日期
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 是否在日期区间内
     */
    public static boolean between(LocalDate date, LocalDate startDate, LocalDate endDate) {
        if (date == null || startDate == null || endDate == null) {
            return false;
        }
        return (date.isEqual(startDate) || date.isAfter(startDate))
                && (date.isEqual(endDate) || date.isBefore(endDate));
    }

    /**
     * <p>
     * 判断在时间区间内
     * </p>
     *
     * @param dateTime  时间
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 是否在日期区间内
     */
    public static boolean between(LocalDateTime dateTime, LocalDateTime startTime, LocalDateTime endTime) {
        if (dateTime == null || startTime == null || endTime == null) {
            return Boolean.FALSE;
        }
        return dateTime.isAfter(startTime) && dateTime.isBefore(endTime);
    }

    /**
     * 获取当天开始时间，例如：2022-09-20 00:00:00
     */
    public static LocalDateTime getFirstTimeOfDay() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
    }

    /**
     * 获取当天开始时间，例如：2022-09-20 00:00:00
     */
    public static LocalDateTime getFirstTimeOfDay(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.MIN);
    }

    /**
     * 获取当天开始时间，例如：2022-09-20 00:00:00
     */
    public static LocalDateTime getFirstTimeOfDay(LocalDateTime dateTime) {
        return LocalDateTime.of(dateTime.toLocalDate(), LocalTime.MIN);
    }

    /**
     * 获取当天结束时间，例如：2022-09-20 23:59:59
     */
    public static LocalDateTime getLastTimeOfDay() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    }

    /**
     * 获取当天结束时间，例如：2022-09-20 23:59:59
     */
    public static LocalDateTime getLastTimeOfDay(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.MAX);
    }

    /**
     * 获取当天结束时间，例如：2022-09-20 23:59:59
     */
    public static LocalDateTime getLastTimeOfDay(LocalDateTime dateTime) {
        return LocalDateTime.of(dateTime.toLocalDate(), LocalTime.MAX);
    }

    /**
     * 获取当前日期所在周几的日期
     *
     * @param weekNum 周几
     */
    public static LocalDate getDateOfWeek(int weekNum) {
        return getDateOfWeek(LocalDate.now(), weekNum);
    }

    /**
     * 获取日期所在周几的日期
     *
     * @param weekNum 周几
     */
    public static LocalDate getDateOfWeek(LocalDate date, int weekNum) {
        if (weekNum < 0 || weekNum > 7) {
            throw new IllegalArgumentException("weekNum must be between 1 and 7");
        }

        int current = date.getDayOfWeek().getValue();
        int diff = weekNum - current;
        return date.plusDays(diff);
    }

    /**
     * 获取当前日期所在周几的日期
     *
     * @param weekNum 周几
     */
    public static LocalDateTime getDateTimeOfWeek(int weekNum) {
        return getDateTimeOfWeek(LocalDateTime.now(), weekNum);
    }

    /**
     * 获取日期所在周几的日期
     *
     * @param weekNum 周几
     */
    public static LocalDateTime getDateTimeOfWeek(LocalDateTime dateTime, int weekNum) {
        if (weekNum < 0 || weekNum > 7) {
            throw new IllegalArgumentException("weekNum must be between 1 and 7");
        }

        int current = dateTime.getDayOfWeek().getValue();
        int diff = weekNum - current;
        return dateTime.plusDays(diff);
    }

    /**
     * 获取所在月最后一天的时间
     * 例如：2021-06-30
     */
    public static LocalDate getLastDayOfMonth() {
        LocalDate now = LocalDate.now();
        return now.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 获取所在月最后一天的时间
     * 例如：2021-06-30
     */
    public static LocalDate getLastDayOfMonth(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 获取所在月第一天的时间
     * 例如：2021-06-01
     */
    public static LocalDate getFirstDayOfMonth() {
        LocalDate now = LocalDate.now();
        return now.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取所在月第一天的时间
     * 例如：2021-06-01
     */
    public static LocalDate getFirstDayOfMonth(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取所在月最后一天的时间
     * 例如：2021-06-30 23:59.999
     */
    public static LocalDateTime getLastTimeOfMonth() {
        LocalDate lastDayOfMonth = getLastDayOfMonth();
        return LocalDateTime.of(lastDayOfMonth, LocalTime.MAX);
    }

    /**
     * 获取所在月最后一天的时间
     * 例如：2021-06-30 23:59.999
     */
    public static LocalDateTime getLastTimeOfMonth(LocalDate date) {
        if (date == null) {
            return null;
        }
        LocalDate lastDayOfMonth = getLastDayOfMonth(date);
        return LocalDateTime.of(lastDayOfMonth, LocalTime.MAX);
    }

    /**
     * 获取所在季度最后一天的时间
     * 例如：2021-06-30 23:59.999
     */
    public static LocalDateTime getLastTimeOfQuarter(LocalDate date) {
        if (date == null) {
            return null;
        }
        int monthValue = date.getMonthValue();

        int quarterLastMonth = monthValue;
        if (monthValue <= 3) {
            quarterLastMonth = 3;
        } else if (monthValue <= 6) {
            quarterLastMonth = 6;
        } else if (monthValue <= 9) {
            quarterLastMonth = 9;
        } else {
            quarterLastMonth = 12;
        }

        if (quarterLastMonth == monthValue) {
            return LocalDateUtil.getLastTimeOfMonth(date);
        }

        int betweenMonth = quarterLastMonth - monthValue;
        return LocalDateUtil.getLastTimeOfMonth(date.plusMonths(betweenMonth));
    }

    /**
     * 获取所在月第一天的时间
     * 例如：2021-06-01 00:00
     */
    public static LocalDateTime getFirstTimeOfMonth() {
        LocalDate firstDayOfMonth = getFirstDayOfMonth();
        return LocalDateTime.of(firstDayOfMonth, LocalTime.MIN);
    }

    /**
     * 获取所在月第一天的时间
     * 例如：2021-06-01 00:00
     */
    public static LocalDateTime getFirstTimeOfMonth(LocalDate date) {
        if (date == null) {
            return null;
        }
        LocalDate firstDayOfMonth = getFirstDayOfMonth(date);
        return LocalDateTime.of(firstDayOfMonth, LocalTime.MIN);
    }

    /**
     * 获取上个月最后一天的时间
     * 例如：2021-06-30 23:59.999
     */
    public static LocalDateTime getLastTimeOfPreMonth() {
        LocalDate now = LocalDate.now().minusMonths(1);
        return getLastTimeOfMonth(now);
    }

    /**
     * 获取上个月最后一天的时间
     * 例如：2021-06-30 23:59.999
     */
    public static LocalDateTime getLastTimeOfPreMonth(LocalDate date) {
        if (date == null) {
            return null;
        }
        date = date.minusMonths(1);
        return getLastTimeOfMonth(date);
    }

    /**
     * 获取上个月第一天的时间
     * 例如：2021-06-01 00:00
     */
    public static LocalDateTime getFirstTimeOfPreMonth() {
        LocalDate now = LocalDate.now().minusMonths(1);
        return getFirstTimeOfMonth(now);
    }

    /**
     * 获取上个月第一天的时间
     * 例如：2021-06-01 00:00
     */
    public static LocalDateTime getFirstTimeOfPreMonth(LocalDate date) {
        if (date == null) {
            return null;
        }
        date = date.minusMonths(1);
        return getFirstTimeOfMonth(date);
    }

    /**
     * 获取当前年第一天的日期
     *
     * @return
     */
    public static LocalDate getFirstDayOfYear() {
        LocalDate now = LocalDate.now();
        return now.with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * 获取所在年第一天的日期
     *
     * @return
     */
    public static LocalDate getFirstDayOfYear(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * 获取当前年最后一天的日期
     *
     * @return
     */
    public static LocalDate getLastDayOfYear() {
        LocalDate now = LocalDate.now();
        return now.with(TemporalAdjusters.lastDayOfYear());
    }

    /**
     * 获取所在年最后一天的日期
     *
     * @return
     */
    public static LocalDate getLastDayOfYear(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.with(TemporalAdjusters.lastDayOfYear());
    }

    /**
     * 获取当前年第一天的时间
     *
     * @return
     */
    public static LocalDateTime getFirstTimeOfYear() {
        return LocalDateTime.of(getFirstDayOfYear(), LocalTime.MIN);
    }

    /**
     * 获取所在年第一天的时间
     *
     * @return
     */
    public static LocalDateTime getFirstTimeOfYear(LocalDate date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.of(getFirstDayOfYear(date), LocalTime.MIN);
    }

    /**
     * 获取所在年最后一天的时间
     *
     * @return
     */
    public static LocalDateTime getLastTimeOfYear() {
        return LocalDateTime.of(getLastDayOfYear(), LocalTime.MAX);
    }

    /**
     * 获取所在年最后一天的时间
     *
     * @return
     */
    public static LocalDateTime getLastTimeOfYear(LocalDate date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.of(getLastDayOfYear(date), LocalTime.MAX);
    }

    /**
     * 获取下一年第一天的日期
     *
     * @return
     */
    public static LocalDate getFirstDateOfNextYear() {
        LocalDate now = LocalDate.now();
        now = now.plusYears(1);
        return now.with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * 获取下一年第一天的日期
     *
     * @return
     */
    public static LocalDate getFirstDateOfNextYear(LocalDate date) {
        if (date == null) {
            return null;
        }
        date = date.plusYears(1);
        return date.with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * 获取下一年第一天的时间
     *
     * @return
     */
    public static LocalDateTime getFirstTimeOfNextYear() {
        return LocalDateTime.of(getFirstDateOfNextYear(), LocalTime.MIN);
    }

    /**
     * 获取下一年第一天的时间
     *
     * @return
     */
    public static LocalDateTime getFirstTimeOfNextYear(LocalDate date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.of(getFirstDateOfNextYear(date), LocalTime.MIN);
    }

}
