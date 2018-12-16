package com.sol4.cloud.Service;

import java.util.List;

import com.sol4.cloud.Domain.Cloud;

public interface BoardService {

	List<Cloud> getNoticebyID(Cloud c) throws Exception;
	List<Cloud> getBoard (Cloud c) throws Exception;
	double usedCapacity(Cloud c);
	int uploadFile(Cloud c);
	int deleteFile(Cloud c);
	int checkFile(Cloud c);
	List<Cloud> listAll(int start, int end, Cloud c) throws Exception;
	void updateShare(Cloud c);
}
