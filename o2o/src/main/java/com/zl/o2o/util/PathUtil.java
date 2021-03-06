package com.zl.o2o.util;

public class PathUtil {
	
	private static String seperator = System.getProperty("flie.separator");
	
	public static String getImgBasePath() {
		
		String os = System.getProperty("os.name");
		String basePath = "";
		
		if(os.toLowerCase().startsWith("win")) {
			basePath = "F:\\ServiceToLife\\image";
		}else {
			basePath = "/home/calma/image";
		}
		
		basePath = basePath.replace("/", seperator);
		
		return basePath;
	}
	
	public static String getShopImagePath(long shopId) {
		String imagePath = "upload/item/shop/" + shopId + "/";
		return imagePath.replace("/", seperator);
	}

}
