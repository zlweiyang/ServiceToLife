package com.zl.o2o.util;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {

	/**
	 * 判断验证码是否输入正确
	 * 
	 * @param request
	 * @return
	 */
	public static boolean checkValidateCode(HttpServletRequest request) {
		//获取图片中的验证码
		String validateCodeExpected = (String) request.getSession()
				.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		//输入验证码
		String validateCode = HttpServletRequestUtil.getString(request, "validateCode");
		if (validateCode == null || !validateCode.equalsIgnoreCase(validateCodeExpected)) {
			return false;
		}
		return true;
	}

}
