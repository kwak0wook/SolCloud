package com.sol4.cloud.DAO;

import java.util.List;
import java.util.Map;

import com.sol4.cloud.Domain.Cloud;

public interface NoticeDAO {
	
	void writeNotice (Cloud c);
	void deleteNotice (Cloud c);
	void updateNotice (Cloud c);
	int checkFile (Cloud c);
	int checkNotice ();
	List<Cloud> getShareFile (Cloud c) throws Exception;
	List<Cloud> getNotice (Cloud c) throws Exception;
	List<Cloud> listAllNotice (Map<String, Object> map) throws Exception;
	void updateViewCnt (Cloud c) throws Exception;

	
}
