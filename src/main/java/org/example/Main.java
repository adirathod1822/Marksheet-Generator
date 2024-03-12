
package org.example;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

class FolderCreation {
    public static void createFolderIfNotExists() {
        File folder = new File("SRKAY");
        if (!folder.exists()) {
            folder.mkdir();
        }
    }
}

class Student {
    private int student_id;
    private String first_name;
    private String last_name;
    private String address;
    private String city;
    private String student_class;

    public Student(int student_id, String first_name, String last_name, String address, String city, String student_class) {
        this.student_id = student_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.address = address;
        this.city = city;
        this.student_class = student_class;
    }

    public int getStudent_id() {
        return student_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }
}

class Subject {
    private int subject_id;
    private String subject_name;

    public Subject(int subject_id, String subject_name) {
        this.subject_id = subject_id;
        this.subject_name = subject_name;
    }

    public String getSubject_name() {
        return subject_name;
    }
}

class Result {
    private int result_id;
    private int student_id;
    private int subject_id;
    private int mark;

    public Result(int result_id, int student_id, int subject_id, int mark) {
        this.result_id = result_id;
        this.student_id = student_id;
        this.subject_id = subject_id;
        this.mark = mark;
    }

    public int getStudent_id() {
        return student_id;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public int getMark() {
        return mark;
    }
}

class Marksheet {
    private String student_name;
    private Map<String, Integer> marks;
    private int total;
    private int average;
    private String grade;

    public Marksheet(String student_name, Map<String, Integer> marks, int total, int average, String grade) {
        this.student_name = student_name;
        this.marks = marks;
        this.total = total;
        this.average = average;
        this.grade = grade;
    }

    public static Map<Integer, Marksheet> generateResults(Map<Integer, Student> students, Map<Integer, Result> results, Map<Integer, Subject> subjects) {
        Map<Integer, Marksheet> marksheetMap = new HashMap<>();

        results.values().stream()
                .collect(Collectors.groupingBy(Result::getStudent_id))
                .forEach((studentId, studentResults) -> {
                    Map<String, Integer> marks = new HashMap<>();
                    int total = 0;
                    for (Result result : studentResults) {
                        Subject subject = subjects.get(result.getSubject_id());
                        if (subject != null) {
                            marks.put(subject.getSubject_name(), result.getMark());
                            total += result.getMark();
                        }
                    }
                    int average = total / marks.size();
                    Student student = students.get(studentId);
                    if (student != null) {
                        String studentName = student.getFirst_name() + " " + student.getLast_name();
                        String grade = calculateGrade(average);
                        Marksheet marksheet = new Marksheet(studentName, marks, total, average, grade);
                        marksheetMap.put(studentId, marksheet);
                    }
                });

        return marksheetMap;
    }

    private static String calculateGrade(int average) {
        if (average >= 90 && average <= 100) {
            return "A+";
        } else if (average >= 80 && average <= 89) {
            return "A";
        } else if (average >= 70 && average <= 79) {
            return "B+";
        } else if (average >= 60 && average <= 69) {
            return "B";
        } else {
            return "PASS";
        }
    }
}

public class Main {
    public static void main(String[] args) {
        FolderCreation.createFolderIfNotExists();

        // Assuming you have data for students, subjects, and results
        Map<Integer, Student> students = new HashMap<>();
        students.put(1, new Student(1, "Aditya", "Rathod", "AB house", "Bardoli", "A"));
        students.put(2, new Student(2, "Maan", "Lad", "AB house", "Bardoli", "B"));

        Map<Integer, Subject> subjects = new HashMap<>();
        subjects.put(1, new Subject(1, "English"));
        subjects.put(2, new Subject(2, "Maths"));
        subjects.put(3, new Subject(3, "Physics"));
        subjects.put(4, new Subject(4, "DS"));

        Map<Integer, Result> results = new HashMap<>();
        results.put(1, new Result(1, 1, 1, 67));
        results.put(2, new Result(2, 1, 2, 33));
        results.put(3, new Result(3, 1, 3, 89));
        results.put(4, new Result(4, 2, 1, 70));
        results.put(5, new Result(5, 2, 2, 48));
        results.put(6, new Result(6, 2, 3, 80));
        results.put(7, new Result(7, 2, 4, 95));

        Map<Integer, Marksheet> marksheet = Marksheet.generateResults(students, results, subjects);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("SRKAY/marksheet.json")) {
            gson.toJson(marksheet, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
