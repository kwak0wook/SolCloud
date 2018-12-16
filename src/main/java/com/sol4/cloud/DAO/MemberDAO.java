package com.sol4.cloud.DAO;

import java.util.List;
import java.util.Map;

import com.sol4.cloud.Domain.Cloud;

public interface MemberDAO {
	
	int insertMember (Cloud m);
	Cloud selectMemberById (String m_id);
	void createCloud_Id (Cloud m);
	void deleteMember (Cloud c);
	void dropMemberTable (Cloud c);
	void updateMemberInfo(Cloud c);
	int checkInfo (Cloud c);
	void updatePW (Cloud c);
	int checkMember ();
	public List<Cloud> listAllMembers (Map<String, Object> map) throws Exception;
}
