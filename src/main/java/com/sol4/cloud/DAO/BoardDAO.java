package com.sol4.cloud.DAO;

import java.util.List;
import java.util.Map;

import com.sol4.cloud.Domain.Cloud;

public interface BoardDAO {
	
	List<Cloud> getBoard (Cloud c) throws Exception;
	List<Cloud> getNoticebyID (Cloud c) throws Exception;
	double usedCapacity(Cloud c);
	int uploadFile(Cloud c);
	int deleteFile(Cloud c);
	int checkFile(Cloud c);
	List<Cloud> listAll(Map<String, Object> map) throws Exception;
	void updateShare (Cloud c);
}
