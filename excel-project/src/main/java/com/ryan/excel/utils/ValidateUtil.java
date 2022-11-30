/*
 * Personal Learning Use
 */
package com.ryan.excel.utils;

import org.apache.commons.compress.utils.Sets;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * <P><B>Description: </B>校验工具</P>
 * Revision Trail: (Date/Author/Description)
 * 2022/11/30 Ryan Huang CREATE
 *
 * @author Ryan Huang
 * @version 1.0
 */
public class ValidateUtil {


    /**
     * 校验对象
     * @param obj 需要校验的对象
     * @return 错误信息
     */
    public static Set<String> validator(Object obj){
        Set<String> errorMessage = Sets.newHashSet();
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        setConstraintViolationMessage(errorMessage, validator, obj);
        return errorMessage;
    }

    /**
     * 校验对象
     * @param obj 需要校验的对象
     * @param failFast 是否遇到一个失败就停止检查 true：是；false：否
     * @return 错误信息
     */
    public static Set<String> validator(Object obj, boolean failFast){
        Set<String> errorMessage = Sets.newHashSet();
        Validator validator = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(failFast)
                .buildValidatorFactory()
                .getValidator();
        setConstraintViolationMessage(errorMessage, validator, obj);
        return errorMessage;
    }

    /**
     * 存储校验对象的错误信息
     * @param errorMessage 错误信息
     * @param validator 校验器
     * @param obj 需要校验的对象
     */
    public static void setConstraintViolationMessage(Set<String> errorMessage, Validator validator, Object obj){
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj);
        if(constraintViolations != null && !constraintViolations.isEmpty()){
            for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
                errorMessage.add(constraintViolation.getMessage());
            }
        }
    }


}
