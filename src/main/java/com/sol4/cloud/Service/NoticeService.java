package com.sol4.cloud.Service;

import java.util.List;

import com.sol4.cloud.Domain.Cloud;

public interface NoticeService {
	
	void writeNotice (Cloud c);
	void deleteNotice (Cloud c);
	void updateNotice (Cloud c);
	int checkFile (Cloud c);
	int checkNotice();
	List<Cloud> getShareFile (Cloud c) throws Exception;
	List<Cloud> getNotice (Cloud c) throws Exception;
	List<Cloud> listAllNotice (int start, int end, String searchOption, String keyword) throws Exception;
	void updateViewCnt(Cloud c) throws Exception;
	
}
