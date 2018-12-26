package com.zl.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zl.o2o.entity.ShopCategory;

public interface ShopCategoryDao {

	//传入参数，查询店铺分类列表
	List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition") ShopCategory shopCategoryCondition);

}