# Address-Book-REST-API
### public Contact[] getContactList()
```
This method is used to retrieve all the Contact from server"
```
### In constructor
```
this.contactList = new ArrayList<>(ContactList);  // Used new memory,  not the same as provided by client to avoid confusion client and api memory loaction
```