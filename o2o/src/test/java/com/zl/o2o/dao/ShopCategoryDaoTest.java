package com.zl.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.zl.o2o.BaseTest;
import com.zl.o2o.dao.ShopCategoryDao;
import com.zl.o2o.entity.ShopCategory;

public class ShopCategoryDaoTest extends BaseTest {

	@Autowired
	private ShopCategoryDao shopCategoryDao;

	@Test
	public void testQueryShopCategory() {
		//先传一个空的对象
		List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(new ShopCategory());
		assertEquals(2, shopCategoryList.size());
		ShopCategory testShopCategory = new ShopCategory();
		ShopCategory parentShopCategory = new ShopCategory();
		parentShopCategory.setShopCategoryId(1l);
		testShopCategory.setParent(parentShopCategory);
		shopCategoryList = shopCategoryDao.queryShopCategory(testShopCategory);
		assertEquals(1, shopCategoryList.size());
		System.out.println(shopCategoryList.get(0).getShopCategoryName());
	}

	@Test
	public void testQueryShopCategoryNull() {
		List<ShopCategory> result = shopCategoryDao.queryShopCategory(null);
		assertEquals(3, result.size());
	}
}
