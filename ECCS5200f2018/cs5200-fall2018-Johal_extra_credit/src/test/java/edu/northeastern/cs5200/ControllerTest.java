package edu.northeastern.cs5200;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ControllerTest {
    @Test
    public void aaDeleteActorMovie() throws SQLException, ClassNotFoundException {
        ControllerClass con = new ControllerClass();
        con.deleteTable("actor");
        con.deleteTable("movie");
    }
    @Test
    public void aitReturnsNullOnNoTable() throws SQLException, ClassNotFoundException {
    ControllerClass con = new ControllerClass();
    assertEquals(con.getRecords("actor"),null);
    }

    @Test
    public void bitReturnsJsonOnNewTableCreate() throws SQLException, ClassNotFoundException {
        ControllerClass con = new ControllerClass();
        String json = "{\"name\":\"Harrison\"}";
       // String res = "{\"id\":\"1\",\"name\":\"Harrison\"}";
        assertEquals(con.createTable("actor",json),json);
    }
    @Test
    public void citReturnsJsonOnNewTableCreate2() throws SQLException, ClassNotFoundException {
        ControllerClass con = new ControllerClass();
        String json = "{\"name\":\"Joe\"}";
        // String res = "{\"id\":\"1\",\"name\":\"Harrison\"}";
        assertEquals(con.createTable("actor",json),json);
    }
    @Test
    public void ditAltersTableSchema() throws SQLException, ClassNotFoundException {
        ControllerClass con = new ControllerClass();
        String json = "{\"last\":\"Goslyn\",\"first\":\"Ryan\"}";
         String res = "{\"name\":\"null\",\"last\":\"Goslyn\",\"first\":\"Ryan\"}";
        assertEquals(con.createTable("actor",json),res);
    }
    @Test
    public void eitReturnsAllActors() throws SQLException, ClassNotFoundException {
        ControllerClass con = new ControllerClass();
        Set<String> set = new HashSet<>();
       // set.add()
        String res = "[{\"last\":\"null\",\"name\":\"Harrison\",\"first\":\"null\"},{\"last\":\"null\",\"name\":\"Joe\",\"first\":\"null\"},{\"last\":\"Goslyn\",\"name\":\"null\",\"first\":\"Ryan\"}]";
        assertEquals(con.getRecords("actor"),res);
    }
    @Test
    public void fitReturnsNullAsTableDoesNotExit() throws SQLException, ClassNotFoundException {
        ControllerClass con = new ControllerClass();
        String json = "{\"name\":\"Joseph\"}";
        assertEquals(con.updateRecordsbyid("movie","432",json),null);
    }
    @Test
    public void gitReturnsNullAsRecordDoesNotExit() throws SQLException, ClassNotFoundException {
        ControllerClass con = new ControllerClass();
        String json = "{\"name\":\"Joseph\"}";
        assertEquals(con.updateRecordsbyid("actor","432",json),null);
    }
    @Test
    public void hitupdatesRecord() throws SQLException, ClassNotFoundException {
        ControllerClass con = new ControllerClass();
        String json = "{\"name\":\"Joseph\"}";
        String res = "{\"name\":\"Joseph\",\"last\":\"null\",\"first\":\"null\"}";
        assertEquals(con.updateRecordsbyid("actor","2",json),res);
    }
    @Test
    public void iitupdatesSchemaandRecord() throws SQLException, ClassNotFoundException {
        ControllerClass con = new ControllerClass();
        String json = "{\"nombre\":\"Joseph\"}";
        String res = "{\"name\":\"Joseph\",\"last\":\"null\",\"first\":\"null\",\"nombre\":\"Joseph\"}";
        assertEquals(con.updateRecordsbyid("actor","2",json),res);
    }
    @Test
    public void jitreturnsNullonDeleteRecordDoesnotExist() throws SQLException, ClassNotFoundException {
        ControllerClass con = new ControllerClass();
        assertEquals(con.deleteRecordsByid("actor","111"),null);
    }
    @Test
    public void kitreturnsNullonDeleteTableDoesnotExist() throws SQLException, ClassNotFoundException {
        ControllerClass con = new ControllerClass();
        assertEquals(con.deleteRecordsByid("movie","111"),null);
    }
    @Test
    public void litDeletesRecors() throws SQLException, ClassNotFoundException {
        ControllerClass con = new ControllerClass();
        assertEquals(con.deleteRecordsByid("actor","2"),new Integer(1));
    }
    @Test
    public void mitReturnsJsonOnNewTableCreate() throws SQLException, ClassNotFoundException {
        ControllerClass con = new ControllerClass();
        String json = "{\"title\":\"Blade Runner\"}";
         String res = "{\"id\":\"1\",\"title\":\"Blade Runner\"}";
        assertEquals(con.createTable("movie",json),json);
    }
    @Test
    public void nitReturnsJsonOnNewTableCreate2() throws SQLException, ClassNotFoundException {
        ControllerClass con = new ControllerClass();
        String json = "{\"title\":\"La La Land\"}";
        assertEquals(con.createTable("movie",json),json);
    }
}
