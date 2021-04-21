package com.badlogic.drop;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class QueryRepository {
    static Connection verbinding = Database.maakVerbinding();

    public static void insertName(String username) {
        try {
            PreparedStatement myStmt = verbinding.prepareStatement("insert into namen (gebruikersnaam) Select ? Where not exists(select * from namen where gebruikersnaam=?)");
            myStmt.setString(1, username);
            myStmt.setString(2, username);
            Database.query(myStmt);
        } catch (Exception e) {
            System.out.println("Fout met opslaan naam");
        }
    }


    public static ArrayList<ArrayList<String>> selectidfromname(String username) {
        try {
            PreparedStatement myStmt = verbinding.prepareStatement("SELECT id FROM namen WHERE gebruikersnaam=?");
            myStmt.setString(1,username);

    public static ArrayList<ArrayList<String>> getTopTen() {
        try {
            PreparedStatement myStmt = verbinding.prepareStatement("SELECT score, playerID FROM highscore ORDER BY SCORE DESC LIMIT 10");

            ArrayList<ArrayList<String>> results = Database.query(myStmt);
            return results;
        } catch (Exception e) {
            System.out.println("Fout met ophalen van leaderboard");
            return null;
        }
    }


    public static void inserthighscore(int punten,String playerid){
        try {
            PreparedStatement myStmt = verbinding.prepareStatement("INSERT INTO highscore VALUES (?,?)");
            myStmt.setInt(1, punten);
            myStmt.setInt(2, Integer.parseInt(playerid));
            Database.query(myStmt);
        } catch (Exception e) {
            System.out.println("Fout met opslaan naam");

    public static ArrayList<ArrayList<String>> getUsername(int id) {
        try {
            PreparedStatement myStmt = verbinding.prepareStatement("SELECT gebruikersnaam FROM namen WHERE id=?");
            myStmt.setInt(1, id);
            ArrayList<ArrayList<String>> results = Database.query(myStmt);
            return results;
        } catch (Exception e) {
            System.out.println("Fout met ophalen van leaderboard");
            return null;

        }
    }
}
