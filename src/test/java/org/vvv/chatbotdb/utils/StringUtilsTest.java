package org.vvv.chatbotdb.utils;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class StringUtilsTest {

    private static Log log = LogFactory.getLog(StringUtilsTest.class);
    
    @Test
    public void testSplit() {
        StringUtils su = new StringUtils();
        String s1 = "word1 word2 word3, word4! word5, word6 word7. word 8?";
        List<String> res = su.split(s1);
        log.info("Split:" + res);
        assertEquals(res.size(), 14);
    }

}
