package com.zifang.util.script.linux.R;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class App {
	public static void main(String[] args) {
		RConnection c;
		try {
			c = new RConnection();
			String command1 = "c(\"John Davis\",\"Angela Williams\",\"Bullwinkle Mosse\","
					+ "\"David Jones\",\"Janice Markhammer\",\"Cheryl Cushing\","
					+ "\"Reuven Ytzrhak\",\"Greg Knox\",\"Joel England\",\"Marry Rayburn\")";
			String command2 = "c(502,600,412,358,495,512,410,625,573,522)";
			String command3 = "c(95,99,80,82,75,85,80,95,89,86)";
			String command4 = "c(25,22,18,15,20,28,15,30,27,18)";
			String command5 = " data.frame(Student,Math,Science,English)";
			REXP Student = c.eval(command1);
			REXP Math = c.eval(command2);
			REXP Science = c.eval(command3);
			REXP English = c.eval(command4);
			c.assign("Student", Student);
			c.assign("Math", Math);
			c.assign("Science", Science);
			c.assign("English", English);
			REXP roster = c.eval(command5);
			RList s1 = roster.asList();
			Vector attributesStrings = s1.names;
			REXPFactor studentREXP = (REXPFactor) s1.at(0);
			RFactor studentRFactor = studentREXP.asFactor();
			int[] studentID = studentRFactor.asIntegers();
			String[] studentName = studentRFactor.levels();
			double[] mathMarks = s1.at(1).asDoubles();
			double[] scienceMarks = s1.at(2).asDoubles();
			double[] englishMarks = s1.at(3).asDoubles();
			List<Student> studentsList = new ArrayList();
			for (int x = 0; x < studentID.length; x++) {
				Student student = new Student();
				student.setStudentName(studentName[studentID[x] - 1]);
				student.setMathMarks(mathMarks[x]);
				student.setEnglishMarks(englishMarks[x]);
				student.setScienceMarks(scienceMarks[x]);
				studentsList.add(student);
			}
			JsonArray studentJsonArray = new JsonArray();
			for (int x = 0; x < studentsList.size(); x++) {
				Gson studentGson = new Gson();
				studentJsonArray.add(studentGson.toJsonTree(studentsList.get(x)));
			}
			System.out.println(studentJsonArray.toString());
			System.out.println(roster.isVector());
		} catch (RserveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("连接不上Rserve");
		} catch (REXPMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
