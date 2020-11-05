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

	private Response addContactToJSONServer(Contact contactData) {
		String contactJSON = new Gson().toJson(contactData);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(contactJSON);
		return request.post("/contacts");
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
		Assert.assertEquals(6, entries);
	}
	
	@Test
	public void givenNewEmployee_WhenAddedInJsonServer_ShouldMatchResponseAndCount() {
		Contact[] arrayOfEmps = getContactList();
		addressBookService = new AddressBookRestMain(Arrays.asList(arrayOfEmps));
		Contact contactData = new Contact(0, "Mark ZukerBerg", "M", 3000000, LocalDate.now());
		Response response = addContactToJSONServer(contactData);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(201, statusCode);
		
		contactData = new Gson().fromJson(response.asString(), Contact.class);
		addressBookService.addContactToAddressBook(contactData, IOService.REST_IO);
		long entries = addressBookService.countEntries(IOService.REST_IO);
		Assert.assertEquals(7, entries);
	}

	@Test
	public void givenListOfNewContacts_WhenAdded__ShouldMatchContactsCount() {
		Contact[] arrayOfEmps = getContactList();
		addressBookService = new AddressBookRestMain(Arrays.asList(arrayOfEmps));
		Contact[] arrayOfContact = { new Contact(0, "DevS", "M", 1000000.00, LocalDate.of(2019, 8, 16)),
				new Contact(0, "Kavya", "F", 2000000.00, LocalDate.of(2017, 11, 14)),
				new Contact(0, "Jayesh", "M", 1000000.00, LocalDate.now()) };
		for (Contact contactData : arrayOfContact) {
			Response response = addContactToJSONServer(contactData);
			int statusCode = response.getStatusCode();
			Assert.assertEquals(201, statusCode);
			contactData = new Gson().fromJson(response.asString(), Contact.class);
			addressBookService.addContactToAddressBook(contactData, IOService.REST_IO);
		}
		long entries = addressBookService.countEntries(IOService.REST_IO);
		Assert.assertEquals(10, entries);
	}
}
