package com.sol4.cloud.DAOImpl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sol4.cloud.DAO.NoticeDAO;
import com.sol4.cloud.Domain.Cloud;

@Repository
public class NoticeDAOImpl implements NoticeDAO {

	@Autowired
	private SqlSession ss;
	
	@Override
	public void writeNotice(Cloud c) {
		ss.getMapper(NoticeDAO.class).writeNotice(c);
	}

	@Override
	public void deleteNotice(Cloud c) {
		ss.getMapper(NoticeDAO.class).deleteNotice(c);
	}

	@Override
	public void updateNotice(Cloud c) {
		ss.getMapper(NoticeDAO.class).updateNotice(c);
	}

	@Override
	public int checkFile(Cloud c) {
		return ss.getMapper(NoticeDAO.class).checkFile(c);
	}

	@Override
	public List<Cloud> getNotice(Cloud c) throws Exception {
		return ss.selectList("getNotice", c);
	}

	@Override
	public List<Cloud> listAllNotice (Map<String, Object> map) throws Exception {
		return ss.selectList("listAllNotice", map);
	}

	@Override
	public int checkNotice() {
		return ss.getMapper(NoticeDAO.class).checkNotice();
	}

	@Override
	public List<Cloud> getShareFile (Cloud c) throws Exception {
		return ss.selectList("getShareFile", c);
	}

	@Override
	public void updateViewCnt(Cloud c) throws Exception {
		ss.getMapper(NoticeDAO.class).updateViewCnt(c);
	}
}
