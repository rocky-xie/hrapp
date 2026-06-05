package top.btmdc.hr.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.Skill;
import top.btmdc.hr.repository.PersonRepository;
import top.btmdc.hr.repository.PositionRepository;
import top.btmdc.hr.repository.SkillRepository;

@Service
@Transactional
public class BatchService {

    private static final Logger LOG = LoggerFactory.getLogger(BatchService.class);

    private final PersonRepository personRepository;
    private final PositionRepository positionRepository;
    private final SkillRepository skillRepository;

    public BatchService(PersonRepository personRepository, PositionRepository positionRepository, SkillRepository skillRepository) {
        this.personRepository = personRepository;
        this.positionRepository = positionRepository;
        this.skillRepository = skillRepository;
    }

    public byte[] exportPersons() throws IOException {
        List<Person> persons = personRepository.findAll();
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Persons");
            createHeaderRow(sheet, "ID", "Person Name", "Employee Code", "Department", "Current Role", "Join Date", "Employment Status");
            for (int i = 0; i < persons.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Person p = persons.get(i);
                row.createCell(0).setCellValue(p.getId() != null ? p.getId() : 0);
                row.createCell(1).setCellValue(p.getPersonName() != null ? p.getPersonName() : "");
                row.createCell(2).setCellValue(p.getEmployeeCode() != null ? p.getEmployeeCode() : "");
                row.createCell(3).setCellValue(p.getDepartment() != null ? p.getDepartment() : "");
                row.createCell(4).setCellValue(p.getCurrentRole() != null ? p.getCurrentRole() : "");
                row.createCell(5).setCellValue(p.getJoinDate() != null ? p.getJoinDate().toString() : "");
                row.createCell(6).setCellValue(p.getEmploymentStatus() != null ? p.getEmploymentStatus().name() : "");
            }
            autoSizeColumns(sheet, 7);
            return toBytes(wb);
        }
    }

    public byte[] exportPositions() throws IOException {
        List<Position> positions = positionRepository.findAll();
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Positions");
            createHeaderRow(
                sheet,
                "ID",
                "Position Code",
                "Position Name",
                "Position Type",
                "Business Importance",
                "Active",
                "Key Position"
            );
            for (int i = 0; i < positions.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Position p = positions.get(i);
                row.createCell(0).setCellValue(p.getId() != null ? p.getId() : 0);
                row.createCell(1).setCellValue(p.getPositionCode() != null ? p.getPositionCode() : "");
                row.createCell(2).setCellValue(p.getPositionName() != null ? p.getPositionName() : "");
                row.createCell(3).setCellValue(p.getPositionType() != null ? p.getPositionType().name() : "");
                row.createCell(4).setCellValue(p.getBusinessImportance() != null ? p.getBusinessImportance().name() : "");
                row.createCell(5).setCellValue(Boolean.TRUE.equals(p.getActive()) ? "Yes" : "No");
                row.createCell(6).setCellValue(Boolean.TRUE.equals(p.getKeyPosition()) ? "Yes" : "No");
            }
            autoSizeColumns(sheet, 7);
            return toBytes(wb);
        }
    }

    public byte[] exportSkills() throws IOException {
        List<Skill> skills = skillRepository.findAll();
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Skills");
            createHeaderRow(sheet, "ID", "Skill Name", "Category", "Description");
            for (int i = 0; i < skills.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Skill s = skills.get(i);
                row.createCell(0).setCellValue(s.getId() != null ? s.getId() : 0);
                row.createCell(1).setCellValue(s.getSkillName() != null ? s.getSkillName() : "");
                row.createCell(2).setCellValue(s.getCategory() != null ? s.getCategory() : "");
                row.createCell(3).setCellValue(s.getDescription() != null ? s.getDescription() : "");
            }
            autoSizeColumns(sheet, 4);
            return toBytes(wb);
        }
    }

    @Transactional
    public String importPersons(InputStream inputStream) throws IOException {
        List<String> errors = new ArrayList<>();
        int imported = 0;
        try (Workbook wb = new XSSFWorkbook(inputStream)) {
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) rows.next();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            while (rows.hasNext()) {
                Row row = rows.next();
                try {
                    Person p = new Person();
                    Cell nameCell = row.getCell(1);
                    if (nameCell == null || getString(nameCell).isBlank()) {
                        errors.add("Row " + (row.getRowNum() + 1) + ": personName is required");
                        continue;
                    }
                    p.setPersonName(getString(nameCell));
                    p.setEmployeeCode(getString(row.getCell(2)));
                    p.setDepartment(getString(row.getCell(3)));
                    p.setCurrentRole(getString(row.getCell(4)));
                    String joinDateStr = getString(row.getCell(5));
                    if (!joinDateStr.isBlank()) {
                        p.setJoinDate(LocalDate.parse(joinDateStr, dtf));
                    }
                    personRepository.save(p);
                    imported++;
                } catch (Exception e) {
                    errors.add("Row " + (row.getRowNum() + 1) + ": " + e.getMessage());
                }
            }
        }
        String result = "Imported " + imported + " persons";
        if (!errors.isEmpty()) {
            result += " with " + errors.size() + " errors: " + String.join("; ", errors);
        }
        return result;
    }

    @Transactional
    public String importPositions(InputStream inputStream) throws IOException {
        List<String> errors = new ArrayList<>();
        int imported = 0;
        try (Workbook wb = new XSSFWorkbook(inputStream)) {
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) rows.next();
            while (rows.hasNext()) {
                Row row = rows.next();
                try {
                    Position p = new Position();
                    Cell codeCell = row.getCell(1);
                    Cell nameCell = row.getCell(2);
                    if (codeCell == null || getString(codeCell).isBlank()) {
                        errors.add("Row " + (row.getRowNum() + 1) + ": positionCode is required");
                        continue;
                    }
                    p.setPositionCode(getString(codeCell));
                    p.setPositionName(nameCell != null ? getString(nameCell) : getString(codeCell));
                    positionRepository.save(p);
                    imported++;
                } catch (Exception e) {
                    errors.add("Row " + (row.getRowNum() + 1) + ": " + e.getMessage());
                }
            }
        }
        String result = "Imported " + imported + " positions";
        if (!errors.isEmpty()) {
            result += " with " + errors.size() + " errors: " + String.join("; ", errors);
        }
        return result;
    }

    @Transactional
    public String importSkills(InputStream inputStream) throws IOException {
        List<String> errors = new ArrayList<>();
        int imported = 0;
        try (Workbook wb = new XSSFWorkbook(inputStream)) {
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) rows.next();
            while (rows.hasNext()) {
                Row row = rows.next();
                try {
                    Skill s = new Skill();
                    Cell nameCell = row.getCell(1);
                    if (nameCell == null || getString(nameCell).isBlank()) {
                        errors.add("Row " + (row.getRowNum() + 1) + ": skillName is required");
                        continue;
                    }
                    s.setSkillName(getString(nameCell));
                    s.setCategory(getString(row.getCell(2)));
                    s.setDescription(getString(row.getCell(3)));
                    skillRepository.save(s);
                    imported++;
                } catch (Exception e) {
                    errors.add("Row " + (row.getRowNum() + 1) + ": " + e.getMessage());
                }
            }
        }
        String result = "Imported " + imported + " skills";
        if (!errors.isEmpty()) {
            result += " with " + errors.size() + " errors: " + String.join("; ", errors);
        }
        return result;
    }

    private void createHeaderRow(Sheet sheet, String... headers) {
        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }
    }

    private void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private byte[] toBytes(Workbook wb) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        wb.write(bos);
        return bos.toByteArray();
    }

    private String getString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }
}
