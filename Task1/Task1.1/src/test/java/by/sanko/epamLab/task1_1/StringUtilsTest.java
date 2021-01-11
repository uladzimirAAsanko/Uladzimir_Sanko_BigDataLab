package by.sanko.epamLab.task1_1;


import org.junit.Test;
import utils.StringUtils;

import static org.junit.Assert.assertEquals;

public class StringUtilsTest {
    @Test
    public void testIsPositiveNumberPositive(){
        boolean actual = StringUtils.isPositiveNumber("2.1");
        boolean expected = true;
        assertEquals(expected,actual);
    }

    @Test
    public void testIsPositiveNumberPositiveInt(){
        boolean actual = StringUtils.isPositiveNumber("2");
        boolean expected = true;
        assertEquals(expected,actual);
    }

    @Test
    public void testIsPositiveNumberNegative(){
        boolean actual = StringUtils.isPositiveNumber("-2.1");
        boolean expected = false;
        assertEquals(expected,actual);
    }

    @Test
    public void testIsPositiveNumberNotNumber(){
        boolean actual = StringUtils.isPositiveNumber("-2.das1");
        boolean expected = false;
        assertEquals(expected,actual);
    }

    @Test
    public void testIsPositiveNumberZero(){
        boolean actual = StringUtils.isPositiveNumber("0");
        boolean expected = false;
        assertEquals(expected,actual);
    }
}
