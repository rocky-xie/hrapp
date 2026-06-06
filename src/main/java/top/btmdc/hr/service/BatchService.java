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
import top.btmdc.hr.domain.enumeration.EmploymentStatus;
import top.btmdc.hr.domain.enumeration.Gender;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.PositionType;
import top.btmdc.hr.domain.enumeration.SkillType;
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
            createHeaderRow(
                sheet,
                "ID",
                "Person Name",
                "Employee Code",
                "Department",
                "Current Role",
                "Join Date",
                "Employment Status",
                "Gender",
                "Age",
                "Mentor Flag",
                "Core Candidate Flag",
                "Note"
            );
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
                row.createCell(7).setCellValue(p.getGender() != null ? p.getGender().name() : "");
                row.createCell(8).setCellValue(p.getAge() != null ? p.getAge() : 0);
                row.createCell(9).setCellValue(Boolean.TRUE.equals(p.getMentorFlag()) ? "Yes" : "No");
                row.createCell(10).setCellValue(Boolean.TRUE.equals(p.getCoreCandidateFlag()) ? "Yes" : "No");
                row.createCell(11).setCellValue(p.getNote() != null ? p.getNote() : "");
            }
            autoSizeColumns(sheet, 12);
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
            createHeaderRow(sheet, "ID", "Skill Code", "Skill Name", "Skill Type", "Measurable Flag", "Description");
            for (int i = 0; i < skills.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Skill s = skills.get(i);
                row.createCell(0).setCellValue(s.getId() != null ? s.getId() : 0);
                row.createCell(1).setCellValue(s.getSkillCode() != null ? s.getSkillCode() : "");
                row.createCell(2).setCellValue(s.getSkillName() != null ? s.getSkillName() : "");
                row.createCell(3).setCellValue(s.getSkillType() != null ? s.getSkillType().name() : "");
                row.createCell(4).setCellValue(Boolean.TRUE.equals(s.getMeasurableFlag()) ? "Yes" : "No");
                row.createCell(5).setCellValue(s.getDescription() != null ? s.getDescription() : "");
            }
            autoSizeColumns(sheet, 6);
            return toBytes(wb);
        }
    }

    @Transactional
    public String importPersons(InputStream inputStream) throws IOException {
        List<String> errors = new ArrayList<>();
        List<Person> validPersons = new ArrayList<>();
        try (Workbook wb = new XSSFWorkbook(inputStream)) {
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) rows.next();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            while (rows.hasNext()) {
                Row row = rows.next();
                List<String> rowErrors = new ArrayList<>();
                Cell nameCell = row.getCell(1);
                if (nameCell == null || getString(nameCell).isBlank()) {
                    rowErrors.add("Row " + (row.getRowNum() + 1) + ": personName is required");
                }
                String empCode = getString(row.getCell(2));
                Optional<Person> existing = personRepository.findByEmployeeCode(empCode);
                Person p = existing.orElseGet(Person::new);
                p.setPersonName(getString(nameCell));
                p.setEmployeeCode(empCode.isBlank() ? null : empCode);
                p.setDepartment(getString(row.getCell(3)));
                p.setCurrentRole(getString(row.getCell(4)));
                String joinDateStr = getString(row.getCell(5));
                if (!joinDateStr.isBlank()) {
                    try {
                        p.setJoinDate(LocalDate.parse(joinDateStr, dtf));
                    } catch (Exception e) {
                        rowErrors.add("Row " + (row.getRowNum() + 1) + ": invalid date '" + joinDateStr + "'");
                    }
                }
                String statusStr = getString(row.getCell(6));
                if (!statusStr.isBlank()) {
                    try {
                        p.setEmploymentStatus(EmploymentStatus.valueOf(statusStr));
                    } catch (IllegalArgumentException e) {
                        rowErrors.add("Row " + (row.getRowNum() + 1) + ": invalid EmploymentStatus '" + statusStr + "'");
                    }
                }
                String genderStr = getString(row.getCell(7));
                if (!genderStr.isBlank()) {
                    try {
                        p.setGender(Gender.valueOf(genderStr));
                    } catch (IllegalArgumentException e) {
                        rowErrors.add("Row " + (row.getRowNum() + 1) + ": invalid Gender '" + genderStr + "'");
                    }
                }
                String ageStr = getString(row.getCell(8));
                if (!ageStr.isBlank()) {
                    try {
                        p.setAge(Integer.valueOf(ageStr));
                    } catch (NumberFormatException e) {
                        rowErrors.add("Row " + (row.getRowNum() + 1) + ": invalid Age '" + ageStr + "'");
                    }
                }
                String mentorStr = getString(row.getCell(9));
                p.setMentorFlag("Yes".equalsIgnoreCase(mentorStr) || "true".equalsIgnoreCase(mentorStr));
                String coreStr = getString(row.getCell(10));
                p.setCoreCandidateFlag("Yes".equalsIgnoreCase(coreStr) || "true".equalsIgnoreCase(coreStr));
                p.setNote(getString(row.getCell(11)));
                if (!rowErrors.isEmpty()) {
                    errors.addAll(rowErrors);
                } else {
                    validPersons.add(p);
                }
            }
        }
        if (!errors.isEmpty()) {
            return "Import failed with " + errors.size() + " errors: " + String.join("; ", errors);
        }
        for (Person p : validPersons) {
            personRepository.save(p);
        }
        personRepository.flush();
        return "Imported " + validPersons.size() + " persons";
    }

    @Transactional
    public String importPositions(InputStream inputStream) throws IOException {
        List<String> errors = new ArrayList<>();
        List<Position> validPositions = new ArrayList<>();
        try (Workbook wb = new XSSFWorkbook(inputStream)) {
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) rows.next();
            while (rows.hasNext()) {
                Row row = rows.next();
                List<String> rowErrors = new ArrayList<>();
                Cell codeCell = row.getCell(1);
                if (codeCell == null || getString(codeCell).isBlank()) {
                    rowErrors.add("Row " + (row.getRowNum() + 1) + ": positionCode is required");
                }
                if (!rowErrors.isEmpty()) {
                    errors.addAll(rowErrors);
                    continue;
                }
                String posCode = getString(codeCell);
                Optional<Position> existing = positionRepository.findByPositionCode(posCode);
                Position p = existing.orElseGet(Position::new);
                p.setPositionCode(posCode);
                Cell nameCell = row.getCell(2);
                p.setPositionName(nameCell != null ? getString(nameCell) : posCode);
                String typeStr = getString(row.getCell(3));
                if (!typeStr.isBlank()) {
                    try {
                        p.setPositionType(PositionType.valueOf(typeStr));
                    } catch (IllegalArgumentException e) {
                        rowErrors.add("Row " + (row.getRowNum() + 1) + ": invalid PositionType '" + typeStr + "'");
                    }
                }
                String impStr = getString(row.getCell(4));
                if (!impStr.isBlank()) {
                    try {
                        p.setBusinessImportance(ImportanceLevel.valueOf(impStr));
                    } catch (IllegalArgumentException e) {
                        rowErrors.add("Row " + (row.getRowNum() + 1) + ": invalid ImportanceLevel '" + impStr + "'");
                    }
                }
                String activeStr = getString(row.getCell(5));
                p.setActive("Yes".equalsIgnoreCase(activeStr) || "true".equalsIgnoreCase(activeStr));
                String keyStr = getString(row.getCell(6));
                p.setKeyPosition("Yes".equalsIgnoreCase(keyStr) || "true".equalsIgnoreCase(keyStr));
                if (!rowErrors.isEmpty()) {
                    errors.addAll(rowErrors);
                } else {
                    validPositions.add(p);
                }
            }
        }
        if (!errors.isEmpty()) {
            return "Import failed with " + errors.size() + " errors: " + String.join("; ", errors);
        }
        for (Position p : validPositions) {
            positionRepository.save(p);
        }
        positionRepository.flush();
        return "Imported " + validPositions.size() + " positions";
    }

    @Transactional
    public String importSkills(InputStream inputStream) throws IOException {
        List<String> errors = new ArrayList<>();
        List<Skill> validSkills = new ArrayList<>();
        try (Workbook wb = new XSSFWorkbook(inputStream)) {
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            if (rows.hasNext()) rows.next();
            while (rows.hasNext()) {
                Row row = rows.next();
                List<String> rowErrors = new ArrayList<>();
                Cell codeCell = row.getCell(1);
                if (codeCell == null || getString(codeCell).isBlank()) {
                    rowErrors.add("Row " + (row.getRowNum() + 1) + ": skillCode is required");
                }
                if (!rowErrors.isEmpty()) {
                    errors.addAll(rowErrors);
                    continue;
                }
                String skillCode = getString(codeCell);
                Optional<Skill> existing = skillRepository.findBySkillCode(skillCode);
                Skill s = existing.orElseGet(Skill::new);
                s.setSkillCode(skillCode);
                Cell nameCell = row.getCell(2);
                if (nameCell == null || getString(nameCell).isBlank()) {
                    rowErrors.add("Row " + (row.getRowNum() + 1) + ": skillName is required");
                }
                if (!rowErrors.isEmpty()) {
                    errors.addAll(rowErrors);
                    continue;
                }
                s.setSkillName(getString(nameCell));
                String typeStr = getString(row.getCell(3));
                if (typeStr != null && !typeStr.isBlank()) {
                    try {
                        s.setSkillType(SkillType.valueOf(typeStr));
                    } catch (IllegalArgumentException e) {
                        rowErrors.add("Row " + (row.getRowNum() + 1) + ": invalid SkillType '" + typeStr + "'");
                    }
                }
                String measFlag = getString(row.getCell(4));
                s.setMeasurableFlag("Yes".equalsIgnoreCase(measFlag) || "true".equalsIgnoreCase(measFlag));
                s.setDescription(getString(row.getCell(5)));
                if (!rowErrors.isEmpty()) {
                    errors.addAll(rowErrors);
                } else {
                    validSkills.add(s);
                }
            }
        }
        if (!errors.isEmpty()) {
            return "Import failed with " + errors.size() + " errors: " + String.join("; ", errors);
        }
        for (Skill s : validSkills) {
            skillRepository.save(s);
        }
        skillRepository.flush();
        return "Imported " + validSkills.size() + " skills";
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
