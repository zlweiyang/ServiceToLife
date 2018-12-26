package com.zl.o2o.service.impl;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.zl.o2o.dao.ShopDao;
import com.zl.o2o.dto.ImageHolder;
import com.zl.o2o.dto.ShopExecution;
import com.zl.o2o.entity.Shop;
import com.zl.o2o.entity.ShopCategory;
import com.zl.o2o.enums.ShopStateEnum;
import com.zl.o2o.exceptions.ShopOperationException;
import com.zl.o2o.service.ShopService;
import com.zl.o2o.util.ImageUtil;
import com.zl.o2o.util.PageCalculator;
import com.zl.o2o.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService {
	@Autowired
	private ShopDao shopDao;

	@Override
	@Transactional
	/**
	 * 使用注解控制事务方法的优点： 1.开发团队达成一致约定，明确标注事务方法的编程风格
	 * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作，RPC/HTTP请求或者剥离到事务方法外部
	 * 3.不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制
	 */
	public ShopExecution addShop(Shop shop,ImageHolder thumbnail)
			throws RuntimeException {
		//空值判断
		if (shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		try {
			//给店铺信息赋初始值
			shop.setEnableStatus(0);//店铺初始状态审核中
			shop.setCreateTime(new Date());//创建时间
			shop.setLastEditTime(new Date());//修改时间
			
			//添加店铺信息
			int effectedNum = shopDao.insertShop(shop);
			if (effectedNum <= 0) {
				throw new ShopOperationException("店铺创建失败");
			} else {
					if (thumbnail.getImage() != null) {
						//存储图片
						try {
							addShopImg(shop, thumbnail);
						}catch(Exception e) {
							throw new ShopOperationException("addShopImg error: "
									+ e.getMessage());
						}

						if (effectedNum <= 0) {	
							//只有使用Runtime异常，事物才有可能终止，进而回滚
							throw new ShopOperationException("更新图片地址失败");
						}
					}
			}
		} catch (Exception e) {
			throw new ShopOperationException("addShop error: " + e.getMessage());
		}
		
		return new ShopExecution(ShopStateEnum.CHECK,shop);
	}

	

	private void addShopImg(Shop shop,ImageHolder thumbnail) {
		//获取Shop图片目录的相对路径
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		shop.setShopImg(shopImgAddr);
	}



	@Override
	public Shop getByShopId(long shopId) {
		// TODO Auto-generated method stub
		return shopDao.queryByShopId(shopId);
	}


	@Override
	public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail)
			throws ShopOperationException {
		if (shop == null || shop.getShopId() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		} else {
			try {
				// 1. 判断是否要处理修改图片
				if (thumbnail.getImage() != null && thumbnail.getImageName() != null && !"".equals(thumbnail.getImageName())) {
					// 先删除原来的图片
					Shop tempShop = shopDao.queryByShopId(shop.getShopId());
					if (tempShop.getShopImg() != null) {
						ImageUtil.deleteFileOrPath(tempShop.getShopImg());
					}
					addShopImg(shop, thumbnail);
				}
				// 2. 更新店铺信息
				shop.setLastEditTime(new Date());//最新更改时间
				int effectedNum = shopDao.updateShop(shop);
				if (effectedNum <= 0) {
					return new ShopExecution(ShopStateEnum.INNER_ERROR);
				} else {
					//操作成功
					shop = shopDao.queryByShopId(shop.getShopId());
					
					//返回成功的信息
					return new ShopExecution(ShopStateEnum.SUCCESS, shop);
				}
			} catch (Exception e) {
				//发生错误是，catch
				throw new ShopOperationException("modifyShop error:" + e.getMessage());
			}
		}
	}
	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		//pageIndex转换
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		int count = shopDao.queryShopCount(shopCondition);
		ShopExecution se = new ShopExecution();
		if (shopList != null) {
			se.setShopList(shopList);
			//页数
			se.setCount(count);
		} else {
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return se;
	}

}
