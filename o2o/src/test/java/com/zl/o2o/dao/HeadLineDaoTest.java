package com.zl.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.zl.o2o.BaseTest;
import com.zl.o2o.dao.HeadLineDao;
import com.zl.o2o.entity.HeadLine;

public class HeadLineDaoTest extends BaseTest {

	@Autowired
	private HeadLineDao headLineDao;

	@Test
	public void testQueryHeadLine() {
		List<HeadLine> result = headLineDao.queryHeadLine(new HeadLine());
		assertEquals(4, result.size());
	}

}
