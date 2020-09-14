package cn.easy4j.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * @author ChenYichen
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalDateUtil {

    public static final String START_TIME = " 00:00:00";

    public static final String END_TIME = " 23:59:59";

    public static final String YYYY = "yyyy";

    public static final String YYYY_MM = "yyyy-MM";

    public static final String HH_MM_SS = "HH:mm:ss";

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static final String[] PARSE_PATTERNS = {
            YYYY_MM_DD, YYYY_MM_DD_HH_MM_SS, "yyyy-MM-dd HH:mm", YYYY_MM,
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 根据生日计算年龄（生日是LocalDate类型）
     *
     * @param birthday 生日
     * @return 年龄
     */
    public static int calculateAge(LocalDate birthday) {
        return (int) ChronoUnit.YEARS.between(birthday, LocalDate.now());
    }
}
