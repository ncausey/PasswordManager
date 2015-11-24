# Creating an instance
DataHandler needs to be used both for logging in and for managing passwords. To create an instance, call the constructor with 
2parameters. The first parameter is an instance of a SharedPreference, while the second parameter is the password used to 
login. Example:

```
SharedPreferences prefs = getSharedPreferences("Prefs", Context.MODE_PRIVATE); 
DataHandler dH = new DataHandler(prefs, password);
```

## Setting up the initial password
Simply call setInitialUserPassword() with the password as a parameter.

## Logging in
To get the hash of the user's password, do the following:

```
String pwHash = CryptoHelper.hash(password);
```

When logging in we obviously want to compare the hash of the input password to the hash stored. To get the hash of the stored
password, simply call getUserPassword() from your DataHandler object.

## Changing the password
To change the login password, call setUserPassword() with the new password as the first parameter and the old password 
as the second.

## Managing Passwords

To save a new managed password, call saveManagedPassword() with the "name" or key as the first parameter and the password
as the second.

To delete a managed password, call deleteManagedPassword() with the "name" or key as the parameter.

To retrieve an arraylist of key value pairs of all managed passwords, call getAllManagedPasswords()
