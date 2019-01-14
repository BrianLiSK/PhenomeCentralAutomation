package pageObjects;

import org.openqa.selenium.By;

// Contains selectors for accordion sections on the create and view patient info pages.
public interface commonInfoSelectors {
    By patientInfoSection = By.id("HPatientinformation"); // "Patient information"
    By familyHistorySection = By.id("HFamilyhistoryandpedigree"); // "Family history and pedigree"
    By prenatalHistorySection = By.id("HPrenatalandperinatalhistory"); // Prenatal and perinatal history
    By medicalHistorySection = By.id("HMedicalhistory"); // Medical history
    By measurementsSection = By.id("HMeasurements"); // Measurements
    By clinicalSymptomsSection = By.id("HClinicalsymptomsandphysicalfindings"); // Clinical symptoms and physical findings
    By suggestedGenesSection = By.id("HSuggestedGenes"); // Suggested Genes
    By genotypeInfoSection = By.id("HGenotypeinformation"); // Genotype information
    By diagnosisSection = By.id("HDiagnosis"); // Diagnosis


}
