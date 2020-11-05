package AddressBookREST;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.blz.addressbookrest.controller.AddressBookRestMain;
import com.blz.addressbookrest.controller.AddressBookRestMain.IOService;
import com.blz.addressbookrest.model.Contact;
import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AddressBookRestTest {
	private static Logger log = Logger.getLogger(AddressBookRestTest.class.getName());
	private static AddressBookRestMain addressBookService = null;

	public Contact[] getContactList() {
		Response response = RestAssured.get("/contacts");
		log.info("Contact entries in JSON Server :\n" + response.asString());
		Contact[] arrayOfEmps = new Gson().fromJson(response.asString(), Contact[].class);
		return arrayOfEmps;
	}

	private int updateSalaryToJSONServer(Contact contactData) {
		String contactJSON = new Gson().toJson(contactData);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(contactJSON);
		Response response = request.put("/contacts/" + contactData.id);

		return response.getStatusCode();
	}

	@Before
	public void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 4000;
	}

	@Test
	public void givenEmployeeDataInJSONServer_WhenRetrieved_ShouldmatchTheCount() {
		Contact[] arrayOfContacts = getContactList();
		addressBookService = new AddressBookRestMain(Arrays.asList(arrayOfContacts));
		long entries = addressBookService.countEntries(IOService.REST_IO);
		Assert.assertEquals(10, entries);
	}

	@Test
	public void givenEmployeeData_WhenUpdatedInJSONServer_ShouldSyncWithList() {
		Contact[] arrayOfContacts = getContactList();
		addressBookService = new AddressBookRestMain(Arrays.asList(arrayOfContacts));
		addressBookService.updateContactSalary("DevS", 4000000.00, IOService.REST_IO);
		Contact contactData = addressBookService.getContactData("DevS");
		int statusCode = updateSalaryToJSONServer(contactData);
		Assert.assertEquals(200, statusCode);
	}

	@Test
	public void givenContactData_WhenDeletedInJSONServer_ShouldSyncWithList() {
		Contact[] arrayOfContacts = getContactList();
		addressBookService = new AddressBookRestMain(Arrays.asList(arrayOfContacts));
		Contact contactData = addressBookService.getContactData("DevS");
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		Response response = request.delete("/contacts/" + contactData.id);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
		addressBookService.deleteContactData("DevS", IOService.REST_IO);
		long entries=addressBookService.countEntries(IOService.REST_IO);
		Assert.assertEquals(9, entries);
	}
}
