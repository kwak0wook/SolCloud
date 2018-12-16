package com.sol4.cloud.Service;

import java.util.List;

import com.sol4.cloud.Domain.Cloud;

public interface MemberService {

	int login (Cloud m);
	int regMember (Cloud m);
	Cloud selectMember (String m_id);
	void deleteMember (Cloud c);
	void dropMemberTable (Cloud c);
	void updateMemberInfo(Cloud c);
	int checkInfo (Cloud c);
	void updatePW (Cloud c);
	int checkMember ();
	public List<Cloud> listAllMembers (int start, int end) throws Exception;
	
}
