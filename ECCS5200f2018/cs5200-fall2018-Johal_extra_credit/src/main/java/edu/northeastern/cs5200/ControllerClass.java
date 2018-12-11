package edu.northeastern.cs5200;

import java.sql.SQLException;
import java.util.Iterator;

import com.google.gson.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import sun.java2d.pipe.LoopBasedPipe;

@RestController
@CrossOrigin(origins = "*")

public class ControllerClass {

	
	@RequestMapping(value = "/api/{table}", method = RequestMethod.POST)
	public String createTable(
			@PathVariable("table") String table,
			@RequestBody String body) throws SQLException, ClassNotFoundException {
		Connection connection = Connection.CreateConnection();
		BaseDao dao = new BaseDao(connection);
		return  dao.createTable(table, body);
	}
	@RequestMapping(value = "/api/{table}", method = RequestMethod.GET)
	public String getRecords(
			@PathVariable("table") String table) throws SQLException, ClassNotFoundException {
		Connection connection = Connection.CreateConnection();
		BaseDao dao = new BaseDao(connection);
		return dao.getFromTable(table);
	}

	@RequestMapping(value = "/api/{table}/{id}", method = RequestMethod.GET)
	public String getRecordsByid(
			@PathVariable("table") String table, @PathVariable("id") String id) throws SQLException, ClassNotFoundException {
		Connection connection = Connection.CreateConnection();
		BaseDao dao = new BaseDao(connection);
		return dao.getByid(table,id);
	}
	@RequestMapping(value = "/api/{table}/{id}", method = RequestMethod.DELETE)
	public Integer deleteRecordsByid(
			@PathVariable("table") String table, @PathVariable("id") String id) throws SQLException, ClassNotFoundException {
		Connection connection = Connection.CreateConnection();
		BaseDao dao = new BaseDao(connection);
		return dao.deleteByid(table,id);

	}
	@RequestMapping(value = "/api/{table}/{id}", method = RequestMethod.PUT)
	public String updateRecordsbyid(
			@PathVariable("table") String table, @PathVariable("id") String id,@RequestBody String body) throws SQLException, ClassNotFoundException {
		Connection connection = Connection.CreateConnection();
		BaseDao dao = new BaseDao(connection);
		String res = dao.updateTable(table,body,id);
		return res;
	}
	@RequestMapping(value = "/api/{table}", method = RequestMethod.DELETE)
	public Integer deleteTable(
			@PathVariable("table") String table) throws SQLException, ClassNotFoundException {
		Connection connection = Connection.CreateConnection();
		BaseDao dao = new BaseDao(connection);
		return dao.deleteTable(table);
	}
	
}
