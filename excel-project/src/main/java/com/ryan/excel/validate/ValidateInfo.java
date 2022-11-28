/*
* Personal Learning Use
 */
package com.ryan.excel.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * <P><B>Description: </B>TODO 添加描述</P>
 * Revision Trail: (Date/Author/Description)
 * 2022/8/26 Ryan Huang CREATE
 *
 * @author Ryan Huang
 * @version 1.0
 */
public class ValidateInfo implements ConstraintValidator<Dict, Object> {

    /**
     * ConstraintValidator定义了两个泛型参数,
     * 第一个是这个校验器所服务到标注类型(在我们的例子中即Dict),
     * 第二个这个校验器所支持到被校验元素到类型 (即Object).
     *
     */

    @Override
    public void initialize(Dict constraintAnnotation) {


    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
