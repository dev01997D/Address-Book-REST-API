package com.blz.addressbookrest.controller;

import java.util.ArrayList;
import java.util.List;

import com.blz.addressbookrest.model.Contact;

public class AddressBookRestMain {
	private List<Contact> contactList;

	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO;
	}

	public AddressBookRestMain() {
	}

	public AddressBookRestMain(List<Contact> contactList) {
		this();
		this.contactList = new ArrayList<>(contactList); // Use new memory not the same as provided by client to avoid confusion

	}

	public long countEntries(IOService ioService) {
		if (ioService.equals(IOService.REST_IO))
			return contactList.size();
		return 0;
	}

	public void addContactToAddressBook(Contact contactData, IOService restIo) {
		contactList.add(contactData);
		System.out.println(contactList.size());
	}
}
