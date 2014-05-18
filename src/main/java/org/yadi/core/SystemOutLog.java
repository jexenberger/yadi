package org.yadi.core;

import java.util.Date;

/**
 * Created by julian3 on 2014/05/12.
 */
public class SystemOutLog implements Log{
    @Override
    public void debug(String message) {
        System.out.println("DEBUG|"+new Date().toString()+"|"+message);
    }
}
