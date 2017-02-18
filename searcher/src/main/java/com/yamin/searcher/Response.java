package com.yamin.searcher;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Yamin on 18-Feb-17.
 */
@IntDef({Response.RESULT_OK, Response.PARAM_ERROR, Response.SERVER_ERROR, Response.RESPONSE_ERROR})
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Response {
    int RESULT_OK = 1;
    int PARAM_ERROR = 2; // Empty twitter Key or Secret
    int SERVER_ERROR = 3; // Server error
    int RESPONSE_ERROR = 4; // Got response but without bearer
}