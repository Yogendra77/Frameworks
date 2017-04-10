package com.zapprx.testing.end2endtests.regression.PARx;

import static com.zapprx.testing.end2endtests.automation.utils.YamlReader.getYamlValue;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.zapprx.testing.end2endtests.basetests.BaseTest;

/**
 * Steps Automated: Step1: Login admin and select PA Workflow mode as 'Manual'
 * for 'Neuromuscular Clinic'. Step 2: Switch to physician portal, login as
 * physician and register new patient. Step 3: Complete Rx and submit PA to
 * PARx. Step 4: Now switch to pharmacy portal, access PA and update PA status
 * to 'Submitted to Payer' and then again update PA status to 'Denied' and also
 * fetch the notification count. Step 5: Login physician and Save PA Under
 * Appeal. Step 6: Now switch to pharmacy portal and verify that no new
 * notification is present. Step 7: Login physician and Re-Submit PA Under
 * Appeal to PARx. Step 8: Now switch to pharmacy portal and verify that new
 * notification is present.
 * 
 * @author QAIT\priyankasingh
 */

public class ZQ285Test extends BaseTest {
	String[] patientLastNameDOBAndGender = null;

	private ZQ285Test(String baseUrl) {
		super("admin.baseUrl");
	}

	@Test
	public void Step01_loginAdminSelectManualWorkflowMode() {
		test.patientCommonWorkflow.verifyAdminIsAbleToLogin(getYamlValue("admin.emailId1"),
				getYamlValue("admin.password1"));
		test.pracPage.selectPractice(getYamlValue("admin.practice.practice2"));
		test.pracPage.clickOnEditPractice();
		test.pracPage.selectPAWorkflowMode(getYamlValue("admin.workflowMode.mode1"));
		test.patientCommonWorkflow.savePracProfileAndLogout();
	}

	@Test
	public void Step02_loginPhysicianRegPatAndCompleteProfile() {
		test.patientCommonWorkflow.switchToPhysicianPortal(getYamlValue("physician.emailPA"),
				getYamlValue("physician.passwordPA"));
		patientLastNameDOBAndGender = test.patientCommonWorkflow.registerPatient();
		test.patientRegistrationPage.clickOnCompleteProfile();
		test.patientCommonWorkflow.completeProfile(getYamlValue("physician.insuranceInfo.insuranceName5"),
				getYamlValue("physician.insuranceInfo.policyId1"), getYamlValue("physician.insuranceInfo.groupNumber1"),
				getYamlValue("physician.insuranceInfo.state5"));
	}

	@Test
	public void Step03_completeRxAndSubmitPAToPARx() {
		test.patientCommonWorkflow.clickPresAndSelectIndication(getYamlValue("physician.indication3.name"));
		boolean remsValue = test.patientCommonWorkflow.prescribePatient(
				getYamlValue("physician.indication3.medicines.medicine2.name"),
				getYamlValue("physician.indication3.diagnosis.diagnosis1"),
				getYamlValue("physician.indication3.medicines.medicine2.pharmacy.pharmacy1"),
				patientLastNameDOBAndGender[2], patientLastNameDOBAndGender[0],
				getYamlValue("physician.indication3.otherDetails"));
		test.dosageCommonWorkflow.selectDosageOptions(getYamlValue("physician.indication3.medicines.medicine2.name"));
		test.patientCommonWorkflow.authorizePres(patientLastNameDOBAndGender[2], remsValue,
				getYamlValue("physician.indication3.medicines.medicine2.name"), getYamlValue("physician.passwordPA"));
		test.authorizationPage.clickReviewOnConfirmationModal();
		test.patientCommonWorkflow.createPASubmitAndVerifyPAStatus(
				getYamlValue("physician.indication3.medicines.medicine2.name"),
				getYamlValue("physician.PASubmissionMsg"), getYamlValue("physician.paStatus.PAStatus1"),
				getYamlValue("physician.priorAuthButton.submit"));
		test.headerPage.logOut();
	}

	@Test
	public void Step04_onPharmacyPortalUpdatePAStatusToDenied() {
		test.patientCommonWorkflow.switchToPharmacyPortal(getYamlValue("pharmacy.emailPA"),
				getYamlValue("pharmacy.password"));
		test.leftnavigationPage.clickOnNotifications();
		test.notificationsPage.clickNotificationMsg(getYamlValue("physician.indication3.medicines.medicine2.name"));
		test.priorAuthPage.updatePAStatus(getYamlValue("pharmacy.paStatus.PAStatus3"));
		test.priorAuthPage.updatePAStatus(getYamlValue("pharmacy.paStatus.PAStatus5"));
		test.notificationsPage.fetchNotificationCount();
		test.headerPage.logOut();
	}

	@Test
	public void Step05_loginPhysicianSavePAUnderAppeal() {
		test.patientCommonWorkflow.switchToPhysicianPortal(getYamlValue("physician.emailPA"),
				getYamlValue("physician.passwordPA"));
		test.leftnavigationPage.clickOnPrescriptionStatus();
		test.rxStatusPage.searchAndSelectPatient(patientLastNameDOBAndGender[0]);
		test.rxStatusPage.clickOnViewRxDetails();
		test.presDetailsPage.verifyPAStatus(getYamlValue("physician.paStatus.PAStatus5"));
		test.presDetailsPage.clickOnPriorAuthorization();
		test.priorAuthorizationPage.savePAUnderAppeal();
		test.priorAuthorizationPage.verifyPAStatus(getYamlValue("physician.paStatus.PAStatus7"));
		test.headerPage.logOut();
	}

	@Test
	public void Step06_onPharmacyPortalVerifyNewNotification() {
		test.patientCommonWorkflow.switchToPharmacyPortal(getYamlValue("pharmacy.emailPA"),
				getYamlValue("pharmacy.password"));
		test.leftnavigationPage.clickOnNotifications();
		test.notificationsPage.verifyNoNewNotification();
		test.notificationsPage.verifyNotificationMessage(getYamlValue("pharmacy.notifications.message2"));
		test.headerPage.logOut();
	}

	@Test
	public void Step07_loginPhysicianReSubmitPAUnderAppeal() {
		test.patientCommonWorkflow.switchToPhysicianPortal(getYamlValue("physician.emailPA"),
				getYamlValue("physician.passwordPA"));
		test.leftnavigationPage.clickOnPrescriptionStatus();
		test.rxStatusPage.searchAndSelectPatient(patientLastNameDOBAndGender[0]);
		test.rxStatusPage.clickOnViewRxDetails();
		test.presDetailsPage.verifyPAStatus(getYamlValue("physician.paStatus.PAStatus7"));
		test.presDetailsPage.clickOnPriorAuthorization();
		test.priorAuthorizationPage.clickOnSaveSubmitReSubButton(getYamlValue("physician.priorAuthButton.reSubmit"));
		test.priorAuthorizationPage.clickOnOkay();
		test.priorAuthorizationPage.verifyPAStatus(getYamlValue("physician.paStatus.PAStatus6"));
		test.headerPage.logOut();
	}

	@Test
	public void Step08_onPharmacyPortalVerifyNewNotification() {
		test.patientCommonWorkflow.switchToPharmacyPortal(getYamlValue("pharmacy.emailPA"),
				getYamlValue("pharmacy.password"));
		test.leftnavigationPage.clickOnNotifications();
		test.notificationsPage.verifyNotificationMessage(getYamlValue("pharmacy.notifications.message1"));
		test.headerPage.logOut();
	}

	@AfterClass
	public void delete_Patient() {
		test.patientCommonWorkflow.loginAdminAndDeletePatient(patientLastNameDOBAndGender[0]);
	}
}