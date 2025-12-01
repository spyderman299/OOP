package main.java.repository;

import main.java.config.DatabaseConnection;
import main.java.model.Homeroom;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HomeroomRepository {
    private enum SQL {
        FIND_ALL("SELECT * FROM homerooms"),
        FIND_BY_ID("SELECT * FROM homerooms WHERE class_id = ?"),
        FIND_BY_SUBJECT_ID("SELECT * FROM homerooms WHERE subject_id = ?"),
        FIND_BY_TEACHER_ID("SELECT * FROM homerooms WHERE teacher_id = ?"),
        INSERT("INSERT INTO homerooms (class_id, subject_id, teacher_id, start_date, end_date) VALUES (?, ?, ?, ?, ?)"),
        UPDATE("UPDATE homerooms SET subject_id = ?, teacher_id = ?, start_date = ?, end_date = ? WHERE class_id = ?"),
        DELETE("DELETE FROM homerooms WHERE class_id = ?");

        final String query;

        SQL(String q) {
            this.query = q;
        }
    }

    private Homeroom map(ResultSet rs) throws SQLException {
        Homeroom homeroom = new Homeroom();
        homeroom.setClassId(rs.getString("class_id"));
        homeroom.setSubjectId(rs.getString("subject_id"));
        homeroom.setTeacherId(rs.getString("teacher_id"));
        homeroom.setStartDate(rs.getDate("start_date").toLocalDate());
        homeroom.setEndDate(rs.getDate("end_date").toLocalDate());
        return homeroom;
    }

    public List<Homeroom> findAll() {
        List<Homeroom> homerooms = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.FIND_ALL.query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                homerooms.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all homerooms: " + e.getMessage());
        }
        return homerooms;
    }

    public Homeroom findById(String classId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.FIND_BY_ID.query)) {
            stmt.setString(1, classId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding homeroom by id: " + e.getMessage());
        }
        return null;
    }

    public List<Homeroom> findBySubjectId(String subjectId) {
        List<Homeroom> homerooms = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.FIND_BY_SUBJECT_ID.query)) {
            stmt.setString(1, subjectId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    homerooms.add(map(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding homerooms by subject id: " + e.getMessage());
        }
        return homerooms;
    }

    public List<Homeroom> findByTeacherId(String teacherId) {
        List<Homeroom> homerooms = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.FIND_BY_TEACHER_ID.query)) {
            stmt.setString(1, teacherId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    homerooms.add(map(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding homerooms by teacher id: " + e.getMessage());
        }
        return homerooms;
    }

    public boolean create(Homeroom homeroom) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.INSERT.query)) {
            stmt.setString(1, homeroom.getClassId());
            stmt.setString(2, homeroom.getSubjectId());
            stmt.setString(3, homeroom.getTeacherId());
            stmt.setDate(4, Date.valueOf(homeroom.getStartDate()));
            stmt.setDate(5, Date.valueOf(homeroom.getEndDate()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating homeroom: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Homeroom homeroom) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.UPDATE.query)) {
            stmt.setString(1, homeroom.getSubjectId());
            stmt.setString(2, homeroom.getTeacherId());
            stmt.setDate(3, Date.valueOf(homeroom.getStartDate()));
            stmt.setDate(4, Date.valueOf(homeroom.getEndDate()));
            stmt.setString(5, homeroom.getClassId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating homeroom: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String classId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.DELETE.query)) {
            stmt.setString(1, classId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting homeroom: " + e.getMessage());
            return false;
        }
    }
}

