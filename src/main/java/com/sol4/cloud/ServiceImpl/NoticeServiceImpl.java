package com.sol4.cloud.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sol4.cloud.DAO.NoticeDAO;
import com.sol4.cloud.Domain.Cloud;
import com.sol4.cloud.Service.NoticeService;

@Service
public class NoticeServiceImpl implements NoticeService {

	@Autowired
	private NoticeDAO nDAO;

	@Override
	public void writeNotice(Cloud c) {
		nDAO.writeNotice(c);
	}

	@Override
	public void deleteNotice(Cloud c) {
		nDAO.deleteNotice(c);
	}

	@Override
	public void updateNotice(Cloud c) {
		nDAO.updateNotice(c);
	}

	@Override
	public int checkFile(Cloud c) {
		return nDAO.checkFile(c);
	}

	@Override
	public List<Cloud> getNotice(Cloud c) throws Exception {
		return nDAO.getNotice(c);
	}

	@Override
	public List<Cloud> listAllNotice(int start, int end, String searchOption, String keyword) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("searchOption", searchOption);
	    map.put("keyword", keyword);
	    map.put("start", start);
	    map.put("end", end);
		return nDAO.listAllNotice (map);
	}

	@Override
	public int checkNotice() {
		return nDAO.checkNotice();
	}

	@Override
	public List<Cloud> getShareFile(Cloud c) throws Exception {
		return nDAO.getShareFile(c);
	}

	@Override
	public void updateViewCnt(Cloud c) throws Exception {
		nDAO.updateViewCnt(c);
	}
}