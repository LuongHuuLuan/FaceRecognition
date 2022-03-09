package faceReconition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FaceManager {

	public static boolean insertFace(int id, String name) {
		Connection conn = Connect.getConnection();
		String sql = "insert into NAME(ID, Name) values(?,?)";
		try {
			PreparedStatement stament = conn.prepareStatement(sql);
			stament.setInt(1, id);
			stament.setString(2, name);
			int rows = stament.executeUpdate();
			if (rows == 1) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean deleteFace(int id) {
		Connection conn = Connect.getConnection();
		String sql = "delete from NAME where ID = ?";
		try {
			PreparedStatement stament = conn.prepareStatement(sql);
			stament.setInt(1, id);
			int rows = stament.executeUpdate();
			if (rows == 1) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String getFaceName(int id) {
		Connection conn = Connect.getConnection();
		String sql = "select Name from NAME where ID =?";
		try {
			PreparedStatement stament = conn.prepareStatement(sql);
			stament.setInt(1, id);
			ResultSet result = stament.executeQuery();
			if (result.next())
				return result.getString("Name");
			else
				return null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean updateName(int id, String name) {
		Connection conn = Connect.getConnection();
		String sql = "update NAME set Name = ? where ID =?";
		try {
			PreparedStatement stament = conn.prepareStatement(sql);
			stament.setString(1, name);
			stament.setInt(2, id);
			int rows = stament.executeUpdate();
			if (rows == 1) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void deleteAll() {
		Connection conn = Connect.getConnection();
		String sql = "delete from NAME";
		try {
			PreparedStatement stament = conn.prepareStatement(sql);
			stament.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
