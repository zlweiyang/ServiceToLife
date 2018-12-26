package com.zl.o2o.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.zl.o2o.BaseTest;
import com.zl.o2o.entity.ShopCategory;
import com.zl.o2o.service.ShopCategoryService;

public class ShopCategoryServiceTest extends BaseTest {

	@Autowired
	private ShopCategoryService shopCategoryService;

	@Test
	public void testGetShopCategoryList() {
		List<ShopCategory> shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
		assertEquals(2, shopCategoryList.size());
	}
}