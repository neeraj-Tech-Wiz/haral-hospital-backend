package com.haral.hospital_backend.service;

import com.haral.hospital_backend.entity.Patient;
import com.haral.hospital_backend.repository.PatientRepository;

import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(
            PatientRepository patientRepository) {

        this.patientRepository = patientRepository;
    }


    // =====================================================
    // CREATE PATIENT
    // =====================================================

    public Patient createPatient(
            Patient patient) {

        // Generate hospital patient code automatically
        String patientCode =
                generateNextPatientCode();

        patient.setPatientCode(
                patientCode
        );

        // Default active status
        patient.setActive(true);

        return patientRepository.save(
                patient
        );
    }


    // =====================================================
    // GENERATE PATIENT CODE
    // Example:
    // HARAL-000001
    // HARAL-000002
    // HARAL-000003
    // =====================================================

    private String generateNextPatientCode() {

        long nextNumber =
                patientRepository.count() + 1;

        String patientCode;

        do {

            patientCode =
                    String.format(
                            "HARAL-%06d",
                            nextNumber
                    );

            nextNumber++;

        } while (
                patientRepository
                        .existsByPatientCode(
                                patientCode
                        )
        );

        return patientCode;
    }


    // =====================================================
    // GET ALL PATIENTS
    // =====================================================

    public List<Patient> getAllPatients() {

        return patientRepository
                .findAllByOrderByCreatedAtDesc();
    }


    // =====================================================
    // GET ACTIVE PATIENTS
    // =====================================================

    public List<Patient> getActivePatients() {

        return patientRepository
                .findByActiveTrueOrderByCreatedAtDesc();
    }


    // =====================================================
    // GET PATIENT BY ID
    // =====================================================

    public Patient getPatientById(
            Long id) {

        return patientRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Patient not found with id: " + id
                        )
                );
    }


    // =====================================================
    // GET PATIENT BY PATIENT CODE
    // =====================================================

    public Patient getPatientByCode(
            String patientCode) {

        return patientRepository
                .findByPatientCode(patientCode)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Patient not found with code: "
                                        + patientCode
                        )
                );
    }


    // =====================================================
    // UPDATE PATIENT
    // =====================================================

    public Patient updatePatient(
            Long id,
            Patient patient) {

        Patient existing =
                getPatientById(id);


        // =================================================
        // PERSONAL INFORMATION
        // =================================================

        existing.setName(
                patient.getName()
        );

        existing.setMarathiName(
                patient.getMarathiName()
        );

        existing.setDateOfBirth(
                patient.getDateOfBirth()
        );

        existing.setAge(
                patient.getAge()
        );

        existing.setGender(
                patient.getGender()
        );

        existing.setBloodGroup(
                patient.getBloodGroup()
        );


        // =================================================
        // CONTACT INFORMATION
        // =================================================

        existing.setMobile(
                patient.getMobile()
        );

        existing.setEmail(
                patient.getEmail()
        );

        existing.setAddress(
                patient.getAddress()
        );

        existing.setEmergencyContact(
                patient.getEmergencyContact()
        );


        // =================================================
        // IDENTITY INFORMATION
        // =================================================

        existing.setIdentityNumber(
                patient.getIdentityNumber()
        );


        // =================================================
        // MEDICAL INFORMATION
        // =================================================

        existing.setMedicalHistory(
                patient.getMedicalHistory()
        );

        existing.setAllergies(
                patient.getAllergies()
        );

        existing.setExistingConditions(
                patient.getExistingConditions()
        );

        existing.setCurrentMedications(
                patient.getCurrentMedications()
        );

        existing.setPreviousSurgeries(
                patient.getPreviousSurgeries()
        );


        // =================================================
        // STATUS
        // =================================================

        existing.setActive(
                patient.isActive()
        );


        // IMPORTANT:
        // patientCode is NOT changed during update.

        return patientRepository.save(
                existing
        );
    }


    // =====================================================
    // DEACTIVATE PATIENT
    // =====================================================

    public Patient deactivatePatient(
            Long id) {

        Patient patient =
                getPatientById(id);

        patient.setActive(false);

        return patientRepository.save(
                patient
        );
    }


    // =====================================================
    // ACTIVATE PATIENT
    // =====================================================

    public Patient activatePatient(
            Long id) {

        Patient patient =
                getPatientById(id);

        patient.setActive(true);

        return patientRepository.save(
                patient
        );
    }


    // =====================================================
    // DELETE PATIENT
    // =====================================================

    public void deletePatient(
            Long id) {

        if (!patientRepository.existsById(id)) {

            throw new RuntimeException(
                    "Patient not found with id: " + id
            );
        }

        patientRepository.deleteById(id);
    }
    // =====================================================
// SEARCH PATIENTS
// =====================================================

    public List<Patient> searchPatients(
            String query) {

        if (query == null ||
                query.trim().isEmpty()) {

            return getAllPatients();
        }

        String search =
                query.trim();

        return patientRepository
                .findByPatientCodeContainingIgnoreCaseOrNameContainingIgnoreCaseOrMobileContaining(
                        search,
                        search,
                        search
                );
    }
    // =====================================================
// ADMIN — BULK IMPORT PATIENTS FROM EXCEL
// =====================================================

    public Map<String, Object> bulkImportPatients(
            MultipartFile file) {

        Map<String, Object> result =
                new LinkedHashMap<>();

        List<Map<String, Object>> errors =
                new ArrayList<>();

        int totalRows = 0;
        int successful = 0;
        int failed = 0;


        // =================================================
        // FILE VALIDATION
        // =================================================

        if (file == null || file.isEmpty()) {

            throw new RuntimeException(
                    "Please upload an Excel file."
            );
        }


        String fileName =
                file.getOriginalFilename();

        if (
                fileName == null ||
                        !fileName.toLowerCase()
                                .endsWith(".xlsx")
        ) {

            throw new RuntimeException(
                    "Only .xlsx Excel files are supported."
            );
        }


        try (
                InputStream inputStream =
                        file.getInputStream();

                Workbook workbook =
                        WorkbookFactory.create(
                                inputStream
                        )
        ) {

            Sheet sheet =
                    workbook.getSheetAt(0);


            // =============================================
            // HEADER ROW
            // =============================================

            Row headerRow =
                    sheet.getRow(0);

            if (headerRow == null) {

                throw new RuntimeException(
                        "Excel file does not contain a header row."
                );
            }


            // =============================================
            // PROCESS ROWS
            // =============================================

            for (
                    int rowIndex = 1;
                    rowIndex <= sheet.getLastRowNum();
                    rowIndex++
            ) {

                Row row =
                        sheet.getRow(rowIndex);

                if (
                        row == null ||
                                isRowEmpty(row)
                ) {
                    continue;
                }

                totalRows++;


                try {

                    Patient patient =
                            new Patient();


                    // -------------------------------------
                    // NAME
                    // -------------------------------------

                    String name =
                            getCellValue(
                                    row.getCell(0)
                            );

                    if (
                            name == null ||
                                    name.trim().isEmpty()
                    ) {

                        throw new RuntimeException(
                                "Name is required."
                        );
                    }

                    patient.setName(
                            name.trim()
                    );


                    // -------------------------------------
                    // MARATHI NAME
                    // -------------------------------------

                    patient.setMarathiName(
                            emptyToNull(
                                    getCellValue(
                                            row.getCell(1)
                                    )
                            )
                    );


                    // -------------------------------------
                    // DATE OF BIRTH
                    // -------------------------------------

                    String dob =
                            getCellValue(
                                    row.getCell(2)
                            );

                    if (
                            dob != null &&
                                    !dob.trim().isEmpty()
                    ) {

                        try {

                            patient.setDateOfBirth(
                                    java.time.LocalDate.parse(
                                            dob.trim()
                                    )
                            );

                        } catch (Exception e) {

                            throw new RuntimeException(
                                    "Date of Birth must be YYYY-MM-DD."
                            );
                        }
                    }


                    // -------------------------------------
                    // AGE
                    // -------------------------------------

                    String age =
                            getCellValue(
                                    row.getCell(3)
                            );

                    if (
                            age != null &&
                                    !age.trim().isEmpty()
                    ) {

                        try {

                            int ageValue =
                                    Integer.parseInt(
                                            age.trim()
                                    );

                            if (
                                    ageValue < 0 ||
                                            ageValue > 150
                            ) {

                                throw new RuntimeException(
                                        "Age must be between 0 and 150."
                                );
                            }

                            patient.setAge(
                                    ageValue
                            );

                        } catch (
                                NumberFormatException e
                        ) {

                            throw new RuntimeException(
                                    "Age must be a valid number."
                            );
                        }
                    }


                    // -------------------------------------
                    // GENDER
                    // -------------------------------------

                    String gender =
                            getCellValue(
                                    row.getCell(4)
                            );

                    if (
                            gender == null ||
                                    gender.trim().isEmpty()
                    ) {

                        throw new RuntimeException(
                                "Gender is required."
                        );
                    }

                    patient.setGender(
                            gender.trim()
                    );


                    // -------------------------------------
                    // MOBILE
                    // -------------------------------------

                    String mobile =
                            getCellValue(
                                    row.getCell(5)
                            );

                    if (
                            mobile == null ||
                                    !mobile.matches(
                                            "\\d{10}"
                                    )
                    ) {

                        throw new RuntimeException(
                                "Mobile number must contain exactly 10 digits."
                        );
                    }

                    patient.setMobile(
                            mobile
                    );


                    // -------------------------------------
                    // EMAIL
                    // -------------------------------------

                    patient.setEmail(
                            emptyToNull(
                                    getCellValue(
                                            row.getCell(6)
                                    )
                            )
                    );


                    // -------------------------------------
                    // ADDRESS
                    // -------------------------------------

                    patient.setAddress(
                            emptyToNull(
                                    getCellValue(
                                            row.getCell(7)
                                    )
                            )
                    );


                    // -------------------------------------
                    // IDENTITY NUMBER
                    // -------------------------------------

                    String identity =
                            getCellValue(
                                    row.getCell(8)
                            );

                    if (
                            identity != null &&
                                    !identity.trim().isEmpty() &&
                                    !identity.matches(
                                            "\\d{12}"
                                    )
                    ) {

                        throw new RuntimeException(
                                "Identity number must contain exactly 12 digits."
                        );
                    }

                    patient.setIdentityNumber(
                            emptyToNull(identity)
                    );


                    // -------------------------------------
                    // BLOOD GROUP
                    // -------------------------------------

                    patient.setBloodGroup(
                            emptyToNull(
                                    getCellValue(
                                            row.getCell(9)
                                    )
                            )
                    );


                    // -------------------------------------
                    // ALLERGIES
                    // -------------------------------------

                    patient.setAllergies(
                            emptyToNull(
                                    getCellValue(
                                            row.getCell(10)
                                    )
                            )
                    );


                    // -------------------------------------
                    // EXISTING CONDITIONS
                    // -------------------------------------

                    patient.setExistingConditions(
                            emptyToNull(
                                    getCellValue(
                                            row.getCell(11)
                                    )
                            )
                    );


                    // -------------------------------------
                    // CURRENT MEDICATIONS
                    // -------------------------------------

                    patient.setCurrentMedications(
                            emptyToNull(
                                    getCellValue(
                                            row.getCell(12)
                                    )
                            )
                    );


                    // -------------------------------------
                    // MEDICAL HISTORY
                    // -------------------------------------

                    patient.setMedicalHistory(
                            emptyToNull(
                                    getCellValue(
                                            row.getCell(13)
                                    )
                            )
                    );


                    // -------------------------------------
                    // PREVIOUS SURGERIES
                    // -------------------------------------

                    patient.setPreviousSurgeries(
                            emptyToNull(
                                    getCellValue(
                                            row.getCell(14)
                                    )
                            )
                    );


                    // -------------------------------------
                    // EMERGENCY CONTACT
                    // -------------------------------------

                    String emergency =
                            getCellValue(
                                    row.getCell(15)
                            );

                    if (
                            emergency != null &&
                                    !emergency.trim().isEmpty() &&
                                    !emergency.matches(
                                            "\\d{10}"
                                    )
                    ) {

                        throw new RuntimeException(
                                "Emergency contact must contain exactly 10 digits."
                        );
                    }

                    patient.setEmergencyContact(
                            emptyToNull(emergency)
                    );


                    // =====================================
                    // SAVE
                    // =====================================

                    // =====================================
// DUPLICATE CHECK
// =====================================

                    if (
                            patient.getMobile() != null &&
                                    patientRepository.existsByMobile(
                                            patient.getMobile()
                                    )
                    ) {

                        throw new RuntimeException(
                                "Patient already exists with mobile number: "
                                        + patient.getMobile()
                        );
                    }


                    if (
                            patient.getIdentityNumber() != null &&
                                    !patient.getIdentityNumber().trim().isEmpty() &&
                                    patientRepository.existsByIdentityNumber(
                                            patient.getIdentityNumber()
                                    )
                    ) {

                        throw new RuntimeException(
                                "Patient already exists with identity number: "
                                        + patient.getIdentityNumber()
                        );
                    }


                    // =====================================
                    // SAVE
                    // =====================================

                    createPatient(patient);

                    successful++;

                } catch (Exception e) {

                    failed++;

                    Map<String, Object> error =
                            new LinkedHashMap<>();

                    error.put(
                            "row",
                            rowIndex + 1
                    );

                    error.put(
                            "message",
                            e.getMessage()
                    );

                    errors.add(error);
                }
            }


        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to process Excel file: "
                            + e.getMessage()
            );
        }


        // =================================================
        // RESULT
        // =================================================

        result.put(
                "success",
                failed == 0
        );

        result.put(
                "totalRows",
                totalRows
        );

        result.put(
                "successful",
                successful
        );

        result.put(
                "failed",
                failed
        );

        result.put(
                "errors",
                errors
        );

        return result;
    }
    // =====================================================
// EXCEL CELL VALUE
// =====================================================

    private String getCellValue(
            Cell cell) {

        if (cell == null) {
            return null;
        }

        DataFormatter formatter =
                new DataFormatter();

        String value =
                formatter.formatCellValue(cell);

        return value == null
                ? null
                : value.trim();
    }


// =====================================================
// EMPTY STRING → NULL
// =====================================================

    private String emptyToNull(
            String value) {

        if (
                value == null ||
                        value.trim().isEmpty()
        ) {
            return null;
        }

        return value.trim();
    }


// =====================================================
// CHECK EMPTY ROW
// =====================================================

    private boolean isRowEmpty(
            Row row) {

        DataFormatter formatter =
                new DataFormatter();

        for (
                int i = 0;
                i < 16;
                i++
        ) {

            Cell cell =
                    row.getCell(i);

            if (
                    cell != null &&
                            !formatter
                                    .formatCellValue(cell)
                                    .trim()
                                    .isEmpty()
            ) {

                return false;
            }
        }

        return true;
    }
}