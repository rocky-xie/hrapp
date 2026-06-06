package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.IntegrationTest;
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

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BatchResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PositionRepository positionRepository;

    @BeforeEach
    void setUp() {
        skillRepository.deleteAll();
        personRepository.deleteAll();
        positionRepository.deleteAll();
    }

    @Test
    @Transactional
    void testExportAndImportSkills() throws Exception {
        Skill s1 = new Skill().skillCode("SKL-001").skillName("Java").skillType(SkillType.TECHNICAL).measurableFlag(true);
        Skill s2 = new Skill().skillCode("SKL-002").skillName("Project Mgmt").skillType(SkillType.BUSINESS).measurableFlag(false);
        skillRepository.saveAndFlush(s1);
        skillRepository.saveAndFlush(s2);

        MvcResult exportResult = mockMvc.perform(get("/api/batch/export/skills")).andExpect(status().isOk()).andReturn();
        byte[] exportBytes = exportResult.getResponse().getContentAsByteArray();

        try (Workbook wb = new XSSFWorkbook(new ByteArrayInputStream(exportBytes))) {
            Sheet sheet = wb.getSheetAt(0);
            Row header = sheet.getRow(0);
            assertThat(header.getCell(0).getStringCellValue()).isEqualTo("ID");
            assertThat(header.getCell(1).getStringCellValue()).isEqualTo("Skill Code");
            assertThat(header.getCell(2).getStringCellValue()).isEqualTo("Skill Name");
            assertThat(header.getCell(3).getStringCellValue()).isEqualTo("Skill Type");
            assertThat(header.getCell(4).getStringCellValue()).isEqualTo("Measurable Flag");
            assertThat(header.getCell(5).getStringCellValue()).isEqualTo("Description");

            assertThat(sheet.getLastRowNum()).isEqualTo(2);
            Row r1 = sheet.getRow(1);
            assertThat(r1.getCell(1).getStringCellValue()).isEqualTo("SKL-001");

            Row newRow = sheet.createRow(3);
            newRow.createCell(0);
            newRow.createCell(1).setCellValue("SKL-NEW");
            newRow.createCell(2).setCellValue("New Skill");
            newRow.createCell(3).setCellValue(SkillType.BUSINESS.name());
            newRow.createCell(4).setCellValue("Yes");
            newRow.createCell(5).setCellValue("Brand new skill");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            wb.write(bos);
            MockMultipartFile file = new MockMultipartFile(
                "file",
                "skills.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                bos.toByteArray()
            );

            MvcResult importResult = mockMvc
                .perform(multipart("/api/batch/import/skills").file(file))
                .andExpect(status().isOk())
                .andReturn();
            String body = importResult.getResponse().getContentAsString();
            assertThat(body).contains("Imported 3 skills");
        }
    }

    @Test
    @Transactional
    void testExportAndImportPersons() throws Exception {
        Person p1 = new Person()
            .personName("Alice")
            .employeeCode("EMP-001")
            .employmentStatus(EmploymentStatus.CORE_STAFF)
            .mentorFlag(true)
            .coreCandidateFlag(false);
        Person p2 = new Person()
            .personName("Bob")
            .employeeCode("EMP-002")
            .employmentStatus(EmploymentStatus.CORE_STAFF)
            .mentorFlag(false)
            .coreCandidateFlag(true)
            .gender(Gender.MALE)
            .age(30);
        personRepository.saveAndFlush(p1);
        personRepository.saveAndFlush(p2);

        MvcResult exportResult = mockMvc.perform(get("/api/batch/export/persons")).andExpect(status().isOk()).andReturn();
        byte[] exportBytes = exportResult.getResponse().getContentAsByteArray();

        try (Workbook wb = new XSSFWorkbook(new ByteArrayInputStream(exportBytes))) {
            Sheet sheet = wb.getSheetAt(0);
            Row header = sheet.getRow(0);
            assertThat(header.getCell(0).getStringCellValue()).isEqualTo("ID");
            assertThat(header.getCell(1).getStringCellValue()).isEqualTo("Person Name");
            assertThat(header.getCell(2).getStringCellValue()).isEqualTo("Employee Code");
            assertThat(header.getCell(9).getStringCellValue()).isEqualTo("Mentor Flag");
            assertThat(header.getCell(10).getStringCellValue()).isEqualTo("Core Candidate Flag");

            Row newRow = sheet.createRow(3);
            newRow.createCell(0);
            newRow.createCell(1).setCellValue("Charlie");
            newRow.createCell(2).setCellValue("EMP-NEW");
            newRow.createCell(3).setCellValue("Engineering");
            newRow.createCell(4).setCellValue("Developer");
            newRow.createCell(5).setCellValue(LocalDate.now().toString());
            newRow.createCell(6).setCellValue(EmploymentStatus.CORE_STAFF.name());
            newRow.createCell(7).setCellValue(Gender.MALE.name());
            newRow.createCell(8).setCellValue(25);
            newRow.createCell(9).setCellValue("No");
            newRow.createCell(10).setCellValue("Yes");
            newRow.createCell(11).setCellValue("A new person");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            wb.write(bos);
            MockMultipartFile file = new MockMultipartFile(
                "file",
                "persons.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                bos.toByteArray()
            );

            MvcResult importResult = mockMvc
                .perform(multipart("/api/batch/import/persons").file(file))
                .andExpect(status().isOk())
                .andReturn();
            String body = importResult.getResponse().getContentAsString();
            assertThat(body).contains("Imported 3 persons");
        }
    }

    @Test
    @Transactional
    void testExportAndImportPositions() throws Exception {
        Position pos1 = new Position()
            .positionCode("POS-001")
            .positionName("Manager")
            .positionType(PositionType.MANAGEMENT_SUPPORT)
            .businessImportance(ImportanceLevel.HIGH)
            .active(true)
            .keyPosition(true);
        positionRepository.saveAndFlush(pos1);

        MvcResult exportResult = mockMvc.perform(get("/api/batch/export/positions")).andExpect(status().isOk()).andReturn();
        byte[] exportBytes = exportResult.getResponse().getContentAsByteArray();

        try (Workbook wb = new XSSFWorkbook(new ByteArrayInputStream(exportBytes))) {
            Sheet sheet = wb.getSheetAt(0);
            Row header = sheet.getRow(0);
            assertThat(header.getCell(1).getStringCellValue()).isEqualTo("Position Code");
            assertThat(header.getCell(3).getStringCellValue()).isEqualTo("Position Type");
            assertThat(header.getCell(4).getStringCellValue()).isEqualTo("Business Importance");

            Row newRow = sheet.createRow(2);
            newRow.createCell(0);
            newRow.createCell(1).setCellValue("POS-NEW");
            newRow.createCell(2).setCellValue("New Position");
            newRow.createCell(3).setCellValue(PositionType.TECHNICAL.name());
            newRow.createCell(4).setCellValue(ImportanceLevel.LOW.name());
            newRow.createCell(5).setCellValue("Yes");
            newRow.createCell(6).setCellValue("No");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            wb.write(bos);
            MockMultipartFile file = new MockMultipartFile(
                "file",
                "positions.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                bos.toByteArray()
            );

            MvcResult importResult = mockMvc
                .perform(multipart("/api/batch/import/positions").file(file))
                .andExpect(status().isOk())
                .andReturn();
            String body = importResult.getResponse().getContentAsString();
            assertThat(body).contains("Imported 2 positions");
        }
    }

    @Test
    @Transactional
    void testImportSkillsWithExistingSkillCodeUpdates() throws Exception {
        Skill existing = new Skill().skillCode("SKL-EXIST").skillName("Old Name").skillType(SkillType.TECHNICAL).measurableFlag(false);
        skillRepository.saveAndFlush(existing);

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Skills");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Skill Code");
            header.createCell(2).setCellValue("Skill Name");
            header.createCell(3).setCellValue("Skill Type");
            header.createCell(4).setCellValue("Measurable Flag");
            header.createCell(5).setCellValue("Description");

            Row row = sheet.createRow(1);
            row.createCell(0);
            row.createCell(1).setCellValue("SKL-EXIST");
            row.createCell(2).setCellValue("Updated Name");
            row.createCell(3).setCellValue(SkillType.CERTIFICATE.name());
            row.createCell(4).setCellValue("Yes");
            row.createCell(5).setCellValue("Updated description");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            wb.write(bos);
            MockMultipartFile file = new MockMultipartFile(
                "file",
                "skills.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                bos.toByteArray()
            );

            mockMvc.perform(multipart("/api/batch/import/skills").file(file)).andExpect(status().isOk());

            Skill updated = skillRepository.findBySkillCode("SKL-EXIST").orElseThrow();
            assertThat(updated.getSkillName()).isEqualTo("Updated Name");
            assertThat(updated.getSkillType()).isEqualTo(SkillType.CERTIFICATE);
            assertThat(updated.getMeasurableFlag()).isTrue();
            assertThat(updated.getDescription()).isEqualTo("Updated description");
        }
    }

    @Test
    @Transactional
    void testImportSkillsWithMissingSkillCodeReportsError() throws Exception {
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Skills");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Skill Code");
            header.createCell(2).setCellValue("Skill Name");

            Row row = sheet.createRow(1);
            row.createCell(0);
            row.createCell(1).setCellValue("");
            row.createCell(2).setCellValue("No Code Skill");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            wb.write(bos);
            MockMultipartFile file = new MockMultipartFile(
                "file",
                "skills.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                bos.toByteArray()
            );

            MvcResult result = mockMvc.perform(multipart("/api/batch/import/skills").file(file)).andExpect(status().isOk()).andReturn();
            assertThat(result.getResponse().getContentAsString()).contains("Import failed with");
            assertThat(result.getResponse().getContentAsString()).contains("skillCode is required");
        }
    }

    @Test
    @Transactional
    void testImportSkillsWithOneBadRowSavesNone() throws Exception {
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Skills");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Skill Code");
            header.createCell(2).setCellValue("Skill Name");
            header.createCell(3).setCellValue("Skill Type");
            header.createCell(4).setCellValue("Measurable Flag");
            header.createCell(5).setCellValue("Description");

            Row goodRow = sheet.createRow(1);
            goodRow.createCell(0);
            goodRow.createCell(1).setCellValue("SKL-GOOD");
            goodRow.createCell(2).setCellValue("Good Skill");
            goodRow.createCell(3).setCellValue(SkillType.TECHNICAL.name());
            goodRow.createCell(4).setCellValue("Yes");

            Row badRow = sheet.createRow(2);
            badRow.createCell(0);
            badRow.createCell(1).setCellValue("SKL-BAD");
            badRow.createCell(2).setCellValue("");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            wb.write(bos);
            MockMultipartFile file = new MockMultipartFile(
                "file",
                "skills.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                bos.toByteArray()
            );

            MvcResult result = mockMvc.perform(multipart("/api/batch/import/skills").file(file)).andExpect(status().isOk()).andReturn();
            assertThat(result.getResponse().getContentAsString()).contains("Import failed with");
            assertThat(result.getResponse().getContentAsString()).contains("skillName is required");

            assertThat(skillRepository.findBySkillCode("SKL-GOOD")).isEmpty();
        }
    }

    @Test
    @Transactional
    void testImportPersonsWithInvalidEnumSavesNone() throws Exception {
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Persons");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Person Name");
            header.createCell(2).setCellValue("Employee Code");
            header.createCell(6).setCellValue("Employment Status");
            header.createCell(7).setCellValue("Gender");

            Row goodRow = sheet.createRow(1);
            goodRow.createCell(0);
            goodRow.createCell(1).setCellValue("Alice");
            goodRow.createCell(2).setCellValue("EMP-ALLORNOTHING");
            goodRow.createCell(6).setCellValue(EmploymentStatus.CORE_STAFF.name());
            goodRow.createCell(7).setCellValue(Gender.FEMALE.name());

            Row badRow = sheet.createRow(2);
            badRow.createCell(0);
            badRow.createCell(1).setCellValue("Bob");
            badRow.createCell(2).setCellValue("EMP-BAD");
            badRow.createCell(6).setCellValue("INVALID_STATUS");
            badRow.createCell(7).setCellValue(Gender.MALE.name());

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            wb.write(bos);
            MockMultipartFile file = new MockMultipartFile(
                "file",
                "persons.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                bos.toByteArray()
            );

            MvcResult result = mockMvc.perform(multipart("/api/batch/import/persons").file(file)).andExpect(status().isOk()).andReturn();
            assertThat(result.getResponse().getContentAsString()).contains("Import failed with");
            assertThat(result.getResponse().getContentAsString()).contains("invalid EmploymentStatus");

            assertThat(personRepository.findByEmployeeCode("EMP-ALLORNOTHING")).isEmpty();
        }
    }

    @Test
    @Transactional
    void testImportPositionsWithMissingCodeSavesNone() throws Exception {
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Positions");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Position Code");
            header.createCell(2).setCellValue("Position Name");

            Row goodRow = sheet.createRow(1);
            goodRow.createCell(0);
            goodRow.createCell(1).setCellValue("POS-GOOD");
            goodRow.createCell(2).setCellValue("Good Position");

            Row badRow = sheet.createRow(2);
            badRow.createCell(0);
            badRow.createCell(1).setCellValue("");
            badRow.createCell(2).setCellValue("No Code");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            wb.write(bos);
            MockMultipartFile file = new MockMultipartFile(
                "file",
                "positions.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                bos.toByteArray()
            );

            MvcResult result = mockMvc.perform(multipart("/api/batch/import/positions").file(file)).andExpect(status().isOk()).andReturn();
            assertThat(result.getResponse().getContentAsString()).contains("Import failed with");
            assertThat(result.getResponse().getContentAsString()).contains("positionCode is required");

            assertThat(positionRepository.findByPositionCode("POS-GOOD")).isEmpty();
        }
    }
}
