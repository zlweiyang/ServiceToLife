package com.zl.o2o.exceptions;

/**
 * 自定义店铺操作异常类
 * 
 *
 */
public class ShopOperationException extends RuntimeException {

	private static final long serialVersionUID = 7923277044845362315L;

	public ShopOperationException(String msg) {
		super(msg);
	}

}
