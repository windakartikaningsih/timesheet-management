/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mii.TimesheetServer.services;

import com.mii.TimesheetServer.entities.Employees;
import com.mii.TimesheetServer.entities.Task;
import com.mii.TimesheetServer.repositories.TaskRepository;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author BlackPearl
 */
@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    //READ ALL
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    public List<Task> getTaskByEmployee(Integer eId) {
        return taskRepository.getTaskByEmployee(eId);
    }

    public List<Task> getByMonthAndYear(Task task) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(task.getDate());
        Integer firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        Integer lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        Integer month = cal.get(Calendar.MONTH) + 1;
        Integer year = cal.get(Calendar.YEAR);
        Date start = new SimpleDateFormat("dd-MM-yyyy").parse(firstDay + "-" + month + "-" + year);
        Date end = new SimpleDateFormat("dd-MM-yyyy").parse(lastDay + "-" + month + "-" + year);
        return taskRepository.findAllByDateBetweenAndEmployees(start, end, task.getEmployees());
    }

    public Optional<Task> getByMonthAndEmployee(Date date, Employees employee) {
        return taskRepository.findByDateAndEmployees(date, employee);
    }

    public byte[] generateExcel(List<Task> tasks) throws ParseException {
        String excelDir = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\Format_Timesheet_Excel.xlsx";
        try {
            //Open th excel
            FileInputStream fis = new FileInputStream(new File(excelDir));
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0);
            //Create Header
            Task task = tasks.get(0);
            inputHeaderExcel(task, sheet);
            inputFooterExcel(task, sheet);
            //Input value into row and column
            inputTableData(tasks, sheet);
            //Save file
            fis.close();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();
            return bos.toByteArray();
        } catch (IOException | EncryptedDocumentException | InvalidFormatException e) {
            return null;
        }
    }

    public String getExcelFileType() throws IOException {
        String dir = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\Format_Timesheet_Excel.xlsx";
        File file = new File(dir);
        return Files.probeContentType(file.toPath());
    }

    public void inputHeaderExcel(Task task, Sheet sheet) {
        Cell projectName = sheet.getRow(0).getCell(3);
        Cell division = sheet.getRow(1).getCell(3);
        Cell name = sheet.getRow(2).getCell(3);
        Cell employeeId = sheet.getRow(3).getCell(3);
        Cell period = sheet.getRow(4).getCell(3);

        projectName.setCellValue(task.getEmployees().getDivision().getManagerSite().getSite().getSiteName());
        division.setCellValue(task.getEmployees().getDivision().getId());
        name.setCellValue(task.getEmployees().getFirstName() + " " + task.getEmployees().getFirstName());
        employeeId.setCellValue(task.getEmployees().getId());
        period.setCellValue(new SimpleDateFormat("MMM, yyyy").format(task.getDate()));
    }

    public void inputTableData(List<Task> tasks, Sheet sheet) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(tasks.get(0).getDate());
        Integer lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        Integer month = cal.get(Calendar.MONTH);
        Integer year = cal.get(Calendar.YEAR);
        Integer pCounter = 0;
        Integer sCounter = 0;
        Integer ptCounter = 0;
        Integer aCounter = 0;
        CellStyle cs = sheet.getWorkbook().createCellStyle();
        cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cs.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setAlignment(HorizontalAlignment.CENTER);

        int i = 0;  //for while loop
        int j = 0;  //for tasks loop
        while (i < lastDay) {
            Date today = new GregorianCalendar(year, month, i + 1).getTime();
            Calendar todayCal = Calendar.getInstance();
            todayCal.setTime(today);
            Cell date = sheet.getRow(i + 9).getCell(0);
            Cell start = sheet.getRow(i + 9).getCell(1);
            Cell end = sheet.getRow(i + 9).getCell(2);
            Cell total = sheet.getRow(i + 9).getCell(3);
            Cell p = sheet.getRow(i + 9).getCell(4);
            Cell s = sheet.getRow(i + 9).getCell(5);
            Cell pt = sheet.getRow(i + 9).getCell(6);
            Cell a = sheet.getRow(i + 9).getCell(7);
            Cell activity = sheet.getRow(i + 9).getCell(8);
            date.setCellValue(new SimpleDateFormat("dd-MM-yyyy").format(today));

            if (j < tasks.size()) {
                if (tasks.get(j) != null && getZeroTime(today).compareTo(getZeroTime(tasks.get(j).getDate())) == 0) {
                    Duration duration = Duration.between(tasks.get(j).getEndHour().toInstant(), tasks.get(j).getStartHour().toInstant());
                    long hour = Math.abs(duration.toHours());
                    long minute = duration.toMinutes() % 60;
                    start.setCellValue(new SimpleDateFormat("HH:mm").format(tasks.get(j).getStartHour()));
                    end.setCellValue(new SimpleDateFormat("HH:mm").format(tasks.get(j).getEndHour()));
                    total.setCellValue(hour + ":" + minute);
                    activity.setCellValue(tasks.get(j).getCategory().getCategoryName() + " - " + tasks.get(j).getActivity());
                    switch (tasks.get(j).getStatusAttendance().getStatusAttendanceName()) {
                        case "Present":
                            p.setCellValue("P");
                            pCounter++;
                            break;
                        case "Sick":
                            s.setCellValue("S");
                            sCounter++;
                            break;
                        case "Permit":
                            pt.setCellValue("PT");
                            ptCounter++;
                            break;
                        case "Absent":
                            a.setCellValue("A");
                            aCounter++;
                    }
                    j++;
                } else {
                    date.setCellStyle(cs);
                    start.setCellStyle(cs);
                    end.setCellStyle(cs);
                    total.setCellStyle(cs);
                    activity.setCellStyle(cs);
                    p.setCellStyle(cs);
                    s.setCellStyle(cs);
                    pt.setCellStyle(cs);
                    a.setCellStyle(cs);
                }
            } else {
                date.setCellStyle(cs);
                start.setCellStyle(cs);
                end.setCellStyle(cs);
                total.setCellStyle(cs);
                activity.setCellStyle(cs);
                p.setCellStyle(cs);
                s.setCellStyle(cs);
                pt.setCellStyle(cs);
                a.setCellStyle(cs);
            }

            i++;
        }
        sheet.getRow(40).getCell(4).setCellValue(pCounter);
        sheet.getRow(40).getCell(5).setCellValue(sCounter);
        sheet.getRow(40).getCell(6).setCellValue(ptCounter);
        sheet.getRow(40).getCell(7).setCellValue(aCounter);
    }

    public void inputFooterExcel(Task task, Sheet sheet) {
        Cell name = sheet.getRow(47).getCell(3);
        Cell manager = sheet.getRow(47).getCell(9);
        Cell date1 = sheet.getRow(48).getCell(3);
        Cell date2 = sheet.getRow(48).getCell(9);
        String date = new SimpleDateFormat("dd MMM yyyy").format(new Date());
        name.setCellValue("(" + task.getEmployees().getFirstName() + " " + task.getEmployees().getLastName() + ")");
        manager.setCellValue("(" + task.getEmployees().getDivision().getManagerSite().getFirstName()
                + " " + task.getEmployees().getDivision().getManagerSite().getLastName() + ")");
        date1.setCellValue(date);
        date2.setCellValue(date);
    }

    public Date getZeroTime(Date date) {
        Date res = date;
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        res = calendar.getTime();
        return res;
    }

    //READ BY ID
    public Optional<Task> getById(String id) {
        return taskRepository.findById(id);
    }

    public boolean existById(String id) {
        return taskRepository.existsById(id);
    }

    //CREATE
    public boolean createTask(Task task) {
        taskRepository.saveAndFlush(task);
        return true;
    }

    //UPDATE
    public boolean updateTask(String id, Task task) {
        Optional<Task> tsk = taskRepository.findById(id);
        if (tsk.isPresent()) {
            taskRepository.updateTask(task.getProjectName(), task.getDate(), task.getStartHour(), task.getEndHour(),
                    task.getActivity(), task.getStatusAttendance().getId(), task.getCategory().getId(), id);
            return true;
        }
        return false;
    }

    //DELETE
    public Task deleteTask(String id) {
        taskRepository.deleteById(id);
        return taskRepository.getOne(id);
    }

}
