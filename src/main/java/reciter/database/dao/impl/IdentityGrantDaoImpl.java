package reciter.database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import reciter.database.DbConnectionFactory;
import reciter.database.DbUtil;
import reciter.database.dao.IdentityGrantDao;
import reciter.database.model.IdentityGrant;
import reciter.database.mongo.model.Grant;

@Repository("identityGrantDao")
public class IdentityGrantDaoImpl implements IdentityGrantDao {

	@Override
	public Map<String, List<reciter.database.mongo.model.Grant>> getAllIdentityGrant() {
		Connection con = DbConnectionFactory.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = "select cwid, grantid, sponsorAwardId, administeringDepartmentDivision, awardingOrganization "
				+ "from rc_identity_grant where cwid is not null and cwid not regexp '^[0-9]+'";
		
		Map<String, List<reciter.database.mongo.model.Grant>> map = new HashMap<String, List<reciter.database.mongo.model.Grant>>();
		try {
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				Grant g = new Grant();
				g.setGrantId(rs.getString(2));
				g.setSponsorAwardId(rs.getString(3));
				g.setDepartment(rs.getString(4));
				g.setOrganization(rs.getString(5));
				
				String cwid = rs.getString(1);
				if (map.containsKey(cwid)) {
					map.get(cwid).add(g);
				} else {
					List<reciter.database.mongo.model.Grant> l = new ArrayList<reciter.database.mongo.model.Grant>();
					l.add(g);
					map.put(cwid, l);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.close(rs);
			DbUtil.close(pst);
			DbUtil.close(con);
		}
		return map;
	}
	
	/**
	 * Returns a list of IdentityGrant objects by query the cwid in table rc_identity_grant.
	 * @param cwid cwid.
	 * @return List of IdentityGrants.
	 */
	@Override
	public List<IdentityGrant> getIdentityGrantListByCwid(String cwid) {
		Connection con = DbConnectionFactory.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = "SELECT id, cwid, date, grantid, sponsorAwardId, "
				+ "administeringDepartmentDivision, awardingOrganization FROM rc_identity_grant "
				+ "WHERE cwid='" + cwid + "'";
		List<IdentityGrant> list = new ArrayList<IdentityGrant>();
		try {
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				IdentityGrant identityGrant = new IdentityGrant();
				identityGrant.setId(rs.getLong(1));
				identityGrant.setCwid(rs.getString(2));
				identityGrant.setDate(rs.getString(3));
				identityGrant.setGrandid(rs.getString(4));
				identityGrant.setSponsorAwardId(rs.getString(5));
				identityGrant.setAdministeringDepartmentDivision(rs.getString(6));
				identityGrant.setAwardingOrganization(rs.getString(7));
				list.add(identityGrant);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.close(rs);
			DbUtil.close(pst);
			DbUtil.close(con);
		}
		return list;
	}

	@Override
	public List<String> getSponsorAwardIdListByCwid(String cwid) {
		Connection con = DbConnectionFactory.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = "SELECT sponsorAwardId FROM rc_identity_grant WHERE cwid='" + cwid + "'";
		List<String> list = new ArrayList<String>();
		try {
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				String sponsorAwardId = rs.getString(1);
				if (sponsorAwardId != null) {
					list.add(sponsorAwardId);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtil.close(rs);
			DbUtil.close(pst);
			DbUtil.close(con);
		}
		return list;
	}
}
