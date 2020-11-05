package AddressBookREST;

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

public class AddressBookRestTest {
	private static Logger log = Logger.getLogger(AddressBookRestTest.class.getName());
	private static AddressBookRestMain addressBookService = null;

	public Contact[] getContactList() {
		Response response = RestAssured.get("/contacts");
		log.info("Contact entries in JSON Server :\n" + response.asString());
		Contact[] arrayOfEmps = new Gson().fromJson(response.asString(), Contact[].class);
		return arrayOfEmps;
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
}
