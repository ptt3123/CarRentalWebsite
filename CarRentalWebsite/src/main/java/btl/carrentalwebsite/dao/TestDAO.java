/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package btl.carrentalwebsite.dao;

/**
 *
 * @author Vhc
 */
public class TestDAO {
    public static void main(String[] args) {
        DAO dao = new DAO();
        System.out.println("Test kết nối DB...");
        dao.closeConnection();
    }
}
